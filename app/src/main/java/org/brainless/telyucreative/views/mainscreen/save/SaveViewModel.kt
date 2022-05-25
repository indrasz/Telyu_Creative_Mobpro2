package org.brainless.telyucreative.views.mainscreen.save

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.brainless.telyucreative.data.model.Favorite
import org.brainless.telyucreative.data.remote.FirestoreProvider

class SaveViewModel : ViewModel(){

    private val fireStore = FirestoreProvider()

    fun initData() : LiveData<ArrayList<Favorite>> {
        val mutableData = MutableLiveData<ArrayList<Favorite>>()

        fireStore.getFavoriteList().observeForever{ favoriteList ->
            mutableData.value = favoriteList
        }

        return mutableData
    }
}