package io.readsee.sdk.util

import android.content.Context

internal object KeyStoreHelper {
    fun saveApiKey(context: Context, apiKey: String) {
        // Save the encrypted API key to SharedPreferences or any other storage solution
        val preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().putString("encrypted_api_key", apiKey).apply()
    }

    fun getApiKey(context: Context): String {
        // Retrieve the encrypted API key from SharedPreferences or any other storage solution
        val preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val encryptedApiKey = preferences.getString("encrypted_api_key", "") ?: ""
        return encryptedApiKey
    }
}
