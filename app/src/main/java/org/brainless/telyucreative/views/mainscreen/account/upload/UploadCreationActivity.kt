package org.brainless.telyucreative.views.mainscreen.account.upload

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ActivityUploadKaryaBinding
import org.brainless.telyucreative.views.mainscreen.MainActivity
import org.brainless.telyucreative.views.mainscreen.account.AccountFragment

class UploadCreationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadKaryaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadKaryaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        binding.btnCancelUpload.setOnClickListener{
            onBackPressed()
        }

    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}