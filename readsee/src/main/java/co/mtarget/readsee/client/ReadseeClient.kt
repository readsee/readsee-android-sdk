package co.mtarget.readsee.client

import android.content.Context
import co.mtarget.readsee.ReadseeAPIInterface
import co.mtarget.readsee.ReadseeEndpointInterface
import co.mtarget.readsee.dto.SdkDto
import co.mtarget.readsee.util.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ReadseeClient {

    companion object {
        fun config(context: Context, apiKey: String): ReadseeClientBuilder {
            return ReadseeClientBuilder(context, apiKey)
        }
    }

    class ReadseeClientBuilder(private val context: Context, private val apikey: String) {

        private val retrofitBuilder: Retrofit.Builder
            get() = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())

        fun createApi() : ReadseeAPIInterface {
            val baseUrl: String = Constants.BASE_URL
            val retrofit = retrofitBuilder.baseUrl(baseUrl).build()
            val endpointInterface = retrofit.create<ReadseeEndpointInterface>()
            return ReadSeeApi(context, endpointInterface, this.apikey)
        }

    }

    class ReadSeeApi(context: Context, endpointInterface: ReadseeEndpointInterface, apiKey: String) : ReadseeAPIInterface {
        val endpointInterface : ReadseeEndpointInterface
        val apiKey: String
        var firebaseToken: String? = null
        var anonymous_id: String? = null
        var distinct_id : String? = null

        init {
            this.endpointInterface = endpointInterface
            this.apiKey = apiKey
            if (FirebaseApp.getApps(context).isEmpty()) setUpFirebase(context)
            profile(JSONObject())
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

        override fun ping(): String {
            return endpointInterface.ping(this.apiKey).execute().body() ?: ""
        }

        override fun event(form: JSONObject): SdkDto {
            val res = endpointInterface.event(this.apiKey, form).execute().body() ?: JSONObject()
            val sdkRes = SdkDto(res.getString("_\$anonymousId"), res.getString("_\$distinctId"))
            return sdkRes
        }

        override fun profile(form: JSONObject): SdkDto {
            val default = form
            if (form.getString("_\$distinctId").isNullOrEmpty()) default.put("_\$distinctId", this.distinct_id ?: "")
            default.put("_\$anonymousId", this.anonymous_id ?: "")
            default.put("_\$deviceId", this.firebaseToken ?: "")
            val res = endpointInterface.profile(this.apiKey, form).execute().body() ?: JSONObject()
            val sdkRes = SdkDto(res.getString("_\$anonymousId"), res.getString("_\$distinctId"))
            this.distinct_id = sdkRes.distinctId
            this.anonymous_id = sdkRes.anonymousId
            return sdkRes
        }
    }
}