package com.rvygon.notforgot.Model
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET


interface LidaFactAPIService {

    @POST("register")
    fun register(
        @Query("email") email: String,
        @Query("name") name: String,
        @Query("password") password: String
    ): Call<RegisterResponse>

    @GET("priorities")
    fun getPriorities(
        @Query("api_token") api_token:String
    ): Call<List<Priority>>

    @GET("categories")
    fun getCategories(
        @Query("api_token") api_token:String
    ): Call<List<Category>>

    @GET("tasks")
    fun getTasks(
        @Query("api_token") api_token:String
    ): Call<List<Task>>

    @DELETE("tasks/{task_id}")
    fun deleteTask(
        @Path("task_id", encoded=true) taskId:String,
        @Query("api_token") api_token:String
    ):Call<ResponseBody>

    @PATCH("tasks/{task_id}")
    fun editTask(
        @Path("task_id", encoded=true) taskId:Int,
        @Query("api_token") api_token:String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("done") done: Int,
        @Query("deadline") deadline: Long,
        @Query("category_id") category_id: Int,
        @Query("priority_id") priority_id: Int
    ):Call<ResponseBody>

    @POST("tasks")
    fun addTask(
        @Query("api_token") api_token:String,
        @Query("title") title: String,
        @Query("description") description: String,
        @Query("done") done: Int,
        @Query("deadline") deadline: Long,
        @Query("category_id") category_id: Int,
        @Query("priority_id") priority_id: Int
    ):Call<ResponseBody>

    @POST("categories")
    fun addCategory(
        @Query("api_token") api_token:String,
        @Query("name") name:String
    ):Call<ResponseBody>

    @POST("login")
    fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ):Call<TokenResponse>


}
