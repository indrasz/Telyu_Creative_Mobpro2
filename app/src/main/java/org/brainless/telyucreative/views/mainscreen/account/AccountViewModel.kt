package org.brainless.telyucreative.views.mainscreen.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.brainless.telyucreative.data.FirestoreProvider
import org.brainless.telyucreative.model.User

class AccountViewModel : ViewModel() {

    private val fireStore = FirestoreProvider()

    fun initData() : LiveData<User> {
        val mutableData = MutableLiveData<User>()

        fireStore.getDataUserAccount().observeForever{ user ->
            mutableData.value = user
        }

        return mutableData
    }
}