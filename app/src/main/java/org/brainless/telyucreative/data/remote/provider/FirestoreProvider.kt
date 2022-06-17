package org.brainless.telyucreative.data.remote.provider

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.brainless.telyucreative.data.model.*
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.views.authscreen.LoginActivity
import org.brainless.telyucreative.views.authscreen.RegisterActivity
import org.brainless.telyucreative.views.detailscreen.CreationDetailActivity
import org.brainless.telyucreative.views.mainscreen.account.AccountFragment
import org.brainless.telyucreative.views.mainscreen.account.dashboard.other.DashboardOtherActivity
import org.brainless.telyucreative.views.mainscreen.account.dashboard.owner.DashboardActivity
import org.brainless.telyucreative.views.mainscreen.account.profile.UserProfileActivity
import org.brainless.telyucreative.views.mainscreen.account.upload.UploadCreationActivity
import org.brainless.telyucreative.views.mainscreen.home.HomeFragment
import org.brainless.telyucreative.views.mainscreen.save.SaveFragment
import java.io.IOException

class FirestoreProvider {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        mFireStore.collection(Constant.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getUserDetails(activity: Activity) {

        mFireStore.collection(Constant.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constant.TELYUCREATIVE_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constant.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }
//
//    fun getUser(activity: UploadCreationActivity, userId: String) {
//
//        mFireStore.collection(Constant.USERS)
//            .document(userId)
//            .get()
//            .addOnSuccessListener { document ->
//
//                Log.e(activity.javaClass.simpleName, document.toString())
//
//                val user = document.toObject(User::class.java)!!
//
//                activity.succesGetUserDetail(user)
//            }
//            .addOnFailureListener { e ->
//
//                activity.hideProgressDialog()
//
//                Log.e(activity.javaClass.simpleName, "Error while getting the creation details.", e)
//            }
//    }


    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constant.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {

                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {

        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constant.getFileExtension(
                activity,
                imageFileURI
            )
        )

        try {
            sRef.putFile(imageFileURI!!)
                .addOnSuccessListener { taskSnapshot ->
                    Log.e(
                        "Firebase Image URL",
                        taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                    )
                    taskSnapshot.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { uri ->
                            Log.e("Downloadable Image URL", uri.toString())

                            // Here call a function of base activity for transferring the result to it.
                            when (activity) {
                                is UserProfileActivity -> {
                                    activity.imageUploadSuccess(uri.toString())
                                }

                                is UploadCreationActivity -> {
                                    activity.imageUploadSuccess(uri.toString())
                                }

                            }
                        }
                }
                .addOnFailureListener { exception ->
                    when (activity) {
                        is UserProfileActivity -> {
                            activity.hideProgressDialog()
                        }

                        is UploadCreationActivity -> {
                            activity.hideProgressDialog()
                        }

                    }

                    Log.e(
                        activity.javaClass.simpleName,
                        exception.message,
                        exception
                    )
                }
        } catch (e : IOException){
            e.printStackTrace()
        }
    }

    fun uploadCreationDetails(activity: UploadCreationActivity, creationInfo: Creation) {

        try {
            mFireStore.collection(Constant.CREATION)
                .document()
                .set(creationInfo, SetOptions.merge())
                .addOnSuccessListener {
                    activity.creationUploadSuccess()
                }
                .addOnFailureListener { e ->
                    activity.hideProgressDialog()
                    Log.e(
                        activity.javaClass.simpleName,
                        "Error while uploading the product details.",
                        e
                    )
                }
        } catch (e : IOException){
            e.printStackTrace()
        }
    }

    fun getDataUserAccount(): LiveData<User?> {

        val fragment : Fragment = AccountFragment()
        val userData = MutableLiveData<User>()

        try {
            mFireStore.collection(Constant.USERS)
                .document(getCurrentUserID())
                .get()
                .addOnSuccessListener { document ->

                    Log.i(fragment.javaClass.simpleName, document.toString())

                    val user = document.toObject(User::class.java)!!

                    val sharedPreferences =
                        fragment.activity?.getSharedPreferences(
                            Constant.TELYUCREATIVE_PREFERENCES,
                            Context.MODE_PRIVATE
                        )

                    val editor = sharedPreferences?.edit()
                    editor?.putString(
                        Constant.LOGGED_IN_USERNAME,
                        user.firstName
                    )
                    editor?.apply()

                    userData.value = user

                }
                .addOnFailureListener { e ->
                    Log.e(
                        fragment.javaClass.simpleName,
                        "Error while getting user details.",
                        e
                    )
                }

        } catch (e : IOException){
            e.printStackTrace()
        }

        return userData
    }

    fun getCreationData(): LiveData<ArrayList<Creation>>{
        val fragment : Fragment = HomeFragment()
        val creationData = MutableLiveData<ArrayList<Creation>>()

        try {
            mFireStore.collection(Constant.CREATION)
                .get()
                .addOnSuccessListener{ document ->

                    Log.e(fragment.javaClass.simpleName, document.documents.toString())

                    val creationList: ArrayList<Creation> = ArrayList()

                    for (i in document.documents) {

                        val creation = i.toObject(Creation::class.java)!!
                        creation.creationId = i.id
                        creationList.add(creation)
                    }

                    creationData.value = creationList
                }

                .addOnFailureListener { e ->
                    Log.e(fragment.javaClass.simpleName, "Error while getting data user account.", e)
                }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return creationData
    }

    fun getCreationListData(): LiveData<ArrayList<Creation>>{
        val activity : Activity = DashboardActivity()
        val creationData = MutableLiveData<ArrayList<Creation>>()

        try {
            mFireStore.collection(Constant.CREATION)
                .whereEqualTo(Constant.USER_ID, getCurrentUserID())
                .get()
                .addOnSuccessListener{ document ->

                    Log.e(activity.javaClass.simpleName, document.documents.toString())
                    val creationList: ArrayList<Creation> = ArrayList()
                    for (i in document.documents) {
                        val creation = i.toObject(Creation::class.java)!!
                        creation.creationId = i.id
                        creationList.add(creation)
                    }
                    creationData.value = creationList
                }
                .addOnFailureListener { e ->
                    Log.e(activity.javaClass.simpleName, "Error while getting data user account.", e)
                }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return creationData
    }

    fun getCreationDetails(activity: CreationDetailActivity, creationId: String) {

        mFireStore.collection(Constant.CREATION)
            .document(creationId)
            .get()
            .addOnSuccessListener { document ->

                Log.e(activity.javaClass.simpleName, document.toString())

                val creation = document.toObject(Creation::class.java)!!

                activity.creationDetailSuccess(creation)
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while getting the creation details.", e)
            }
    }

    fun getDashboardUser(activity: DashboardOtherActivity, userId: String){

        mFireStore.collection(Constant.USERS)
            .document(userId)
            .get()
            .addOnSuccessListener { document ->

                Log.e(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!

                activity.dashboardDetailSuccess(user)
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while getting the creation details.", e)
            }
    }

    fun addFavoriteCreation (activity: CreationDetailActivity, favoriteCreation: Favorite) {

        mFireStore.collection(Constant.FAVORITE)
            .document()
            .set(favoriteCreation, SetOptions.merge())
            .addOnSuccessListener {
                activity.creationSuccessAddToFavorite()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }
    }

    fun checkIfCreationExistInFavorite(activity: CreationDetailActivity, creationId: String) {

        mFireStore.collection(Constant.FAVORITE)
            .whereEqualTo(Constant.USER_ID, getCurrentUserID())
            .whereEqualTo(Constant.CREATION_ID, creationId)
            .get()
            .addOnSuccessListener { document ->

                Log.e(activity.javaClass.simpleName, document.documents.toString())

                if (document.documents.size > 0) {
                    activity.creationExistsInFavorite()
                } else {
                    activity.hideProgressDialog()
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is an error.
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking the existing favorite list.",
                    e
                )
            }
    }

    fun getFavoriteList(): LiveData<ArrayList<Favorite>>{
        val fragment : Fragment = SaveFragment()
        val favoriteData = MutableLiveData<ArrayList<Favorite>>()

        try {
            mFireStore.collection(Constant.FAVORITE)
                .whereEqualTo(Constant.USER_ID, getCurrentUserID())
                .get()
                .addOnSuccessListener{ document ->

                    Log.e(fragment.javaClass.simpleName, document.documents.toString())

                    val list: ArrayList<Favorite> = ArrayList()

                    for (i in document.documents) {

                        val favorite = i.toObject(Favorite::class.java)!!
                        favorite.favoriteId = i.id

                        list.add(favorite)
                    }

                    favoriteData.value = list

                }

                .addOnFailureListener { e ->
                    Log.e(fragment.javaClass.simpleName, "Error while getting favorite list items.", e)
                }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return favoriteData
    }

    fun checkEmptyFavorite(fragment: SaveFragment) {

        mFireStore.collection(Constant.FAVORITE)
            .whereEqualTo(Constant.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                if (document.documents.size > 0) {
                    fragment.ifFavoriteListIsEmpty()
                } else {
                    fragment.hideProgressDialog()
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is an error.
                fragment.hideProgressDialog()

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while checking the existing favorite list.",
                    e
                )
            }
    }

    fun removeItemFromFavorite(fragment: Fragment, favoriteId: String) {

        // Cart items collection name
        mFireStore.collection(Constant.FAVORITE)
            .document(favoriteId) // cart id
            .delete()
            .addOnSuccessListener {

                // Notify the success result of the removed cart item from the list to the base class.
                when (fragment) {
                    is SaveFragment -> {
                        fragment.itemRemovedSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is any error.
                when (fragment) {
                    is SaveFragment -> {
                        fragment.hideProgressDialog()
                    }
                }
                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while removing the item from the cart list.",
                    e
                )
            }
    }

}