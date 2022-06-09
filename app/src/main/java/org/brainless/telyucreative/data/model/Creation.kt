package org.brainless.telyucreative.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Creation(
    val userId: String = "",
    val userName: String = "",
    val userImage: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val urlLink: String? = null,
    val image: String = "",
    var creationId: String = "",
) : Parcelable
