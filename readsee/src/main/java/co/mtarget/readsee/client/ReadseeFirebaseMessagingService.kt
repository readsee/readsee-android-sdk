package co.mtarget.readsee.client

import co.mtarget.readsee.handler.NotificationHandler
import co.mtarget.readsee.handler.TokenHandler
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ReadseeFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Create an instance of the NotificationHandler class and show the notification
        NotificationHandler(applicationContext).handle(remoteMessage)
    }

    override fun onNewToken(token: String) {
        // Create an instance of the TokenHandler class and send token to server
        TokenHandler(applicationContext).handle(token)
    }
}
