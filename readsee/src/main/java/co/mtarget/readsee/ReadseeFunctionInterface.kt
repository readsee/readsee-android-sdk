package co.mtarget.readsee

import co.mtarget.readsee.dto.SdkDto
import org.json.JSONObject


interface ReadseeAPIInterface {

    fun ping(): String

    fun event(form: JSONObject): SdkDto

    fun profile(form: JSONObject): SdkDto
}