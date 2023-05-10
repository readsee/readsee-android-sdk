package co.mtarget.readsee

import org.json.JSONObject

interface ReadseeAPIInterface {

    fun ping()

    fun event(eventName: String, eventData: JSONObject)

    fun profile(profileData: JSONObject)

    fun logout()
}