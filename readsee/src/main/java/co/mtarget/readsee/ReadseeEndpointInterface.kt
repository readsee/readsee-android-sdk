package co.mtarget.readsee

import co.mtarget.readsee.dto.SdkDto
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface ReadseeEndpointInterface {

    @GET("ping")
    fun ping(): Call<String>

    @POST("track/event")
    fun event(@Header("Authorization") token: String, @Body form: JsonObject): Call<SdkDto>

    @POST("track/profile")
    fun profile(
        @Header("Authorization") token: String,
        @Body form: JsonObject
    ): Call<SdkDto>
}