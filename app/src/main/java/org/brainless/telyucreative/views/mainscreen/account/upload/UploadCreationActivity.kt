package org.brainless.telyucreative.views.mainscreen.account.upload

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ActivityUploadCreationBinding
import org.brainless.telyucreative.utils.BaseActivity
import org.brainless.telyucreative.utils.Constant

class UploadCreationActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUploadCreationBinding

    private var mSelectedImageFileUri: Uri? = null
    private var mProductImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        binding.btnCancelUpload.setOnClickListener{
            onBackPressed()
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

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_creation_image -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constant.showImageChooser(this@UploadCreationActivity)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constant.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_upload_creation -> {

                }
            }
        }
    }
}