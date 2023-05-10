package io.readsee.sdk.client

import android.content.Context
import android.util.Log
import io.readsee.sdk.ReadseeAPIInterface
import io.readsee.sdk.ReadseeEndpointInterface
import io.readsee.sdk.dto.SdkDto
import io.readsee.sdk.util.Constants
import io.readsee.sdk.util.KeyStoreHelper
import io.readsee.sdk.util.TrackerHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReadseeClient {

    companion object {
        fun config(context: Context, apiKey: String): ReadseeClientBuilder {
            KeyStoreHelper.saveApiKey(context, apiKey)
            return ReadseeClientBuilder(context, apiKey)
        }
    }

    class ReadseeClientBuilder(private val context: Context, private val apikey: String) {
        fun createApi() : ReadseeAPIInterface {
            return ReadSeeApi(context, ReadseeRequest.instance, this.apikey)
        }
    }

    class ReadSeeApi(context: Context, endpointInterface: ReadseeEndpointInterface, apiKey: String) : ReadseeAPIInterface {
        private val endpointInterface : ReadseeEndpointInterface
        private val apiKey: String
        private var firebaseToken: String? = null
        private var anonymousId: String? = null
        private var distinctId : String? = null
        private var context: Context

        init {
            this.context = context
            this.endpointInterface = endpointInterface
            this.apiKey = apiKey
            if (FirebaseApp.getApps(context).isEmpty()) setUpFirebase(context)
            initProfile()
        }

        private fun setUpFirebase(context: Context) {
            FirebaseApp.initializeApp(context,
                FirebaseOptions.Builder()
                    .setApiKey(Constants.FIREBASE_APIKEY)
                    .setApplicationId(Constants.APP_ID)
                    .setGcmSenderId(Constants.GCM_SENDER_ID)
                    .setProjectId(Constants.PROJECT_ID)
                    .setStorageBucket(Constants.STORAGE_BUCKET)
                    .build()
            )

            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result
                this.firebaseToken = token
            })
        }

        override fun ping() {
            endpointInterface.ping()
                .enqueue(object: Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            Log.d("ReadseeClient", "ping Request Success: ${response.body()}")
                        } else {
                            Log.d("ReadseeClient", "ping Request Error: ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("ReadseeClient", "ping Network Error: ${t.localizedMessage}")
                    }
                })
        }

        override fun event(eventName: String, eventData: JSONObject) {
            val sdkDto = TrackerHelper.getTrackerToken(this.context)
            anonymousId = sdkDto.anonymousId
            distinctId = sdkDto.distinctId

            if (anonymousId?.isNotEmpty() == true && distinctId?.isNotEmpty() == true) {
                val form = Gson().fromJson(eventData.toString(), JsonObject::class.java)
                form.addProperty("_\$anonymous_id", anonymousId)
                form.addProperty("_\$distinct_id", distinctId)
                form.addProperty("_\$name", eventName)

                sendEvent(form)
            }
        }

        private fun sendEvent(form: JsonObject) {
            endpointInterface.event("Bearer $apiKey", form)
                .enqueue(object: Callback<SdkDto>{
                    override fun onResponse(call: Call<SdkDto>, response: Response<SdkDto>) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                TrackerHelper.saveTrackerToken(this@ReadSeeApi.context, it)
                            }
                        } else {
                            Log.d("ReadseeClient", "event Request Error: ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<SdkDto>, t: Throwable) {
                        Log.d("ReadseeClient", "event Network Error: ${t.localizedMessage}")
                    }
                })
        }

        private fun initProfile() {
            val sdkDto = TrackerHelper.getTrackerToken(this.context)
            anonymousId = sdkDto.anonymousId
            distinctId = sdkDto.distinctId

            if (anonymousId.isNullOrEmpty() || distinctId.isNullOrEmpty()) {
                val profileData = JsonObject()
                profileData.addProperty("_\$anonymous_id", anonymousId ?: "")
                profileData.addProperty("_\$distinct_id", distinctId ?: "")

                if (!firebaseToken.isNullOrEmpty())
                    profileData.addProperty("_\$device_id", firebaseToken ?: "")

                sendUpdateProfile(profileData)
            }
        }

        override fun profile(profileData: JSONObject) {
            val sdkDto = TrackerHelper.getTrackerToken(this.context)
            anonymousId = sdkDto.anonymousId
            distinctId = sdkDto.distinctId

            if (anonymousId?.isNotEmpty() == true && distinctId?.isNotEmpty() == true) {
                val form = Gson().fromJson(profileData.toString(), JsonObject::class.java)
                form.addProperty("_\$anonymous_id", anonymousId)

                if (firebaseToken?.isNotEmpty() == true)
                    form.addProperty("_\$device_id", firebaseToken)

                if (form.get("_\$device_id")?.asString?.isNotEmpty() == true)
                    form.addProperty("_\$device_id", form.get("_\$device_id")?.asString)

                if (form.get("_\$distinct_id")?.asString.isNullOrEmpty())
                    form.addProperty("_\$distinct_id", distinctId)

                sendUpdateProfile(form)
            }
        }

        private fun sendUpdateProfile(form: JsonObject) {
            endpointInterface.profile("Bearer $apiKey", form)
                .enqueue(object: Callback<SdkDto>{
                    override fun onResponse(call: Call<SdkDto>, response: Response<SdkDto>) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                TrackerHelper.saveTrackerToken(this@ReadSeeApi.context, it)
                            }
                        } else {
                            Log.d("ReadseeClient", "profile Request Error: ${response.errorBody()}")
                        }
                    }

                    override fun onFailure(call: Call<SdkDto>, t: Throwable) {
                        Log.d("ReadseeClient", "profile Network Error: ${t.localizedMessage}")
                    }
                })
        }

        override fun logout() {
            TrackerHelper.saveTrackerToken(this.context, SdkDto("", ""))
        }
    }
}