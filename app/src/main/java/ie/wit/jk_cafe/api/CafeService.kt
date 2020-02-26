package ie.wit.jk_cafe.api

import com.google.gson.GsonBuilder
import ie.wit.jk_cafe.models.OrderModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface CafeService
{
    @GET("/orders")
    fun getall(): Call<List<OrderModel>>

    @GET("/orders/{id}")
    fun get(@Path("id")id:String):Call<OrderModel>

    @DELETE("/orders/{id}")
    fun delete(@Path("id")id:String):Call<CafeWrapper>

    @POST("/orders")
    fun post(@Body order: OrderModel):Call<CafeWrapper>

    @PUT("/orders/{id}")
    fun put(@Path("id")id: String,
        @Body order:OrderModel): Call<CafeWrapper>

    companion object{
        val serviceURL = "https://donationweb-hdip-server.herokuapp.com"

        fun create(): CafeService
        {
            val gson = GsonBuilder().create()

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(serviceURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
            return retrofit.create(CafeService::class.java)
        }
    }
}