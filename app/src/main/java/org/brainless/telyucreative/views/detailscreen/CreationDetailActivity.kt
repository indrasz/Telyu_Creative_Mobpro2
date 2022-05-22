package org.brainless.telyucreative.views.detailscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.brainless.telyucreative.databinding.ActivityCreationDetailBinding

class CreationDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreationDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         binding = ActivityCreationDetailBinding.inflate(layoutInflater)
         setContentView(binding.root)

    }
}