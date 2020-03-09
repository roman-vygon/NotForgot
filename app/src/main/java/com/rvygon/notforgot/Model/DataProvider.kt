package com.rvygon.notforgot.Model

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataProvider (val context: Context, private val emptyText: TextView, private val placeholderImage: ImageView) {
    val dataset : MutableList<Task> = arrayListOf()
    var categories: List<Category> = arrayListOf()
    var priorities: List<Priority> = arrayListOf()
    var isOffline: Boolean = false
    var wasOffline: Boolean = false
    val dbHandler = DBHandler(context)
    lateinit var adapter: CustomAdapter

    private fun createDataset(tasks: List<Task>, categories: List<Category>) : MutableList<Task> {

        val newDataset = arrayListOf<Task>()
        for (category in categories){
            newDataset.add(
                Task(
                    category.name,
                    0,
                    "isCategory!!!",
                    0,
                    Priority(0, "0", "0"),
                    category,
                    0,
                    0
                )
            )
            for (task in tasks) {
                if (task.category.id == category.id)
                    newDataset.add(task)
            }
        }
        return newDataset
    }
    fun initTasks() {
        if (!isOffline) {
            APIHandler.getTasks(
                context,
                this,
                dbHandler,
                ::createDataset
            )
            APIHandler.getCategories(context, this, dbHandler)
            APIHandler.getPriorities(context, this, dbHandler)
        }
        else {
            dbHandler.getTasks(this, ::createDataset)
            dbHandler.getCategories(this)
            dbHandler.getPriorities(this)
        }
    }
    fun changeDone(task: Task) {
        task.done = 1 - task.done
        APIHandler.editTask(context, task)
        checkAndInitTasks()
    }
    fun checkAndInitTasks() {
        APIHandler.checkOffline(context, this)
    }

    fun checkEmpty() : Boolean
    {
        if (adapter.itemCount == 0)
        {
            emptyText.visibility = View.VISIBLE
            placeholderImage.visibility = View.VISIBLE
        }
        else
        {
            emptyText.visibility = View.INVISIBLE
            placeholderImage.visibility = View.INVISIBLE
        }
        return adapter.itemCount != 0
    }
    fun addItem(context: Context, task: Task) {
        if (!isOffline) {
            APIHandler.addTask(context, task)
        }
        else
        {
            wasOffline = true
            dbHandler.createTask(task) {}
        }
        dataset.clear()
        initTasks()
    }
    fun removeItem(viewHolder: RecyclerView.ViewHolder) : String  {
        val name = dataset[viewHolder.adapterPosition].title
        if (!isOffline) {
            APIHandler.deleteTask(
                context,
                dataset[viewHolder.adapterPosition]
            )
        }
        else
        {
            wasOffline = true
            dbHandler.deleteTask(dataset[viewHolder.adapterPosition])
        }
        dataset.clear()
        initTasks()
        return name
    }
}