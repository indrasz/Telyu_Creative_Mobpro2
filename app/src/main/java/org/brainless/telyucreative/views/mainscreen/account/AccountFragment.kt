package org.brainless.telyucreative.views.mainscreen.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.brainless.telyucreative.databinding.FragmentAccountBinding
import org.brainless.telyucreative.views.mainscreen.account.dashboard.DashboardActivity
import org.brainless.telyucreative.views.mainscreen.account.profile.UserProfileActivity
import org.brainless.telyucreative.views.mainscreen.account.telyuaccount.TelyuAccountActivity
import org.brainless.telyucreative.views.mainscreen.account.upload.UploadCreationActivity

class AccountFragment : Fragment() {

    private lateinit var binding : FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            editProfile.setOnClickListener {
                startActivity(
                    Intent(
                        activity, UserProfileActivity::class.java
                    )
                )
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

        }
    }
}