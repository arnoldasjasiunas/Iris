package com.example.irisrouter.srd

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.irisrouter.Constants
import kotlinx.coroutines.launch

class SRDRouterCommunicationService: LifecycleService() {
    private val eventManager = SRDRouterEventManager
    private val handler = SRDRouterCommunicationHandler()
    private var service: Messenger? = null

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private val messenger: Messenger = Messenger(IncomingHandler(Looper.getMainLooper()))

    private var isBound = false

    /**
     * Missing documentation
     */
    private val components = arrayListOf(Constants.Servers.SRD_Routing_SERVER)

    override fun onCreate() {
        super.onCreate()
        doBindService()
    }

    override fun onDestroy() {
        doUnbindService()
        super.onDestroy()
    }

    private fun doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because there is no reason to be able to let other
        // applications replace our component.
        val routingService = Intent()
        routingService.component = ComponentName(
            "com.iris.srd_router",
            "com.iris.srd_router.services.RoutingMessengerService"
        )
        isBound = bindService(routingService, connection, BIND_AUTO_CREATE)
    }

    private fun doUnbindService() {
        if (isBound) {
            // If we have received the service, and hence registered with
            // it, then now is the time to unregister.
            if (service != null) {
                val msg = Message.obtain(
                    null,
                    Constants.SRDServiceOp.MSG_UNREGISTER_CLIENT
                )
                sendMessageToSRDRouterService(msg)
            }
            // Detach our existing connection.
            unbindService(connection)
            isBound = false
        }
    }

    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {

            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            this@SRDRouterCommunicationService.service = Messenger(service)

            val msg = Message.obtain(null, Constants.SRDServiceOp.MSG_REGISTER_CLIENT)
            msg.data = Bundle().apply {
                putStringArrayList(Constants.Servers.SERVERS_EXTRA, components)
            }
            sendMessageToSRDRouterService(msg)
            lifecycleScope.launch {
                SRDRouterEventManager.onRegisteredWithSRDRouter()
            }

            Log.d(
                TAG,
                "onServiceConnected() called with: className = [$className], service = [$service]"
            )
        }

        override fun onServiceDisconnected(className: ComponentName) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            service = null
            lifecycleScope.launch {
                SRDRouterEventManager.onDisconnectedFromSRDRouter()
            }
            Log.d(TAG, "onServiceDisconnected called with: className = [$className]")
        }
    }

    private fun sendMessageToSRDRouterService(msg: Message) {
        msg.replyTo = messenger
        // We want to monitor the service for as long as we are
        // connected to it.
        try {
            service?.send(msg)
                ?: Log.w(TAG, "Should not call when service is not ready:")

        } catch (e: RemoteException) {
            // In this case the service has crashed before we could even
            // do anything with it; we can count on soon being
            // disconnected (and then reconnected if it can be restarted)
            // so there is no need to do anything here.
        }
    }

    inner class IncomingHandler(mainLooper: Looper) : Handler(mainLooper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constants.SRDServiceOp.ROUTING_MSG_SERVE -> {
                    val m = msg.data.getString(Constants.CounterApp.MSG_KEY_COUNT_LABEL, "")
                    val p = msg.data.getString(Constants.CounterApp.MSG_KEY_COUNT_VALUE, "")
                    // instead of onHandleSRDRoutingServerMessage(..)
                    lifecycleScope.launch {
                        handler.handleMessage(m, p)
                    }
                }
                else ->
                    Log.d(TAG, "unhandled message received: ${msg.what}")
                    // instead of onHandleSRDMessageReceivedFromService(msg)
            }
        }
    }

    companion object {
        val TAG: String = SRDRouterCommunicationService::class.java.simpleName
    }
}