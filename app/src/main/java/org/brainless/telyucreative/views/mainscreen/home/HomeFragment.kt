package org.brainless.telyucreative.views.mainscreen.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.FragmentHomeBinding
import org.brainless.telyucreative.datastore.FireStoreClass
import org.brainless.telyucreative.model.Category
import org.brainless.telyucreative.model.Creation
import org.brainless.telyucreative.utils.BaseFragment
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.views.detailscreen.CreationDetailActivity
import org.brainless.telyucreative.views.mainscreen.home.adapters.OurRecommendationAdapter
import org.brainless.telyucreative.views.mainscreen.home.adapters.PopularSearchAdapter

class HomeFragment : BaseFragment() {
    private lateinit var binding :FragmentHomeBinding
    private lateinit var popularSearchAdapter: PopularSearchAdapter
    private lateinit var ourRecommendationAdapter: OurRecommendationAdapter
    private val arrayOfPopularSearch = arrayListOf<Category>()
    private var arrayOfOurRecommendation = arrayListOf<Creation>()
    private lateinit var creation : Creation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        popularSearchView()
    }

    override fun onResume() {
        super.onResume()
        getOurRecommendationItemsList()
    }

    private fun getOurRecommendationItemsList() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getCreationList(this@HomeFragment)
    }

    @SuppressLint("Recycle")
    private fun popularSearchView() {
        val imageArray = resources.obtainTypedArray(R.array.category_image_array)
        val nameArray = resources.getStringArray(R.array.category_name_array)

        for (i in nameArray.indices) arrayOfPopularSearch.add(
            Category(
                image = imageArray.getResourceId(i, 0),
                name = nameArray[i],
            )
        )
        popularSearchAdapter = PopularSearchAdapter(arrayOfPopularSearch) {
            Snackbar.make(
                binding.root,
                "${it.name}",
                Snackbar.LENGTH_SHORT,

                ).show()
        }
        with(binding.rvPopularSearch) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
            adapter = popularSearchAdapter
        }
    }

    @SuppressLint("Recycle")
    fun successOurRecommendationItemsList(ourRecommendationItemsList: ArrayList<Creation>) {
        arrayOfOurRecommendation = ourRecommendationItemsList
        hideProgressDialog()

        if (ourRecommendationItemsList.size > 0) {

            ourRecommendationAdapter = OurRecommendationAdapter(arrayOfOurRecommendation) {

                val intent = Intent(context, CreationDetailActivity::class.java)
                    intent.putExtra(Constant.EXTRA_CREATION_ID, creation.creationId)
                    intent.putExtra(Constant.EXTRA_CREATION_OWNER_ID, creation.userId)
                    startActivity(intent)
            }

            with(binding.rvRecomendation){
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
                adapter = ourRecommendationAdapter

            }
        }
    }

}