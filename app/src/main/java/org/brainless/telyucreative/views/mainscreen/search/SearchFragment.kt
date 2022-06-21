package org.brainless.telyucreative.views.mainscreen.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.databinding.FragmentSearchBinding
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.views.detailscreen.CreationDetailActivity

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

        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchData()
            }
            true
        }
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

    fun searchData(){
        val dataCreation = binding.edtSearch.text.toString().trim{ it <= ' '}

        searchDataCreation(dataCreation)
    }

    fun searchDataCreation(search : String){
        viewModel.initDataSearch(search).observe(viewLifecycleOwner) {
            searchAdapter.setListData(it)
        }
    }


}