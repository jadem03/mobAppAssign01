package ie.wit.jk_cafe.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class OrderModel(
    var uid: String? = "",
    var quantity: Int = 0,
    var editText: String = "",
    var coffeeCup: String = "N/A",
    var where: String = " ",
    var collectTime: String = " ",
    var total: String = "",
    var email: String? = "jay@cafe.com",
    var profilePic: String = ""): Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?>
    {
        return mapOf(
            "uid" to uid,
            "quantity" to quantity,
            "editText" to editText,
            "coffeeCup" to coffeeCup,
            "where" to where,
            "collectTime" to collectTime,
            "total" to total,
            "email" to email,
            "profilePic" to profilePic
        )
    }
}

