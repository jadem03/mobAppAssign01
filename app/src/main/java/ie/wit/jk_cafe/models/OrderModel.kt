package ie.wit.jk_cafe.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import java.security.Timestamp
import java.sql.Time
import java.time.LocalDateTime

@IgnoreExtraProperties
@Parcelize
data class OrderModel(
    var uid: String? = "",
    var coffee: String = "",
    var quantity: Int = 0,
    var editText: String = "",
    var coffeeCup: String = "N/A",
    var where: String = " ",
    var request: String = "",
    var collectTime: String = " ",
    var total: String = ""): Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?>
    {
        return mapOf(
            "uid" to uid,
            "coffee" to coffee,
            "quantity" to quantity,
            "editText" to editText,
            "coffeeCup" to coffeeCup,
            "where" to where,
            "requests" to request,
            "collectTime" to collectTime,
            "total" to total
        )
    }
}

