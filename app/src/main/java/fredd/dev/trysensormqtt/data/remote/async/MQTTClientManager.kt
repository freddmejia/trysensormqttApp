package fredd.dev.trysensormqtt.data.remote.async

import android.content.Context
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions

import android.util.Log
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttClientManager(
    private val context: Context,
    private val serverUri: String,
    private val clientId: String
) {

    private lateinit var mqttAndroidClient: MqttAndroidClient
    private var isConnected = false

    /**
     * Inicializa el cliente con el callback
     */
    fun initClient(
        username: String? = null,
        password: String? = null,
        onMessageReceived: (topic: String, message: String) -> Unit
    ) {
        mqttAndroidClient = MqttAndroidClient(context, serverUri, clientId)

        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Log.d("MQTT", "Connected to: $serverURI, reconnect: $reconnect")
            }

            override fun connectionLost(cause: Throwable?) {
                Log.e("MQTT", "Connection lost: ${cause?.message}")
                isConnected = false
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                val msg = message?.toString() ?: ""
                Log.d("MQTT", "Message arrived [$topic]: $msg")
                if (topic != null) {
                    onMessageReceived(topic, msg)
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d("MQTT", "Delivery complete")
            }
        })

        connect(username, password)
    }

    /**
     * Conexión al broker MQTT
     */
    private fun connect(username: String? = null, password: String? = null) {
        val options = MqttConnectOptions().apply {
            isAutomaticReconnect = true
            isCleanSession = true
            if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
                userName = username
                this.password = password.toCharArray()
            }
        }

        try {
            mqttAndroidClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MQTT", "Connected successfully")
                    isConnected = true
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("MQTT", "Connection failed: ${exception?.message}")
                    isConnected = false
                }
            })
        } catch (e: Exception) {
            Log.e("MQTT", "Exception while connecting: ${e.message}")
        }
    }

    /**
     * Suscribirse a un topic
     */
    fun subscribe(topic: String, qos: Int = 1) {
        if (!isConnected) {
            Log.w("MQTT", "Not connected, cannot subscribe yet")
            return
        }
        try {
            mqttAndroidClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d("MQTT", "Subscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e("MQTT", "Failed to subscribe: ${exception?.message}")
                }
            })
        } catch (e: Exception) {
            Log.e("MQTT", "Exception while subscribing: ${e.message}")
        }
    }

    /**
     * Publicar mensaje
     */
    fun publish(topic: String, payload: String, qos: Int = 1, retained: Boolean = false) {
        if (!isConnected) {
            Log.w("MQTT", "Not connected, cannot publish")
            return
        }
        try {
            val message = MqttMessage()
            message.payload = payload.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttAndroidClient.publish(topic, message)
            Log.d("MQTT", "Published: $payload to $topic")
        } catch (e: Exception) {
            Log.e("MQTT", "Exception while publishing: ${e.message}")
        }
    }

    /**
     * Desconectar
     */
    fun disconnect() {
        try {
            mqttAndroidClient.disconnect()
            isConnected = false
            Log.d("MQTT", "Disconnected from broker")
        } catch (e: Exception) {
            Log.e("MQTT", "Exception while disconnecting: ${e.message}")
        }
    }
}