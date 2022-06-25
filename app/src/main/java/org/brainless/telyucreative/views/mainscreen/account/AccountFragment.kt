package org.brainless.telyucreative.views.mainscreen.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import org.brainless.telyucreative.databinding.FragmentAccountBinding
import org.brainless.telyucreative.data.model.User
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.utils.GlideLoader
import org.brainless.telyucreative.views.authscreen.LoginActivity
import org.brainless.telyucreative.views.mainscreen.account.dashboard.owner.DashboardActivity
import org.brainless.telyucreative.views.mainscreen.account.profile.UserProfileActivity
import org.brainless.telyucreative.views.mainscreen.account.telyuaccount.TelyuAccountActivity
import org.brainless.telyucreative.views.mainscreen.account.upload.UploadCreationActivity

class AccountFragment : Fragment(){

    private lateinit var binding : FragmentAccountBinding
    private lateinit var mUserDetails: User
    private lateinit var auth: FirebaseAuth

    private val viewModel: AccountViewModel by lazy {
        ViewModelProvider(this)[AccountViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        getUserDetails()

        auth = FirebaseAuth.getInstance()

        binding.apply {
            editProfile.setOnClickListener {
                val intent = Intent(activity, UserProfileActivity::class.java)
                intent.putExtra(Constant.EXTRA_USER_DETAILS, mUserDetails)
                startActivity(intent)
            }

            seeWork.setOnClickListener{
                Snackbar.make(
                    root,
                    "Fitur ini belum tersedia",
                    Snackbar.LENGTH_SHORT,
                    ).show()
            }
            uploadCreation.setOnClickListener {
                val intent = Intent(activity, UploadCreationActivity::class.java)
                intent.putExtra(Constant.EXTRA_USER_DETAILS, mUserDetails)
                startActivity(intent)
            }
            dashboard.setOnClickListener {
                val intent = Intent(activity, DashboardActivity::class.java)
                intent.putExtra(Constant.EXTRA_USER_DETAILS, mUserDetails)
                startActivity(intent)
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

    private fun getUserDetails() = viewModel.initData().observe(requireActivity()) {
        userDetailsSuccess(it)
    }

    private fun userDetailsSuccess(user: User?) {

        if (user == null) return

        user.also { mUserDetails = it }
        activity?.let { GlideLoader(it).loadUserPicture(
            image = user.image,
            imageView = binding.ivUser
        ) }
        binding.tvUsername.text = user.firstName
    }

}