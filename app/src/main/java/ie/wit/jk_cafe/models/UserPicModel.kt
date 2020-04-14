package ie.wit.jk_cafe.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class UserPicModel(var uid: String? = "",
                          var profilePic: String = "") : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "profilePic" to profilePic
        )
    }
}