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

    fun searchAddress(address: String, callBack: (List<Suggestion>) -> Unit) {

        val param = Parameters(context, "Location", Body(address), Params())

        getClient().getSuggestions(param).enqueue(Request<Dictionary>(context) {
            it.body()?.Result?.Suggestions?.let { suggestions ->
                if (suggestions.isNotEmpty()) {
                    callBack(suggestions)
                }
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

}