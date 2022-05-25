package org.brainless.telyucreative.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher


object Constant{
    const val SPLASH_SCREEN_TAG = "splash_screen"
    const val USERS: String = "users"
    const val CREATION: String = "creation"
    const val FAVORITE: String = "favorite"
    const val FAVORITE_ITEM: String = "favorite_item"

    const val TELYUCREATIVE_PREFERENCES: String = "TelyuCreativePrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"

    // Intent extra constants.
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val EXTRA_CREATION_ID: String = "extra_creation_id"
    const val EXTRA_CREATION_OWNER_ID: String = "extra_creation_owner_id"
    const val ERROR_GET_DATA: String = "error_get_data"

    const val EXTRA_ADDRESS_DETAILS: String = "AddressDetails"
    const val EXTRA_SELECT_ADDRESS: String = "extra_select_address"
    const val EXTRA_SELECTED_ADDRESS: String = "extra_selected_address"
    const val EXTRA_MY_ORDER_DETAILS: String = "extra_my_order_details"
    const val EXTRA_SOLD_PRODUCT_DETAILS: String = "extra_sold_product_details"

    //A unique code for asking the Read Storage Permission using this we will be check and identify in the method onRequestPermissionsResult in the Base Activity.
    const val READ_STORAGE_PERMISSION_CODE = 2

    // A unique code of image selection from Phone Storage.
    const val PICK_IMAGE_REQUEST_CODE = 2
    const val ADD_ADDRESS_REQUEST_CODE: Int = 121

    // Constant variables for Gender
    const val MALE: String = "Male"
    const val FEMALE: String = "Female"

    const val DEFAULT_CART_QUANTITY: String = "1"

    // Firebase database field names
    const val PROFESSION: String = "profession"
    const val DESCRIPTION: String = "description"
    const val INSTAGRAM_LINK: String = "instagramLink"
    const val YOUTUBE_LINK: String = "youtubeLink"
    const val LINKEDIN_LINK: String = "linkedinLink"
    const val IMAGE: String = "image"
    const val COMPLETE_PROFILE: String = "profileCompleted"
    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"
    const val USER_ID: String = "userId"
    const val CREATION_ID: String = "creationId"

    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val CREATION_IMAGE: String = "Creation_Image"

    const val CART_QUANTITY: String = "cart_quantity"

    const val STOCK_QUANTITY: String = "stock_quantity"

    const val HOME: String = "Home"
    const val OFFICE: String = "Office"
    const val OTHER: String = "Other"

    fun showImageChooser(activity: ActivityResultLauncher<Intent>) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        activity.launch(galleryIntent)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {

        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}
