package com.rvygon.notforgot.Presenter

import android.app.DatePickerDialog
import android.content.Context
import android.text.InputType
import android.widget.EditText
import com.rvygon.notforgot.Model.APIHandler
import com.rvygon.notforgot.Model.DBHandler
import com.rvygon.notforgot.Model.FormValidator
import com.rvygon.notforgot.Model.Task
import com.rvygon.notforgot.View.EditTaskActivity
import kotlinx.android.synthetic.main.activity_create_task.*
import java.text.SimpleDateFormat
import java.util.*

class EditTaskPresenter(val activity: EditTaskActivity) {
    lateinit var dbHandler: DBHandler
    val priorityIds = mutableListOf<Int>()
    val categoryIds = mutableListOf<Int>()
    var deadline = 0L
    lateinit var task:Task
    fun showDialog(context: Context){

        var categoryName: String

        val builder = android.app.AlertDialog.Builder(context)//AlertDialog.Builder(this)
        builder.setTitle("Добавить категорию")

        // Set up the input
        val input = EditText(context)
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.inputType = InputType.TYPE_CLASS_TEXT

        builder.setView(input)
        builder.setCancelable(false)

        builder.setPositiveButton("Save") { dialog, _ ->
            categoryName = input.text.toString()
            if (!categoryName.isEmpty()) {
                val context = this
                APIHandler.addCategory(activity, categoryName, dbHandler, activity.categorySpinner, categoryIds)
            } else {
                dialog.dismiss()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        val dialog = builder.create()
        dialog.show()

    }
    fun setup()
    {
        dbHandler = DBHandler(activity)




        val actionBar = activity.supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        activity.categoryAddButton.setOnClickListener {showDialog(activity)}
        task = activity.intent.getSerializableExtra("Task") as Task
        dbHandler.setCategories(activity, activity.categorySpinner, categoryIds, task.category.name)
        dbHandler.setPriorities(activity, activity.prioritySpinner, priorityIds, task.priority.name)
        activity.titleTextView.text = "Изменить заметку"
        activity.nameInputEditText.setText(task.title)
        activity.bodyInputEditText.setText(task.description)

        val format = java.text.SimpleDateFormat("dd.MM.yyyy")

        activity.dateEditText.setText(format.format(java.util.Date(task.deadline.toLong() * 1000)).toString())
    }
    fun date() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            activity,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                val str = "" + dayOfMonth + "." + (monthOfYear.toInt()+1).toString() + "." + year
                activity.dateEditText.setText(str)
                val date = SimpleDateFormat("dd.MM.yyyy").parse(str)
                deadline = date.time / 1000L
            },
            year,
            month,
            day
        )

        dpd.show()
    }
    fun save() {
        val layouts = arrayListOf(activity.nameInputLayout, activity.bodyInputLayout)
        val edits = arrayListOf(activity.nameInputEditText, activity.bodyInputEditText)
        var ok = true
        for (i in 0..1)
        {
            layouts[i].error = null
            ok = ok and FormValidator.checkNonEmpty(layouts[i], edits[i])
        }

        if (!ok)
            return

        task.title = activity.nameInputEditText.text.toString()
        task.deadline = deadline
        task.description = activity.bodyInputEditText.text.toString()
        task.category.id = categoryIds[activity.categorySpinner.selectedItemPosition]
        task.priority.id = priorityIds[activity.prioritySpinner.selectedItemPosition]
        APIHandler.editTask(activity, task)
        activity.finish()
    }

}