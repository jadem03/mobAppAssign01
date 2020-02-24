package ie.wit.jk_cafe.models

import android.util.Log

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class OrderMemStore : OrderStore {

        val orders = ArrayList<OrderModel>()

        override fun findAll(): List<OrderModel> {
            return orders
        }

        override fun findById(id:Long) : OrderModel? {
            val foundOrder: OrderModel? = orders.find { it.id == id }
            return foundOrder
        }

        override fun create(order: OrderModel) {
            order.id = getId()
            orders.add(order)
            logAll()
        }

        fun logAll() {
            Log.v("Order","** List of Orders **")
            orders.forEach { Log.v("Order","${it}") }
        }

    override fun delete(order:OrderModel)
    {
        orders.remove(order)
    }

}
