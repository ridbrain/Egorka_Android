package com.egorka.delivery.handlers

import android.app.Activity
import com.egorka.delivery.entities.*
import com.egorka.delivery.services.GeneralRouter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.ArrayList

interface RestApi {

    @GET("maps/api/directions/json")
    fun getPolyline(
        @Query("origin") originLatLng: String,
        @Query("destination") destLatLng: String,
        @Query("key") key: String
    ): Call<GoogleMapDTO>

    @POST("service/auth/user/")
    fun getUUID(
        @Body param: Parameters
    ): Call<AnswerUser>

    @POST("service/delivery/dictionary/")
    fun getSuggestions(
        @Body param: Parameters
    ): Call<Dictionary>

    @POST("service/delivery/dictionary/")
    fun getMarketplaces(
        @Body param: Parameters
    ): Call<Marketplaces>

    @POST("service/delivery/")
    fun getDelivery(
        @Body param: Parameters
    ): Call<Delivery>

}

class NetworkHandler(val context: Activity) {

    private val BASE_URL = "https://app.egorka.dev/"
    private val GOOGLE_URL = "https://maps.googleapis.com/"

    private fun getClient(baseUrl: String = BASE_URL): RestApi {
        return Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(RestApi::class.java)
    }

    open class Request<T>(val context: Activity, val callBack: (Response<T>) -> Unit) : Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                callBack(response)
            } else {
                openError()
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            openError()
        }

        private fun openError() {
            GeneralRouter(context).openErrorActivity()
        }

    }

    fun getUUID(callBack: (UserUUID) -> Unit) {

        val param = Parameters(context, "UUIDCreate", "", "")

        getClient().getUUID(param).enqueue(Request<AnswerUser>(context) {
            it.body()?.Result?.let { user ->
                if (user.Status == "Active") {
                    callBack(user)
                }
            }
        })

    }

    fun searchAddress(address: String, callBack: (List<Dictionary.Suggestion>) -> Unit) {

        val param = Parameters(context, "Location", com.egorka.delivery.entities.Query(address), Params())

        getClient().getSuggestions(param).enqueue(Request<Dictionary>(context) {
            it.body()?.Result?.Suggestions?.let { suggestions ->
                if (suggestions.isNotEmpty()) {
                    callBack(suggestions)
                }
            }
        })

    }

    fun calculateDelivery(type: DeliveryType, locations: List<OrderLocation>, callBack: (Delivery) -> Unit) {

        val param = Parameters(context, "Calculate",  DeliveryCalc(type, locations), Params())

        getClient().getDelivery(param).enqueue(Request<Delivery>(context) { delivery ->
            delivery.body()?.Type = type
            delivery.body()?.Result?.TotalPrice?.let {
                callBack(delivery.body()!!)
            }
        })

    }

    fun getPolyline(pickup: String, drop: String, key: String, callback: (Routes) -> Unit) {

        getClient(GOOGLE_URL).getPolyline(pickup, drop, key).enqueue(Request<GoogleMapDTO>(context) { google ->
            google.body()?.routes?.let { routes ->
                if (routes.isNotEmpty()) {
                    callback(routes[0])
                }
            }
        })

    }

    fun getMarketPlaces(callBack: (List<Marketplaces.MarketplacesPoint>) -> Unit) {

        val param = Parameters(context, "Marketplace", ArrayList<String>(), Params())

        getClient().getMarketplaces(param).enqueue(Request<Marketplaces>(context) {
            it.body()?.Result?.Points?.let { points ->
                if (points.isNotEmpty()) {
                    callBack(points)
                }
            }
        })

    }

}