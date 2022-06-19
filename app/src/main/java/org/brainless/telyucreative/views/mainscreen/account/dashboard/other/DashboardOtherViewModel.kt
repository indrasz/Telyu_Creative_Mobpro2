package org.brainless.telyucreative.views.mainscreen.account.dashboard.other

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.data.remote.provider.FirestoreProvider

class DashboardOtherViewModel : ViewModel() {

    private val fireStore = FirestoreProvider()

    fun initData(creationOwnerId : String): LiveData<ArrayList<Creation>> {
        val mutableData = MutableLiveData<ArrayList<Creation>>()

        fireStore.getDashboardList(creationOwnerId).observeForever { creationList ->
            mutableData.value = creationList
        }

        return mutableData
    }


}