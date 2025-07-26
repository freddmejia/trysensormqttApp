package fredd.dev.trysensormqtt.data.remote.implementation


import com.google.gson.Gson
import fredd.dev.trysensormqtt.domain.repository.MqttState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.UUID



class MqttSensorManager(
    host: String,
    port: Int = 1883,
    private val topic: String,
    private val user: String?,
    private val pwd: String?,
    private val onMessage: (String) -> Unit,
    private val onState: (MqttState, String?) -> Unit
) {

    private val brokerUri = buildUri(host, port)
    private var client: MqttClient? = null
    private val gson = Gson()

    private fun buildUri(h: String, p: Int): String =
        when {
            h.startsWith("tcp://") || h.startsWith("ssl://")
                    || h.startsWith("ws://") || h.startsWith("wss://") -> h
            else -> "tcp://$h:$p"
        }

    suspend fun start() = withContext(Dispatchers.IO) {

        onState(MqttState.Connecting, null)

        try {
            client = MqttClient(brokerUri, UUID.randomUUID().toString(), null)

            val options = MqttConnectOptions().apply {
                isAutomaticReconnect = true
                isCleanSession = true
                user?.let { userName = it }
                pwd?.let { password = it.toCharArray() }
            }

            client?.setCallback(object : MqttCallback {

                override fun connectionLost(cause: Throwable?) {
                    onState(MqttState.ConnectionLost, cause?.message)
                }

                override fun messageArrived(t: String?, msg: MqttMessage?) {
                    val raw = msg?.payload?.decodeToString() ?: return
                    val data = runCatching { gson.fromJson(raw, JsonWrapper::class.java).data }
                        .getOrNull()
                    data?.let(onMessage)
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })

            client?.connect(options)
            client?.subscribe(topic)

            onState(MqttState.Connected, null)
        }
        catch (e: Exception) {
            onState(MqttState.Error, e.message)
            e.printStackTrace()
        }
    }

    suspend fun stop() = withContext(Dispatchers.IO) {
        runCatching {
            client?.disconnect()
            client?.close()
        }
    }

    private data class JsonWrapper(val data: String?)
}
