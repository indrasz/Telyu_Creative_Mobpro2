package org.brainless.telyucreative.views.mainscreen.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.brainless.telyucreative.databinding.FragmentHomeBinding
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

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

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
        successOurRecommendationItemsList()

        //viewmodel
        observeData()

    }

    @SuppressLint("Recycle")
    private fun popularSearchView() {

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
    fun successOurRecommendationItemsList() {
        ourRecommendationAdapter = OurRecommendationAdapter(arrayOfOurRecommendation){

            ourRecommendationAdapter.setOnClickListener( object : OurRecommendationAdapter.OnClickListener{
                override fun onClick(position: Int, creation: Creation) {
                    val intent = Intent(requireContext(), CreationDetailActivity::class.java)
                    intent.putExtra(Constant.EXTRA_CREATION_ID, creation.creationId)
                    intent.putExtra(Constant.EXTRA_CREATION_OWNER_ID, creation.userId)
                    startActivity(intent)
                }
            })

        }
        with(binding.rvRecomendation){
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
            adapter = ourRecommendationAdapter

        }
    }

    private fun observeData(){
        viewModel.initData().observe(viewLifecycleOwner) {
            ourRecommendationAdapter.setListData(it)
        }

        viewModel.getCategoryData().observe(viewLifecycleOwner) {
            popularSearchAdapter.setListDataCategory(it)
        }
    }
}