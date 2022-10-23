package org.d3ifcool.telcrav.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import org.d3ifcool.telcrav.databinding.ActivitySplashScreenBinding
import org.d3ifcool.telcrav.ui.authscreen.LoginActivity
import org.d3ifcool.telcrav.utils.Constant.SPLASH_SCREEN_TAG
import java.lang.Exception

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.insetsController?.hide(WindowInsets.Type.statusBars())

        object : Thread() {
            override fun run() {
                try {
                    sleep(2000)
                    startActivity(Intent(baseContext, LoginActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Log.d(SPLASH_SCREEN_TAG, e.message.toString())
                }
            }
        }.start()

        supportActionBar?.hide()
    }
}