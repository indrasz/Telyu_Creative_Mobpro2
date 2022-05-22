package org.brainless.telyucreative.views.mainscreen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.FireStoreClass
import org.brainless.telyucreative.model.Category
import org.brainless.telyucreative.model.Creation

class HomeViewModel : ViewModel() {
    private val fireStore = FireStoreClass()

    private val categoryData = MutableLiveData<ArrayList<Category>>()
//    private val arrayOfPopularSearch = arrayListOf<Category>()

    fun initData() : LiveData<ArrayList<Creation>>{
        val mutableData = MutableLiveData<ArrayList<Creation>>()

        fireStore.getCreationData().observeForever{ creationList ->
            mutableData.value = creationList
        }

        return mutableData
    }

    init {
        categoryData.value = initCategoryData()
    }

    private fun initCategoryData(): ArrayList<Category> {
        return arrayListOf(
            Category(R.drawable.img_popular1, "UI Design"),
            Category(R.drawable.img_popular2, "Mainan"),
            Category(R.drawable.img_popular4, "Backsound")
        )
    }

    fun getCategoryData(): LiveData<ArrayList<Category>> = categoryData
}