package org.brainless.telyucreative.views.mainscreen.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.data.model.Favorite
import org.brainless.telyucreative.data.remote.FirestoreProvider
import org.brainless.telyucreative.databinding.FragmentSaveBinding
import org.brainless.telyucreative.databinding.FragmentSearchBinding
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.views.detailscreen.CreationDetailActivity
import org.brainless.telyucreative.views.mainscreen.save.SaveAdapter
import org.brainless.telyucreative.views.mainscreen.save.SaveViewModel

class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private var arrayOfSearch = arrayListOf<Creation>()

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        successSearchList()
        observeData()
    }

    @SuppressLint("Recycle")
    fun successSearchList() {
        searchAdapter = SearchAdapter(arrayOfSearch){

            searchAdapter.setOnClickListener( object : SearchAdapter.OnClickListener{
                override fun onClick(position: Int, creation: Creation) {
                    val intent = Intent(requireContext(), CreationDetailActivity::class.java)
                    intent.putExtra(Constant.EXTRA_CREATION_ID, creation.creationId)
                    intent.putExtra(Constant.EXTRA_CREATION_OWNER_ID, creation.userId)
                    startActivity(intent)
                }
            })

        }
        with(binding.rvSearch){
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(),2)
            adapter = searchAdapter
        }

    }

    private fun observeData(){
        viewModel.initData().observe(viewLifecycleOwner) {
            searchAdapter.setListData(it)
        }

    }


}