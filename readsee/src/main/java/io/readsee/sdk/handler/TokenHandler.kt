package io.readsee.sdk.handler

import android.content.Context
import io.readsee.sdk.client.ReadseeClient
import io.readsee.sdk.util.KeyStoreHelper
import org.json.JSONObject

class TokenHandler(private val context: Context) {
    fun handle(token: String) {
        val API_KEY = KeyStoreHelper.getApiKey(context)
        val data = JSONObject()
        data.put("_\$device_id", token)

        ReadseeClient.config(context, API_KEY)
            .createApi()
            .profile(data)
    }
}
