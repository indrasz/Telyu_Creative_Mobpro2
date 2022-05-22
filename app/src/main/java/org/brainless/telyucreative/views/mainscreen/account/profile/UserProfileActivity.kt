package org.brainless.telyucreative.views.mainscreen.account.profile

import android.Manifest
import android.app.Activity
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
import org.brainless.telyucreative.databinding.ActivityUserProfileBinding
import org.brainless.telyucreative.data.FireStoreClass
import org.brainless.telyucreative.model.User
import org.brainless.telyucreative.utils.BaseActivity
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.utils.GlideLoader
import org.brainless.telyucreative.views.mainscreen.MainActivity
import java.io.IOException


class UserProfileActivity : BaseActivity(), View.OnClickListener{

    private lateinit var binding : ActivityUserProfileBinding
    private lateinit var mUserDetails: User

    private lateinit var activityLauncher : ActivityResultLauncher<Intent>

    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvCancelUploadProfile.setOnClickListener {
            onBackPressed()
        }

        if (intent.hasExtra(Constant.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constant.EXTRA_USER_DETAILS)!!
        }

        if (mUserDetails.profileCompleted == 0) {

            binding.apply {
                edtFirstName.isEnabled = false
                edtFirstName.setText(mUserDetails.firstName)

                edtLastName.isEnabled = false
                edtLastName.setText(mUserDetails.lastName)

                edtEmail.isEnabled = false
                edtEmail.setText(mUserDetails.email)
            }

        } else {

            binding.apply{

                GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image, ivUserProfileUpload)

                edtFirstName.setText(mUserDetails.firstName)
                edtLastName.setText(mUserDetails.lastName)
                edtProfession.setText(mUserDetails.profession)
                edtDesc.setText(mUserDetails.description)
                edtSosmedInsta.setText(mUserDetails.instagramLink)
                edtSosmedYoutube.setText(mUserDetails.youtubeLink)
                edtSosmedLinkedin.setText(mUserDetails.linkedinLink)

                edtEmail.isEnabled = false
                edtEmail.setText(mUserDetails.email)

            }
       }

        activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {

                try {
                    mSelectedImageFileUri = result.data!!.data!!

                    GlideLoader(this@UserProfileActivity).loadUserPicture(
                        mSelectedImageFileUri!!,
                        binding.ivUserProfileUpload
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@UserProfileActivity,
                        resources.getString(R.string.image_selection_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                Log.e("Request Cancelled", "Image selection cancelled")
            }
        }

        binding.btnSaveProfile.setOnClickListener(this)
        binding.ivUserProfileUpload.setOnClickListener(this)

    }
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_user_profile_upload -> {

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

                R.id.btn_save_profile -> {
                    if (validateUserProfileDetails()) {
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (mSelectedImageFileUri != null) {
                            FireStoreClass().uploadImageToCloudStorage(
                                this@UserProfileActivity,
                                mSelectedImageFileUri,
                                Constant.USER_PROFILE_IMAGE
                            )
                        } else {
                            updateUserProfileDetails()
                        }
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


    private fun validateUserProfileDetails(): Boolean {
        return when {

            TextUtils.isEmpty(binding.edtProfession.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_description), true)
                false
            }

            TextUtils.isEmpty(binding.edtDesc.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_description), true)
                false
            }

            TextUtils.isEmpty(binding.edtSosmedInsta.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_link_insta), true)
                false
            }

            TextUtils.isEmpty(binding.edtSosmedYoutube.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_link_yt), true)
                false
            }

            TextUtils.isEmpty(binding.edtSosmedLinkedin.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_link_linkedin), true)
                false
            }

            else -> {
                true
            }
        }
    }



    private fun updateUserProfileDetails() {

        val userHashMap = HashMap<String, Any>()

        val firstName = binding.edtFirstName.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constant.FIRST_NAME] = firstName
        }

        val lastName = binding.edtLastName.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.lastName) {
            userHashMap[Constant.LAST_NAME] = lastName
        }

        val profession = binding.edtProfession.text.toString().trim { it <= ' ' }
        if (profession != mUserDetails.profession) {
            userHashMap[Constant.PROFESSION] = profession
        }

        val description = binding.edtDesc.text.toString().trim { it <= ' ' }
        if (description != mUserDetails.description) {
            userHashMap[Constant.DESCRIPTION] = description
        }

        val instagramLink = binding.edtSosmedInsta.text.toString().trim { it <= ' ' }
        if (instagramLink != mUserDetails.instagramLink) {
            userHashMap[Constant.INSTAGRAM_LINK] = instagramLink
        }

        val youtubeLink = binding.edtSosmedYoutube.text.toString().trim { it <= ' ' }
        if (youtubeLink != mUserDetails.youtubeLink) {
            userHashMap[Constant.YOUTUBE_LINK] = youtubeLink
        }

        val linkedinLink = binding.edtSosmedLinkedin.text.toString().trim { it <= ' ' }
        if (linkedinLink != mUserDetails.linkedinLink) {
            userHashMap[Constant.LINKEDIN_LINK] = linkedinLink
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constant.IMAGE] = mUserProfileImageURL
        }
        if (mUserDetails.profileCompleted == 0) {
            userHashMap[Constant.COMPLETE_PROFILE] = 1
        }

        FireStoreClass().updateUserProfileData(
            this@UserProfileActivity,
            userHashMap
        )
    }

    fun userProfileUpdateSuccess() {

        hideProgressDialog()

        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }
}