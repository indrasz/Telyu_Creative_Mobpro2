package org.brainless.telyucreative.views.mainscreen.account.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

    }
}