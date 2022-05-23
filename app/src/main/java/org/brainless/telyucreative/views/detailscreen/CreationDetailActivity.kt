package org.brainless.telyucreative.views.detailscreen

import android.os.Bundle
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.remote.FirestoreProvider
import org.brainless.telyucreative.databinding.ActivityCreationDetailBinding
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.utils.BaseActivity
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.utils.GlideLoader

class CreationDetailActivity : BaseActivity() {

    private lateinit var mCreationDetail: Creation

    // A global variable for product id.
    private var mCreationId: String = ""
    private lateinit var binding : ActivityCreationDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constant.EXTRA_CREATION_ID)) {
            mCreationId =
                intent.getStringExtra(Constant.EXTRA_CREATION_ID)!!
        }

        var creationOwnerId: String = ""

        if (intent.hasExtra(Constant.EXTRA_CREATION_OWNER_ID)) {
            creationOwnerId =
                intent.getStringExtra(Constant.EXTRA_CREATION_OWNER_ID)!!
        }

        getCreationDetails()

    }

    fun creationDetailSuccess(creation: Creation) {

        mCreationDetail = creation

        // Populate the creation details in the UI.
        GlideLoader(this@CreationDetailActivity).loadCreationPicture(
            creation.image,
            binding.ivCreation
        )

        binding.apply {
            tvCreationName.text = creation.title
            tvCreationDesc.text = creation.description
        }

        if (FirestoreProvider().getCurrentUserID() == creation.userId) {
            hideProgressDialog()
        }
    }

    private fun getCreationDetails() {

        // Show the product dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the product details.
        FirestoreProvider().getCreationDetails(this@CreationDetailActivity, mCreationId)
    }
}