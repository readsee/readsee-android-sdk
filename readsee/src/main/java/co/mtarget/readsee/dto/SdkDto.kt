package co.mtarget.readsee.dto

import java.util.*

data class SdkDto(
    val anonymousId: String? = null,
    val distinctId: String? = null,
)

data class SdkToken(
    val id: String = "",
    val name: String = "",
    val companyId: String = "",
    val createdAt: Long = Date().time,
    val expiredAt: Long = -1,
)