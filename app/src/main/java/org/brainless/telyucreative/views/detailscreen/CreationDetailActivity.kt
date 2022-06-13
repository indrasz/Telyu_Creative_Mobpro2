package org.brainless.telyucreative.views.detailscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.drawable.DrawableUtils
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.remote.FirestoreProvider
import org.brainless.telyucreative.databinding.ActivityCreationDetailBinding
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.data.model.Favorite
import org.brainless.telyucreative.utils.BaseActivity
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.utils.GlideLoader
import org.brainless.telyucreative.views.mainscreen.account.dashboard.DashboardActivity

class CreationDetailActivity : BaseActivity() {

    private lateinit var mCreationDetail: Creation
    private var mCreationId: String = ""
    private var creationOwnerId: String = ""
    private lateinit var binding : ActivityCreationDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivFavoriteCheck.visibility = View.GONE

        if (intent.hasExtra(Constant.EXTRA_CREATION_ID)
            && intent.hasExtra(Constant.EXTRA_CREATION_OWNER_ID)
        ) {
            mCreationId = intent.getStringExtra(Constant.EXTRA_CREATION_ID)!!
            creationOwnerId = intent.getStringExtra(Constant.EXTRA_CREATION_OWNER_ID)!!
            getCreationDetails()
            binding.ivFavorite.setOnClickListener {
                addToFavorite()
            }
        }

    }

    fun creationDetailSuccess(creation: Creation) {

        mCreationDetail = creation

        // Populate the creation details in the UI.
        GlideLoader(this@CreationDetailActivity).loadCreationPicture(
            creation.image,
            binding.ivCreation
        )

        GlideLoader(this@CreationDetailActivity).loadUserPicture(
            creation.userImage,
            binding.ivUser
        )


        binding.apply {
            tvCreationName.text = creation.title
            tvCreationDesc.text = creation.description
            tvUsername.text = creation.userName

            btnSeeCreator.setOnClickListener {
                val intent = Intent(this@CreationDetailActivity, DashboardActivity::class.java)
                intent.putExtra(Constant.EXTRA_CREATION_OWNER_ID, creationOwnerId)
                startActivity(intent)
            }
        }

        FirestoreProvider().checkIfCreationExistInFavorite(this, mCreationId)

    }

    private fun getCreationDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreProvider().getCreationDetails(this@CreationDetailActivity, mCreationId)
    }

    private fun addToFavorite(){
        val favorite = Favorite(
            FirestoreProvider().getCurrentUserID(),
            creationOwnerId,
            mCreationId,
            mCreationDetail.title,
            mCreationDetail.category,
            mCreationDetail.image,
        )

        FirestoreProvider().addFavoriteCreation(
            this@CreationDetailActivity,
            favorite
        )
    }

    fun creationExistsInFavorite() {
        hideProgressDialog()

        binding.ivFavorite.visibility = View.GONE
        binding.ivFavoriteCheck.visibility = View.VISIBLE

    }

    fun creationSuccessAddToFavorite() {

        hideProgressDialog()

        Toast.makeText(
            this@CreationDetailActivity,
            resources.getString(R.string.creation_add_favorite_success_message),
            Toast.LENGTH_SHORT
        ).show()

        binding.ivFavorite.visibility = View.GONE
        binding.ivFavoriteCheck.visibility = View.VISIBLE

    }
}

