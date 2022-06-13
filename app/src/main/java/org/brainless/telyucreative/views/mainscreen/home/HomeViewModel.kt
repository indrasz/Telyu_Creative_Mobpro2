package org.brainless.telyucreative.views.mainscreen.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.remote.provider.FirestoreProvider
import org.brainless.telyucreative.data.model.Category
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.data.remote.network.ApiStatus
import org.brainless.telyucreative.data.remote.network.CategoryApi

class HomeViewModel : ViewModel() {

    private val fireStore = FirestoreProvider()
    private val categoryData = MutableLiveData<List<Category>>()
    private val status = MutableLiveData<ApiStatus>()

    init {
        retrieveData()
    }

    fun initData() : LiveData<ArrayList<Creation>>{
        val mutableData = MutableLiveData<ArrayList<Creation>>()

        fireStore.getCreationData().observeForever{ creationList ->
            mutableData.value = creationList
        }

        return mutableData
    }

    private fun retrieveData() {
        viewModelScope.launch (Dispatchers.IO) {
            status.postValue(ApiStatus.LOADING)
            try {
                categoryData.postValue(CategoryApi.service.getCategory())
                status.postValue(ApiStatus.SUCCESS)
            } catch (e: Exception) {
                Log.d("HomeViewModel", "Failure: ${e.message}")
                status.postValue(ApiStatus.FAILED)
            }
        }
    }

    fun getCategoryData(): LiveData<List<Category>> = categoryData
    fun getStatus(): LiveData<ApiStatus> = status

//    init {
//        categoryData.value = initCategoryData()
//    }
//
//    fun initData() : LiveData<ArrayList<Creation>>{
//        val mutableData = MutableLiveData<ArrayList<Creation>>()
//
//        fireStore.getCreationData().observeForever{ creationList ->
//            mutableData.value = creationList
//        }
//
//        return mutableData
//    }
//
//    private fun initCategoryData(): ArrayList<Category> {
//        return arrayListOf(
//            Category(R.drawable.img_popular1, "UI Design"),
//            Category(R.drawable.img_popular2, "Mainan"),
//            Category(R.drawable.img_popular4, "Backsound")
//        )
//    }

//    fun getCategoryData(): LiveData<ArrayList<Category>> = categoryData

}