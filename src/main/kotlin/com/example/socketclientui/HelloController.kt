package com.example.socketclientui

import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.text.Text
import org.json.JSONObject

class HelloController {
    lateinit var topicsInfo: Text
    lateinit var pathInfo: Text
    lateinit var uriInfo: Text
    lateinit var topicsTextField: TextField
    lateinit var pathSocketTextField: TextField
    lateinit var uriSocketTextField: TextField
    lateinit var notificationsShow: TextArea
    lateinit var buttonConnect: Button

    private lateinit var uri: String
    private lateinit var path: String
    private lateinit var listTopics: List<String>

    @FXML
    private lateinit var welcomeText: Label

    @FXML
    private fun onHelloButtonClick() {
        welcomeText.text = "Welcome to the test socket io application"
        notificationsShow.setPrefSize(1000.0, 1000.0)
        notificationsShow.text = "Notifications"

        uri = uriSocketTextField.text
        path = pathSocketTextField.text

        val topics = topicsTextField.text
        listTopics = topics.split(",").toList()

        connectToSocket(uri, path, listTopics)
    }

    private fun connectToSocket(uri: String, path: String, topics: List<String>) {
        println("URI to connect socket: $uri")
        println("PATH to connect socket: $path")
        val opts = IO.Options()
        opts.path = path

        val mSocket = IO.socket(uri, opts)

        topics.forEach {
            mSocket.on(it, onListen)
        }

        mSocket.on("connect", onListen)
        mSocket.on("message", onListen)
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("reconnect", onReconnect)
        mSocket.on("reconnecting", onReconnecting)

        mSocket.connect()

        if (mSocket.connected()) {
            println("The socket is connected")
        } else {
            println("The socket cannot connect")
        }
    }

    private val onListen = Emitter.Listener { args ->
        notificationsShow.text = ""
        try {

            if (args.isNotEmpty()) {
                val data = args[0] as JSONObject

                notificationsShow.text = data.toString(4)
                println(data)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private val onConnect = Emitter.Listener { args ->
        println("Connecting to socket")
        println(args)
        modifiedTextInButton()
    }

    private fun modifiedTextInButton() {
        Platform.runLater {
            buttonConnect.text = "Connected to socket Success!"
            buttonConnect.isDisable = true

            uriInfo.text = "URI: $uri"
            pathInfo.text = "PATH: $path"
            topicsInfo.text = "TOPICS: $listTopics"

            uriSocketTextField.isVisible = false
            pathSocketTextField.isVisible = false
            topicsTextField.isVisible = false
        }
    }

    private val onReconnect = Emitter.Listener { args ->
        println(args)
    }

    private val onReconnecting = Emitter.Listener { args ->
        println(args)
    }
}