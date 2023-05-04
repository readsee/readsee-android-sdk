package co.mtarget.readsee.dto

data class TrackEvent(
    var distinctId: String?,
    var name: String?,
    var custom_props_text : String?,
    var custom_props_number : Int? ,
    var custom_props_date : Long?
)

data class TrackProfile(
    val anonymousId: String?,
    var distinctId: String?,
    var custom_props_text : String?,
    var custom_props_number : Int? ,
    var custom_props_date : Long?
)