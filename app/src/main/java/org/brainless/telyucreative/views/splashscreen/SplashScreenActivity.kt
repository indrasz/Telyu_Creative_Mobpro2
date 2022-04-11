package org.brainless.telyucreative.views.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.brainless.telyucreative.databinding.ActivitySplashScreenBinding
import org.brainless.telyucreative.utils.SPLASH_SCREEN_TAG
import org.brainless.telyucreative.views.authscreen.LoginActivity
import java.lang.Exception

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        object : Thread(){
            override fun run() {
                try {
                    sleep(2500)
                    startActivity(Intent(baseContext, LoginActivity::class.java))
                    finish()
                } catch (e: Exception){
                    Log.d(SPLASH_SCREEN_TAG, e.message.toString())
                }
            }
        }.start()

        supportActionBar?.hide()
    }
}