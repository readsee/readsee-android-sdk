package co.mtarget.readsee.client

import co.mtarget.readsee.ReadseeEndpointInterface
import co.mtarget.readsee.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal object ReadseeRequest {
    val instance : ReadseeEndpointInterface by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReadseeEndpointInterface::class.java)
    }
}
