package org.brainless.telyucreative.views.mainscreen.account.upload

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ActivityUploadCreationBinding
import org.brainless.telyucreative.data.FireStoreClass
import org.brainless.telyucreative.model.Creation
import org.brainless.telyucreative.utils.BaseActivity
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.utils.GlideLoader
import java.io.IOException

class UploadCreationActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityUploadCreationBinding
    private lateinit var activityLauncher : ActivityResultLauncher<Intent>

    private var mSelectedImageFileUri: Uri? = null
    private var mProductImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadCreationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {

                binding.ivClickUploadImage.visibility = View.GONE
                mSelectedImageFileUri = result.data!!.data!!

                try {

                    GlideLoader(this@UploadCreationActivity).loadCreationPicture(
                        mSelectedImageFileUri!!,
                        binding.ivCreationImage
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@UploadCreationActivity,
                        resources.getString(R.string.image_selection_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Log.e("Request Cancelled", "Image selection cancelled")
            }
        }

        binding.apply {
            btnCancelUpload.setOnClickListener {
                onBackPressed()
            }
            btnUploadCreation.setOnClickListener(this@UploadCreationActivity)
            ivCreationImage.setOnClickListener(this@UploadCreationActivity)
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
                        Constant.showImageChooser(activityLauncher)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constant.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_upload_creation -> {
                    if (validateCreationDetails()) {
                        uploadCreationImage()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Constant.showImageChooser(activityLauncher)

            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun validateCreationDetails() : Boolean{
        return when {
            mSelectedImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_creation_image), true)
                false
            }

            TextUtils.isEmpty(binding.edtTitle.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_creation_title), true)
                false
            }

            TextUtils.isEmpty(binding.edtDesc.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_creation_desc), true)
                false
            }

            TextUtils.isEmpty(binding.spCategory.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_creation_category),
                    true
                )
                false
            }

            TextUtils.isEmpty(binding.edtUrlLink.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_creatio_link),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }

    private fun uploadCreationImage() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FireStoreClass().uploadImageToCloudStorage(
            this@UploadCreationActivity,
            mSelectedImageFileUri,
            Constant.CREATION_IMAGE
        )
    }

    fun imageUploadSuccess(imageURL: String) {

        // Initialize the global image url variable.
        mProductImageURL = imageURL

        uploadCreationDetails()
    }


    private fun uploadCreationDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at a time of login.
        val username =
            this.getSharedPreferences(Constant.TELYUCREATIVE_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constant.LOGGED_IN_USERNAME, "")!!

        // Here we get the text from editText and trim the space
        val creation = Creation(
            FireStoreClass().getCurrentUserID(),
            username,
            binding.edtTitle.text.toString().trim { it <= ' ' },
            binding.edtDesc.text.toString().trim { it <= ' ' },
            binding.spCategory.text.toString().trim { it <= ' ' },
            binding.edtUrlLink.text.toString().trim { it <= ' ' },
            mProductImageURL
        )

        FireStoreClass().uploadProductDetails(this@UploadCreationActivity, creation)
    }

    fun creationUploadSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@UploadCreationActivity,
            resources.getString(R.string.creation_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}