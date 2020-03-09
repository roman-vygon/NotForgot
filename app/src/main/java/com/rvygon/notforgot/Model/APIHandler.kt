package com.rvygon.notforgot.Model


import android.content.Context
import android.widget.Spinner
import android.widget.Toast
import com.rvygon.notforgot.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
    class APIHandler {
        companion object {

        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://practice.mobile.kreosoft.ru/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            var service: LidaFactAPIService

        init {
            service = retrofit.create(
                LidaFactAPIService::class.java)
        }
        fun checkOffline(context: Context, managerObj: DataProvider)
        {
            val request = service.getPriorities(
                getAPIToken(
                    context
                )
            )
            request.enqueue(object : Callback<List<Priority>> {
                override fun onFailure(call: Call<List<Priority>>, t: Throwable) {
                    managerObj.isOffline = true
                    managerObj.initTasks()

                }

                override fun onResponse(call: Call<List<Priority>>, response: Response<List<Priority>>) {
                    managerObj.isOffline = false
                    managerObj.initTasks()
                }
            })
        }
        fun register(email: String, name: String, password:String, context:Context, onResponse: (context: Context) -> Unit)
        {
            val request = service.register(email, name, password)
            request.enqueue(object : Callback<RegisterResponse> {
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    println("Registration failure")
                    Toast.makeText(context, "Registration Failure", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    val facts = response.body()
                    if (facts != null) {
                        val myPrefs =context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
                        val editor = myPrefs.edit()
                        editor.putString(context.getString(R.string.token_key), facts.api_token)
                        editor.commit()
                        onResponse(context)
                    }
                    else
                    {
                        println("Registration failure null")
                        Toast.makeText(context, "Email already taken", Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
        fun getPriorities(context: Context, managerObj: DataProvider, dbObj: DBHandler) {

            val request = service.getPriorities(
                getAPIToken(
                    context
                )
            )
            request.enqueue(object : Callback<List<Priority>> {
                override fun onFailure(call: Call<List<Priority>>, t: Throwable) {
                    println("Get categories failure")
                    managerObj.isOffline = true

                }

                override fun onResponse(call: Call<List<Priority>>, response: Response<List<Priority>>) {
                    val facts = response.body()
                    managerObj.priorities = facts!!
                    dbObj.initPriorities(facts)

                }
            })
        }

        fun getCategories(context: Context, managerObj: DataProvider?, dbObj: DBHandler) : Call<List<Category>> {

            val request = service.getCategories(
                getAPIToken(
                    context
                )
            )
            request.enqueue(object : Callback<List<Category>> {
                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    println("Get categories failure")
                    managerObj?.isOffline = true

                }

                override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {
                    val facts = response.body()
                    managerObj?.categories = facts!!
                    dbObj.initCategories(facts)
                }
            })
            return request
        }

        fun login(email: String, password: String, context:Context, onResponse: (context: Context) -> Unit) {

            val request = service.login(email, password)
            request.enqueue(object : Callback<TokenResponse> {
                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    println("Login failure")
                    Toast.makeText(context, "Login Failure", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                    val facts = response.body()

                    if (facts != null) {
                        saveAPIToken(
                            context,
                            facts.api_token
                        )
                        onResponse(context)
                    }
                    else
                    {
                        Toast.makeText(context, "Pair email/password is incorrect", Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
        fun getTasks(context: Context, managerObj: DataProvider, dbObj:DBHandler, createDataset : (tasks:List<Task>, categories: List<Category>)->List<Task>) : Call<List<Task>> {


            val service = retrofit.create(LidaFactAPIService::class.java)
            val request = service.getTasks(
                getAPIToken(
                    context
                )
            )
            request.enqueue(object: Callback<List<Task>> {
                override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                    println("Failure getTasks")
                    managerObj.isOffline = true
                }

                override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {

                    val tasks = response.body()

                    if (tasks != null) {
                        for (task in managerObj.dataset) {
                            var isOnServer = false
                            for (task1 in tasks) {
                                if (task1.id == task.id) {
                                    isOnServer = true
                                    break
                                }
                            }
                            if (!isOnServer && task.description != "isCategory!!!") {
                                addTask(
                                    context,
                                    task
                                )
                            }
                        }
                    }
                    managerObj.dataset.clear()
                    val categories = arrayListOf<Category>()
                    val used: MutableMap<Int,Boolean> = mutableMapOf()

                    for (task in tasks!!){
                        val category = task.category
                        val use = used[category.id]
                        if (use == null)
                        {
                            used[category.id] = true
                            categories.add(category)
                        }
                    }
                    managerObj.dataset.addAll(createDataset(tasks, categories))
                    dbObj.initTasks(tasks)
                    managerObj.adapter.notifyDataSetChanged()
                    managerObj.checkEmpty()
                }
            })
            return request
        }
        fun addTask(context: Context, task: Task)  {
            val service = retrofit.create(LidaFactAPIService::class.java)
            val request = service.addTask(
                getAPIToken(
                    context
                ),task.title, task.description, task.done, task.deadline, task.category.id, task.priority.id)
            request.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("Add task failure")
                    Toast.makeText(context, "Add task failure", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                }
            })
        }
        fun editTask(context: Context, task: Task) {
            val service = retrofit.create(LidaFactAPIService::class.java)
            val request = service.editTask(task.id,
                getAPIToken(context),task.title, task.description, task.done, task.deadline, task.category.id, task.priority.id)
            request.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("Edit task failure")
                    Toast.makeText(context, "Edit task Failure", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                }
            })
        }
        fun deleteTask(context: Context, task: Task)
        {
            val service = retrofit.create(LidaFactAPIService::class.java)
            val request = service.deleteTask( task.id.toString(),
                getAPIToken(context)
            )
            request.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("Delete task failure")
                    Toast.makeText(context, "Delete task Failure", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                }
            })
        }

        fun addCategory(context: Context, name: String, dbHandler: DBHandler, categorySpinner: Spinner, categoryIds:MutableList<Int>) {

            val service = retrofit.create(LidaFactAPIService::class.java)
            val request = service.addCategory(
                getAPIToken(
                    context
                ), name)
            request.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {


                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    getCategories(
                        context,
                        null,
                        dbHandler
                    )

                    dbHandler.setCategories(context, categorySpinner, categoryIds, "")
                }
            })
        }


        fun getAPIToken(context: Context): String {
            val myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val apiToken = myPrefs.getString(context.getString(R.string.token_key), "")
            return apiToken!!
        }

        fun saveAPIToken(context: Context, token: String) {
            val myPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val editor = myPrefs.edit()
            editor.putString(context.getString(R.string.token_key), token)
            editor.commit()
        }
    }

}