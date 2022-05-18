package org.brainless.telyucreative.views.mainscreen.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.brainless.telyucreative.R
import org.brainless.telyucreative.databinding.FragmentHomeBinding
import org.brainless.telyucreative.model.Category
import org.brainless.telyucreative.views.mainscreen.home.adapters.PopularSearchAdapter

class HomeFragment : Fragment() {
    private lateinit var binding :FragmentHomeBinding
    private lateinit var popularSearchAdapter: PopularSearchAdapter
    private val arrayOfPopularSearch = arrayListOf<Category>()

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

}