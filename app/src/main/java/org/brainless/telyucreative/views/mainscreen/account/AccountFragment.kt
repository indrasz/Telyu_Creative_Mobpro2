package org.brainless.telyucreative.views.mainscreen.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import org.brainless.telyucreative.databinding.FragmentAccountBinding
import org.brainless.telyucreative.datastore.FireStoreClass
import org.brainless.telyucreative.model.User
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.utils.GlideLoader
import org.brainless.telyucreative.views.authscreen.LoginActivity
import org.brainless.telyucreative.views.mainscreen.MainActivity
import org.brainless.telyucreative.views.mainscreen.account.dashboard.DashboardActivity
import org.brainless.telyucreative.views.mainscreen.account.profile.UserProfileActivity
import org.brainless.telyucreative.views.mainscreen.account.telyuaccount.TelyuAccountActivity
import org.brainless.telyucreative.views.mainscreen.account.upload.UploadCreationActivity

class AccountFragment : Fragment(){

    private lateinit var binding : FragmentAccountBinding
    private lateinit var mUserDetails: User
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        binding.apply {
            editProfile.setOnClickListener {
                val intent = Intent(activity, UserProfileActivity::class.java)
                intent.putExtra(Constant.EXTRA_USER_DETAILS, mUserDetails)
                startActivity(intent)
            }

            seeWork.setOnClickListener{
                startActivity(
                    Intent(
                        activity, TelyuAccountActivity::class.java
                    )
                )
            }
            uploadCreation.setOnClickListener {
                startActivity(
                    Intent(
                        activity, UploadCreationActivity::class.java
                    )
                )
            }
            dashboard.setOnClickListener {
                startActivity(
                    Intent(
                        activity, DashboardActivity::class.java
                    )
                )
            }

            tvLogout.setOnClickListener{
                auth.signOut()
                Intent(activity, LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }

        }
    }

    private fun getUserDetails() {
        FireStoreClass().getUserAccount(this@AccountFragment)
    }

    fun userDetailsSuccess(user: User) {

        mUserDetails = user

        activity?.let { GlideLoader(it).loadUserPicture(user.image, binding.ivUser) }

        binding.tvUsername.text = user.lastName
    }

}