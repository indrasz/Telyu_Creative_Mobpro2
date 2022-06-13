package org.brainless.telyucreative.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
//    var image: Int = 0,
//    var name: String? = "",
    val nama : String,
    val imageId: String,
) : Parcelable