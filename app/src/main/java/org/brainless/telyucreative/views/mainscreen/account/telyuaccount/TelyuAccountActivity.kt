package org.brainless.telyucreative.views.mainscreen.account.telyuaccount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ActivityTelyuAccountBinding

class TelyuAccountActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTelyuAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelyuAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}