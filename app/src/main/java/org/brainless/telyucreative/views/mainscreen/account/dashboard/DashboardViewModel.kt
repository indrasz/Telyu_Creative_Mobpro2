package org.brainless.telyucreative.views.mainscreen.account.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import org.brainless.telyucreative.R
import org.brainless.telyucreative.data.model.Category
import org.brainless.telyucreative.data.model.Creation
import org.brainless.telyucreative.data.remote.FirestoreProvider
import org.brainless.telyucreative.utils.Constant
import org.brainless.telyucreative.views.detailscreen.CreationDetailActivity
import org.brainless.telyucreative.views.mainscreen.home.adapters.OurRecommendationAdapter

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