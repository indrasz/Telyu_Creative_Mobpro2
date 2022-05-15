package org.brainless.telyucreative.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val profession : String = "",
    val description : String = "",
    val instagramLink : String = "",
    val youtubeLink : String = "",
    val linkedinLink : String = "",
    val profileCompleted: Int = 0
) : Parcelable
