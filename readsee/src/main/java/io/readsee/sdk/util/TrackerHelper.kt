package io.readsee.sdk.util

import android.content.Context
import io.readsee.sdk.dto.SdkDto

internal object TrackerHelper {
    fun saveTrackerToken(context: Context, sdkDto: SdkDto) {
        // Save the token key to SharedPreferences or any other storage solution
        val preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit()
            .putString("anonymous_id", sdkDto.anonymousId)
            .putString("distinct_id", sdkDto.distinctId)
            .apply()
    }

    fun getTrackerToken(context: Context): SdkDto {
        // Retrieve the token key from SharedPreferences or any other storage solution
        val preferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val anonymousId = preferences.getString("anonymous_id", "") ?: ""
        val distinctId = preferences.getString("distinct_id", "") ?: ""
        return SdkDto(anonymousId, distinctId)
    }
}
