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
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ActivityUserProfileBinding
import org.brainless.telyucreative.datastore.FireStoreClass
import org.brainless.telyucreative.model.User
import org.brainless.telyucreative.utils.BaseActivity
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.utils.GlideLoader
import org.brainless.telyucreative.views.mainscreen.MainActivity
import org.brainless.telyucreative.views.mainscreen.account.AccountFragment
import java.io.IOException


class UserProfileActivity : BaseActivity(), View.OnClickListener{

    private lateinit var binding : ActivityUserProfileBinding
    private lateinit var mUserDetails: User
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

//         If the profile is incomplete then user is from login screen and wants to complete the profile.
        if (mUserDetails.profileCompleted == 0) {
            // Update the title of the screen to complete profile.

            binding.apply {

                // Here, the some of the edittext components are disabled because it is added at a time of Registration.
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

                // Set the existing values to the UI and allow user to edit except the Email ID.
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

        getImageProfile()

        binding.btnSaveProfile.setOnClickListener(this@UserProfileActivity)
        binding.ivUserProfileUpload.setOnClickListener(this@UserProfileActivity)

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
                        Constant.showImageChooser(this@UserProfileActivity)
                    } else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constant.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_save_profile -> {
                    // Show the progress dialog.
                    if (validateUserProfileDetails()) {

                        // Show the progress dialog.
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
                Constant.showImageChooser(this@UserProfileActivity)
            } else {
                //Displaying another toast if permission is not granted
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

            // We have kept the user profile picture is optional.
            // The FirstName, LastName, and Email Id are not editable when they come from the login screen.
            // The Radio button for Gender always has the default selected value.

            // Check if the description is not empty as it is mandatory to enter.
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

        // Get the FirstName from editText and trim the space
        val firstName = binding.edtFirstName.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName) {
            userHashMap[Constant.FIRST_NAME] = firstName
        }

        // Get the LastName from editText and trim the space
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

        val instaLink = binding.edtSosmedInsta.text.toString().trim { it <= ' ' }
        if (instaLink != mUserDetails.instagramLink) {
            userHashMap[Constant.INSTA_LINK] = instaLink
        }

        val ytLink = binding.edtSosmedYoutube.text.toString().trim { it <= ' ' }
        if (ytLink != mUserDetails.youtubeLink) {
            userHashMap[Constant.YT_LINK] = ytLink
        }

        val linkedinLink = binding.edtSosmedLinkedin.text.toString().trim { it <= ' ' }
        if (linkedinLink != mUserDetails.linkedinLink) {
            userHashMap[Constant.LINKEDIN_LINK] = linkedinLink
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constant.IMAGE] = mUserProfileImageURL
        }

        // Here if user is about to complete the profile then update the field or else no need.
        // 0: User profile is incomplete.
        // 1: User profile is completed.
        if (mUserDetails.profileCompleted == 0) {
            userHashMap[Constant.COMPLETE_PROFILE] = 1
        }

        // call the registerUser function of FireStore class to make an entry in the database.
        FireStoreClass().updateUserProfileData(
            this@UserProfileActivity,
            userHashMap
        )
    }

    fun userProfileUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()
    }

//    @Deprecated("Deprecated in Java")
//    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == Constant.PICK_IMAGE_REQUEST_CODE) {
//                if (data != null) {
//                    try {
//
//                        // The uri of selected image from phone storage.
//                        mSelectedImageFileUri = data.data!!
//
//                        GlideLoader(this@UserProfileActivity).loadUserPicture(
//                            mSelectedImageFileUri!!,
//                            binding.ivUserProfileUpload
//                        )
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                        Toast.makeText(
//                            this@UserProfileActivity,
//                            resources.getString(R.string.image_selection_failed),
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    }
//                }
//            }
//        } else if (resultCode == Activity.RESULT_CANCELED) {
//            // A log is printed when user close or cancel the image selection.
//            Log.e("Request Cancelled", "Image selection cancelled")
//        }
//    }

    fun getImageProfile(){
//        val launchActivityResult = registerForActivityResult<Intent , ActivityResult>(
//            ActivityResultContracts.StartActivityForResult()
//        ) { res ->
//            if (res.resultCode == RESULT_OK) {
//                // Kalo berhasil
//
//                if (res.data != null) {
//                    try {
////
//                        // The uri of selected image from phone storage.
//                        mSelectedImageFileUri = res.data!!.data!!
//
//                        GlideLoader(this@UserProfileActivity).loadUserPicture(
//                            mSelectedImageFileUri!!,
//                            binding.ivUserProfileUpload
//                        )
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                        Toast.makeText(
//                            this@UserProfileActivity,
//                            resources.getString(R.string.image_selection_failed),
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                    }
//
//                } else if (res.resultCode == RESULT_CANCELED) {
//                    Log.e("Request Cancelled", "Image selection cancelled")
//                }
//            }
//        }
//
//        val intent = Intent(this, MainActivity::class.java)
//        launchActivityResult.launch(intent)

        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            binding.ivUserProfileUpload.setImageURI(it)
        }

        binding.ivUserProfileUpload.setOnClickListener {
            getImage.launch("image/*")
        }
    }

    fun imageUploadSuccess(imageURL: String) {

        mUserProfileImageURL = imageURL

        updateUserProfileDetails()
    }
}