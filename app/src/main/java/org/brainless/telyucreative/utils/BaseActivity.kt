package org.brainless.telyucreative.utils

import android.app.Dialog
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.DialogProgressBinding
import org.brainless.telyucreative.views.authscreen.LoginActivity
import java.lang.Exception

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog
    private var doubleBackToExitPressedOnce = false
    private lateinit var binding : DialogProgressBinding

    fun showErrorSnackBar(message : String, errorMessage: Boolean){
        val snackbar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view

        if(errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }

        snackbar.show()

    }

    fun showProgressDialog(text: String) {

        binding = DialogProgressBinding.inflate(layoutInflater)

        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(binding.root)

        binding.apply {
            tvProgress.text = text
        }

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()


        object : Thread(){
            override fun run() {
                try {
                    sleep(2000)
                    doubleBackToExitPressedOnce = false
                } catch (e: Exception){
                    Log.d(Constant.SPLASH_SCREEN_TAG, e.message.toString())
                }
            }
        }.start()

        supportActionBar?.hide()
    }
}