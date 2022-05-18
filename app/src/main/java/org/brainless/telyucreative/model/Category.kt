package org.brainless.telyucreative.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var image: Int = 0,
    var name: String? = "",
) : Parcelable