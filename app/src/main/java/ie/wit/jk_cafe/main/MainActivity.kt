package ie.wit.jk_cafe.main

import android.app.Application
import android.util.Log
import ie.wit.jk_cafe.api.CafeService
import ie.wit.jk_cafe.models.OrderMemStore
import ie.wit.jk_cafe.models.OrderStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainActivity : Application(), AnkoLogger {

    lateinit var ordersStore: OrderMemStore
    lateinit var cafeService: CafeService

    override fun onCreate() {
        super.onCreate()
        ordersStore = OrderMemStore()
        Log.v("Order","JK Café App started")

        cafeService = CafeService.create()
        info("JK Café Service Created")
    }
}