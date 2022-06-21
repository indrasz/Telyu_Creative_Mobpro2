package org.brainless.telyucreative.views.authscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ActivityForgotPasswordBinding
import org.brainless.telyucreative.utils.BaseActivity

class ForgotPasswordActivity : BaseActivity() {

    private lateinit var binding : ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        binding.btnResetPassword.setOnClickListener {
            val email : String = binding.edtEmail.text.toString().trim { it <= ' '}

            if (email.isEmpty()) {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
            } else {
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        hideProgressDialog()

                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                resources.getString(R.string.email_sent_success),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }

                    }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupActionBar() {
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            val drawable = getDrawable(R.drawable.bg_bottom_nav)
            setBackgroundDrawable(drawable)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}