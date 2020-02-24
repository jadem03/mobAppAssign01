package ie.wit.jk_cafe.main

import android.app.Application
import android.util.Log
import ie.wit.jk_cafe.models.OrderMemStore
import ie.wit.jk_cafe.models.OrderStore

class MainActivity : Application() {

    lateinit var ordersStore: OrderStore

    override fun onCreate() {
        super.onCreate()
        ordersStore = OrderMemStore()
        Log.v("Order","JK Café App started")
    }
}