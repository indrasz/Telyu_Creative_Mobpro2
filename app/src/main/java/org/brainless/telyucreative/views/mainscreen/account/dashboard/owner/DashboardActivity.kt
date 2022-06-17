package org.brainless.telyucreative.views.mainscreen.account.dashboard.owner

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.data.model.User
import org.brainless.telyucreative.databinding.ActivityDashboardBinding
import org.brainless.telyucreative.utils.BaseActivity
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.utils.GlideLoader
import org.brainless.telyucreative.views.detailscreen.CreationDetailActivity

class DashboardActivity : BaseActivity() {
    private lateinit var binding : ActivityDashboardBinding
    private lateinit var userDetail : User
//    private var creationOwnerId: String = ""
    private lateinit var dashboardAdapter: DashboardAdapter
    private val arrayOfDashboard = arrayListOf<Creation>()

    private val viewModel: DashboardViewModel by lazy {
        ViewModelProvider(this)[DashboardViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constant.EXTRA_USER_DETAILS) ) {
            userDetail = intent.getParcelableExtra(Constant.EXTRA_USER_DETAILS)!!
//            creationOwnerId = intent.getStringExtra(Constant.EXTRA_CREATION_OWNER_ID)!!
            GlideLoader(this@DashboardActivity).loadUserPicture(
                userDetail.image,
                binding.ivUser
            )

            binding.apply {
                tvUsername.text = userDetail.firstName
                tvUserProfession.text = userDetail.profession
                tvDescUser.text = userDetail.description
            }
//            if (userDetail.id == creationOwnerId){
//                binding.apply {
//                    tvUsername.text = userDetail.firstName
//                    tvUserProfession.text = userDetail.profession
//                    tvDescUser.text = userDetail.description
//                }
//            } else {
//                binding.apply {
//                    tvUsername.text = userDetail.firstName
//                    tvUserProfession.text = userDetail.profession
//                    tvDescUser.text = userDetail.description
//                }
//            }
        }

        successGetDashboardList()
        observeData()

        supportActionBar?.hide()
    }

    @SuppressLint("Recycle")
    fun successGetDashboardList() {
        dashboardAdapter = DashboardAdapter(arrayOfDashboard){

            dashboardAdapter.setOnClickListener( object : DashboardAdapter.OnClickListener{
                override fun onClick(position: Int, creation: Creation) {
                    val intent = Intent(this@DashboardActivity, CreationDetailActivity::class.java)
                    intent.putExtra(Constant.EXTRA_CREATION_ID, creation.creationId)
                    intent.putExtra(Constant.EXTRA_CREATION_OWNER_ID, creation.userId)
                    startActivity(intent)
                }
            })

        }
        with(binding.rvDashboard){
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@DashboardActivity,2)
            adapter = dashboardAdapter

        }
    }
    private fun observeData(){
        viewModel.initData().observe(this){
            dashboardAdapter.setListData(it)
        }

    }
}