package org.brainless.telyucreative.views.authscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ActivityLoginBinding
import org.brainless.telyucreative.utils.BaseActivity
import org.brainless.telyucreative.views.mainscreen.MainActivity

class LoginActivity :  BaseActivity(), View.OnClickListener {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvForgotPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.tvRegisterOptionClick.setOnClickListener(this)

        supportActionBar?.hide()

//        setupActionBar()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.tv_forgot_password -> {
                    startActivity(
                        Intent(
                            baseContext, ForgotPasswordActivity::class.java
                        )
                    )
                }

                R.id.btn_login -> {
//                    logInRegisteredUser()
                    startActivity(
                        Intent(
                            baseContext, MainActivity::class.java
                        )
                    )
                }

                R.id.tv_register_option_click -> {
                    startActivity(
                        Intent(
                            baseContext, RegisterActivity::class.java
                        )
                    )
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.edtEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(binding.edtPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val email = binding.edtEmail.text.toString().trim { it <= ' ' }
            val password = binding.edtPassword.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    // Hide the progress dialog
                    hideProgressDialog()

                    if (task.isSuccessful) {

                        startActivity(
                            Intent(
                                baseContext, MainActivity::class.java

                            )
                        )
                        finish()

                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }


}