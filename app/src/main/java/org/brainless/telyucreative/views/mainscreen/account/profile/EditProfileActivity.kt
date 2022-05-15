package org.brainless.telyucreative.views.mainscreen.account.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.ActivityEditProfileBinding
import org.brainless.telyucreative.model.User
import org.brainless.telyucreative.datastore.FireStoreClass
import org.brainless.telyucreative.utils.BaseActivity
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.utils.GlideLoader
import org.brainless.telyucreative.views.mainscreen.account.AccountFragment

class EditProfileActivity : BaseActivity(), View.OnClickListener{

    private lateinit var binding : ActivityEditProfileBinding
    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
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

            // Load the image using the GlideLoader class with the use of Glide Library.

                binding.apply{

                    GlideLoader(this@EditProfileActivity).loadUserPicture(mUserDetails.image, ivUserProfileUpload)

                    // Set the existing values to the UI and allow user to edit except the Email ID.
                    edtFirstName.setText(mUserDetails.firstName)
                    edtLastName.setText(mUserDetails.lastName)

                    edtEmail.isEnabled = false
                    edtEmail.setText(mUserDetails.email)


                }

       }


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
                        Constant.showImageChooser(this@EditProfileActivity)
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
                    showProgressDialog(resources.getString(R.string.please_wait))

                    if (mSelectedImageFileUri != null) {

                        FireStoreClass().uploadImageToCloudStorage(
                            this@EditProfileActivity,
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
            this@EditProfileActivity,
            userHashMap
        )
    }

    fun userProfileUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@EditProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()

        val mFragmentManager = supportFragmentManager
        val mFragmentTransaction = mFragmentManager.beginTransaction()
        val mFragment = AccountFragment()

        // Redirect to the Main Screen after profile completion.
        mFragmentTransaction.replace(R.id.edit_profile_activity, mFragment)
        finish()
    }


    fun imageUploadSuccess(imageURL: String) {

        mUserProfileImageURL = imageURL

        updateUserProfileDetails()
    }
}