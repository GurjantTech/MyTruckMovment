package com.truckmovement.mvvm.data.remote

import com.truckmovement.mvvm.ui.LogIn.Model.LoginModel
import com.truckmovement.mvvm.ui.home.model.LogoutModel
import com.truckmovement.mvvm.ui.register.model.RegisterApiResponceModel
import com.truckmovement.mvvm.ui.truckdetail.model.SendMailResponceModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RequestInterface {

    @GET("User/GetUserLogin")
    fun userLoginApi(
        @Query("username") username: String,
        @Query("password") password: String
    ): Observable<LoginModel>

    @Headers("Content-Type:application/json")
    @POST("Company")
    fun userRegisterApi(@Body body: String): Observable<RegisterApiResponceModel>

    @Multipart
    @POST("CompanyMail")
    fun sendMailOnServer(
        @Part("Email") Email: RequestBody,
        @Part("FormName") FormName: RequestBody,
        @Part("Body") Body: RequestBody,
        @Part images: ArrayList<MultipartBody.Part>
    ): Observable<SendMailResponceModel>

    @PUT("User/SignoutUser")
    fun logoutApi(@Query("id")id:String):Observable<LogoutModel>

}