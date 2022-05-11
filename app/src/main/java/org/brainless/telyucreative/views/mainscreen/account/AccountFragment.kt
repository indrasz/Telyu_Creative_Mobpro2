package org.brainless.telyucreative.views.mainscreen.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.brainless.telyucreative.databinding.FragmentAccountBinding
import org.brainless.telyucreative.views.mainscreen.account.dashboard.DashboardActivity
import org.brainless.telyucreative.views.mainscreen.account.profile.EditProfileActivity
import org.brainless.telyucreative.views.mainscreen.account.telyuaccount.TelyuAccountActivity
import org.brainless.telyucreative.views.mainscreen.account.upload.UploadKaryaActivity

class AccountFragment : Fragment() {

    private lateinit var binding : FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            editProfile.setOnClickListener {
                startActivity(
                    Intent(
                        activity, EditProfileActivity::class.java
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
            uploadWork.setOnClickListener {
                startActivity(
                    Intent(
                        activity, UploadKaryaActivity::class.java
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