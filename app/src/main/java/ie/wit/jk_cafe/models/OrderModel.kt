package ie.wit.jk_cafe.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderModel(var id: Long = 0,
                      var coffeeCup: String = " ",
                      var where: String = " ",
                      var collectTime: String = " ",
                      var total: String = ""): Parcelable

