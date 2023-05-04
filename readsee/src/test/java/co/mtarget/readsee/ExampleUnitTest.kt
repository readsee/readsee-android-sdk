package co.mtarget.readsee

import android.app.Activity
import co.mtarget.readsee.client.ReadseeClient
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testConfig() {
        val context = Activity()
        val api = ReadseeClient.config(context,"test")
            .createApi()

        println(api.ping())

    }
}