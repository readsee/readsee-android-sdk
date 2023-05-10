package io.readsee.sdk.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class SdkDto(
    @SerializedName("_\$anonymous_id")
    val anonymousId: String? = null,

    @SerializedName("_\$distinct_id")
    val distinctId: String? = null,
)
