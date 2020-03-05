package ie.wit.jk_cafe.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class OrderModel(
    var id: String? = "",
    var quantity: String = "",
    var coffeeCup: String = "N/A",
    var where: String = "N/A",
    var collectTime: String = " ",
    var total: String = "",
    var email: String? = "jay@cafe.com"): Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?>
    {
        return mapOf(
            "uid" to id,
            "quantity" to quantity,
            "coffeeCup" to coffeeCup,
            "where" to where,
            "collectTime" to collectTime,
            "total" to total,
            "email" to email
        )
    }
}

