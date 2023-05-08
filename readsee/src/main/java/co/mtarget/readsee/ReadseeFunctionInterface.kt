package co.mtarget.readsee

import org.json.JSONObject

interface ReadseeAPIInterface {

    fun ping()

    fun event(eventData: JSONObject)

    fun initProfile()

    fun profile(profileData: JSONObject)
}