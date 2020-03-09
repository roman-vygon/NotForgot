package com.rvygon.notforgot.Model


import android.content.Context
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DBHandler(val context: Context) {

    fun createTask(task: Task, onResult: ()->Unit){

        GlobalScope.launch(Dispatchers.Main) {

            withContext(Dispatchers.IO) {

                db.taskDao().createTask(task)
            }
            onResult()
        }
    }
    fun deleteTask(task: Task){

        GlobalScope.launch(Dispatchers.Main) {

            withContext(Dispatchers.IO) {

                db.taskDao().deleteTask(task)
            }

        }
    }

    fun getCategories(managerObj: DataProvider){

        GlobalScope.launch(Dispatchers.Main) {

            val categories = withContext(Dispatchers.IO) {

                db.categoryDao().getAllCategories()

            }
             managerObj.categories = categories

        }
    }
    fun setCategories(context: Context, spinner:Spinner, ids:MutableList<Int>, category:String) {
        GlobalScope.launch(Dispatchers.Main) {

            val categories = withContext(Dispatchers.IO) {

                db.categoryDao().getAllCategories()

            }
            val categoryNames = mutableListOf<String>()
            for (category1 in categories) {
                categoryNames.add(category1.name)
                ids.add(category1.id)
            }

            val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, categoryNames)
            // Set layout to use when the list of choices appear
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Set Adapter to Spinner
            spinner.adapter = aa
            for (i in 0 until categoryNames.size) {
                if (categoryNames[i] == category)
                    spinner.setSelection(i, true)
            }
        }
    }
    fun setPriorities(context: Context, spinner:Spinner, ids:MutableList<Int>, priority: String) {
        GlobalScope.launch(Dispatchers.Main) {

            val priorities = withContext(Dispatchers.IO) {

                db.priorityDao().getAllPriorities()

            }
            val priorityNames = mutableListOf<String>()
            for (priority1 in priorities) {
                priorityNames.add(priority1.name)
                ids.add(priority1.id)
            }
            val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, priorityNames)
            // Set layout to use when the list of choices appear
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Set Adapter to Spinner
            spinner.adapter = aa
            for (i in 0 until priorityNames.size) {
                if (priorityNames[i] == priority)
                    spinner.setSelection(i, true)
            }

        }
    }
    fun getTasks(managerObj: DataProvider, createDataset : (tasks:List<Task>, categories: List<Category>)->List<Task>){

        GlobalScope.launch(Dispatchers.Main) {

            val tasks = withContext(Dispatchers.IO) {

                db.taskDao().getAllTasks()

            }
            val categories = arrayListOf<Category>()
            val used: MutableMap<Int,Boolean> = mutableMapOf()
            for (task in tasks){
                val category = task.category
                val use = used[category.id]
                if (use == null)
                {
                    used[category.id] = true
                    categories.add(category)
                }
            }
            managerObj.dataset.clear()
            managerObj.dataset.addAll(createDataset(tasks,categories))
            managerObj.adapter.notifyDataSetChanged()
            managerObj.checkEmpty()
        }
    }

    fun getPriorities(managerObj: DataProvider){

        GlobalScope.launch(Dispatchers.Main) {

            val priorities = withContext(Dispatchers.IO) {

                db.priorityDao().getAllPriorities()

            }
            managerObj.priorities = priorities

        }
    }
    fun initTasks(tasks: List<Task>) {
        GlobalScope.launch(Dispatchers.Main) {

            withContext(Dispatchers.IO) {

                db.taskDao().deleteTasks()
                for (task in tasks){
                    db.taskDao().createTask(task)
                }
            }
        }
    }
    fun initCategories(categories: List<Category>) {
        GlobalScope.launch(Dispatchers.Main) {

            withContext(Dispatchers.IO) {

                db.categoryDao().deleteCategories()
                for (category in categories) {
                    db.categoryDao().createCategory(category)
                }
            }
        }
    }
    fun initPriorities(priorities: List<Priority>) {

        GlobalScope.launch(Dispatchers.Main) {

            withContext(Dispatchers.IO) {

                db.priorityDao().deletePriorities()
                for (priority in priorities) {
                    db.priorityDao().createPriority(priority)

                }
            }
        }
    }

    private val db = Room.databaseBuilder(
        context,
        dataBase::class.java,
        Environment.getExternalStorageDirectory().absolutePath + "/NotForgot/Database/" + DATABASE_FILE_NAME
    ).build()

    fun clearDatabase() {
        GlobalScope.launch(Dispatchers.Main) {

            withContext(Dispatchers.IO) {
                db.clearAllTables()
                }
            }
        }


    fun closeDatabase() {
        if(db.isOpen) {
            db.openHelper.close()
        }
    }
    companion object{
        const val DATABASE_FILE_NAME = "database"
    }
}