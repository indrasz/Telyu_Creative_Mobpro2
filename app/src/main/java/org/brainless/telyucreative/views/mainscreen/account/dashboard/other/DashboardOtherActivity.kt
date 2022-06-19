package org.brainless.telyucreative.views.mainscreen.account.dashboard.other

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.data.model.User
import org.brainless.telyucreative.data.remote.provider.FirestoreProvider
import org.brainless.telyucreative.databinding.ActivityDashboardOtherBinding
import org.brainless.telyucreative.utils.BaseActivity
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.utils.GlideLoader
import org.brainless.telyucreative.views.detailscreen.CreationDetailActivity
import org.brainless.telyucreative.views.mainscreen.account.dashboard.owner.DashboardActivity
import org.brainless.telyucreative.views.mainscreen.account.dashboard.owner.DashboardAdapter
import org.brainless.telyucreative.views.mainscreen.account.dashboard.owner.DashboardViewModel

class DashboardOtherActivity : BaseActivity() {

    private lateinit var binding : ActivityDashboardOtherBinding

    private lateinit var userDetail: User
    private var userId: String = ""
    private lateinit var dashboardOtherAdapter: DashboardOtherAdapter
    private val arrayOfDashboardOther = arrayListOf<Creation>()
    private var creationOwnerId: String = ""

    private val viewModel: DashboardOtherViewModel by lazy {
        ViewModelProvider(this)[DashboardOtherViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardOtherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constant.USERS)) {
            userId = intent.getStringExtra(Constant.USERS)!!
            creationOwnerId = intent.getStringExtra(Constant.USERS)!!
            getDashboardUserDetail()
        }

        successGetDashboardList()
        observeData(creationOwnerId)

        supportActionBar?.hide()
    }

    @SuppressLint("Recycle")
    fun successGetDashboardList() {
        dashboardOtherAdapter = DashboardOtherAdapter(arrayOfDashboardOther){

            dashboardOtherAdapter.setOnClickListener( object : DashboardOtherAdapter.OnClickListener{
                override fun onClick(position: Int, creation: Creation) {
                    val intent = Intent(this@DashboardOtherActivity, CreationDetailActivity::class.java)
                    intent.putExtra(Constant.EXTRA_CREATION_ID, creation.creationId)
                    intent.putExtra(Constant.EXTRA_CREATION_OWNER_ID, creation.userId)
                    startActivity(intent)
                }
            })

        }
        with(binding.rvDashboard){
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@DashboardOtherActivity,2)
            adapter = dashboardOtherAdapter

        }
    }
    private fun observeData(creationOwner : String){

        viewModel.initData(creationOwner).observe(this){
            dashboardOtherAdapter.setListData(it)
        }

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