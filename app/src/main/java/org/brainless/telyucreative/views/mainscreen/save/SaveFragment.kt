package org.brainless.telyucreative.views.mainscreen.save

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.model.Category
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.data.model.Favorite
import org.brainless.telyucreative.databinding.FragmentSaveBinding
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.views.detailscreen.CreationDetailActivity
import org.brainless.telyucreative.views.mainscreen.home.HomeViewModel
import org.brainless.telyucreative.views.mainscreen.home.adapters.OurRecommendationAdapter
import org.brainless.telyucreative.views.mainscreen.home.adapters.PopularSearchAdapter

class SaveFragment : Fragment() {

    private lateinit var binding : FragmentSaveBinding
    private lateinit var saveAdapter: SaveAdapter
    private var arrayOfSave = arrayListOf<Favorite>()

    private val viewModel: SaveViewModel by lazy {
        ViewModelProvider(this)[SaveViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        successFavoriteList()
        observeData()
    }

    @SuppressLint("Recycle")
    fun successFavoriteList() {
        saveAdapter = SaveAdapter(arrayOfSave){

            saveAdapter.setOnClickListener( object : SaveAdapter.OnClickListener{
                override fun onClick(position: Int, favorite: Favorite) {
                    val intent = Intent(requireContext(), CreationDetailActivity::class.java)
                    intent.putExtra(Constant.EXTRA_CREATION_ID, favorite.creationId)
                    intent.putExtra(Constant.EXTRA_CREATION_OWNER_ID, favorite.userId)
                    startActivity(intent)
                }
            })

        }
        with(binding.rvFavoriteList){
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = saveAdapter

        }
    }

    private fun observeData(){
        viewModel.initData().observe(viewLifecycleOwner) {
            saveAdapter.setListData(it)
        }

    }



}