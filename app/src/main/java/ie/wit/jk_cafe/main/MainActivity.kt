package ie.wit.jk_cafe.main

import android.app.Application
import android.util.Log
import ie.wit.jk_cafe.models.OrderMemStore
import ie.wit.jk_cafe.models.OrderStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainActivity : Application(), AnkoLogger {

    lateinit var ordersStore: OrderMemStore

    override fun onCreate() {
        super.onCreate()
        ordersStore = OrderMemStore()
        Log.v("Order","JK Café App started")

    }
}