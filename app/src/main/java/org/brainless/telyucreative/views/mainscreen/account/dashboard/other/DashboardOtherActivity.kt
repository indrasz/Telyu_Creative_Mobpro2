package org.brainless.telyucreative.views.mainscreen.account.dashboard.other

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.data.model.User
import org.brainless.telyucreative.data.remote.provider.FirestoreProvider
import org.brainless.telyucreative.databinding.ActivityDashboardOtherBinding
import org.brainless.telyucreative.utils.BaseActivity
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.utils.GlideLoader
import org.brainless.telyucreative.views.mainscreen.account.dashboard.owner.DashboardActivity

class DashboardOtherActivity : BaseActivity() {

    private lateinit var binding : ActivityDashboardOtherBinding

    private lateinit var userDetail: User
    private var userId: String = ""
    private var creationOwnerId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardOtherBinding.inflate(layoutInflater)
        setContentView(binding.root)



        if (intent.hasExtra(Constant.USERS)) {
            userId = intent.getStringExtra(Constant.USERS)!!
            getDashboardUserDetail()
        }

        supportActionBar?.hide()
    }

    fun dashboardDetailSuccess(user: User) {

        hideProgressDialog()

        userDetail = user

        GlideLoader(this@DashboardOtherActivity).loadCreationPicture(
            user.image,
            binding.ivUser
        )

        binding.apply {
            tvUsername.text = user.firstName
            tvUserProfession.text = user.profession
            tvDescUser.text = user.description

        }

//        FirestoreProvider().checkIfCreationExistInFavorite(this, mCreationId)

    }

    private fun getDashboardUserDetail() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreProvider().getDashboardUser(this@DashboardOtherActivity, userId)
    }
}