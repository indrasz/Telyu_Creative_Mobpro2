package org.brainless.telyucreative.views.mainscreen.account.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.data.remote.provider.FirestoreProvider

class DashboardViewModel : ViewModel() {

    private val fireStore = FirestoreProvider()

    fun initData(): LiveData<ArrayList<Creation>> {
        val mutableData = MutableLiveData<ArrayList<Creation>>()

        fireStore.getCreationListData().observeForever { creationList ->
            mutableData.value = creationList
        }

        return mutableData
    }


}