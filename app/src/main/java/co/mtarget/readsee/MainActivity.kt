package co.mtarget.readsee

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import co.mtarget.readsee.client.ReadseeClient
import co.mtarget.readsee.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var tracker: ReadseeAPIInterface

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tracker = ReadseeClient.config(this,"64587b1cb345e900072e688b")
            .createApi()

        tracker.initProfile()

        askNotificationPermission()

        binding.btnTrackEvent.setOnClickListener {
            trackEvent()
        }

        binding.btnUpdateProfile.setOnClickListener {
            updateProfile()
        }
    }

    fun trackEvent() {
        val data = JSONObject()
        data.put("_\$name", "androidsdkevent")
        data.put("_\$city", "Semarang")
        data.put("_\$country", "Indonesia")
        Log.d("MainActivity", "On Click btn Tracker ${data}")
        tracker.event(data)
    }

    fun updateProfile() {
        val data = JSONObject()
        data.put("_\$first_name", binding.etFirstname.text.toString())
        data.put("_\$last_name", binding.etLastname.text.toString())
        data.put("birthdatetext", binding.etBirthdatetext.text.toString())
        data.put("_\$country", binding.etCountry.text.toString())
        Log.d("MainActivity", "On Click btn Update Profile ${data}")
        tracker.profile(data)
    }
}