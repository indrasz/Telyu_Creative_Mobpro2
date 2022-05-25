package org.brainless.telyucreative.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Favorite(
    val userId : String = "",
    val userOwnerId : String = "",
    val creationId : String = "",
    val title: String = "",
    val category: String = "",
    val image: String = "",
    var favoriteId: String = "",
) : Parcelable