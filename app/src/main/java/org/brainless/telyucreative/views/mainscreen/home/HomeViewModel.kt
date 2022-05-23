package org.brainless.telyucreative.views.mainscreen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.remote.FirestoreProvider
import org.brainless.telyucreative.data.model.Category
import org.brainless.telyucreative.data.model.Creation

class HomeViewModel : ViewModel() {

    private val fireStore = FirestoreProvider()

    private val categoryData = MutableLiveData<ArrayList<Category>>()

    init {
        categoryData.value = initCategoryData()
    }

    fun initData() : LiveData<ArrayList<Creation>>{
        val mutableData = MutableLiveData<ArrayList<Creation>>()

        fireStore.getCreationData().observeForever{ creationList ->
            mutableData.value = creationList
        }

        return mutableData
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