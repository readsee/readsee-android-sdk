package co.mtarget.readsee.handler

import android.content.Context
import co.mtarget.readsee.client.ReadseeClient
import co.mtarget.readsee.util.KeyStoreHelper
import org.json.JSONObject

internal class TokenHandler(private val context: Context) {
    fun handle(token: String) {
        val API_KEY = KeyStoreHelper.getApiKey(context)
        val data = JSONObject()
        data.put("_\$device_id", token)

        ReadseeClient.config(context, API_KEY)
            .createApi()
            .profile(data)
    }
}
