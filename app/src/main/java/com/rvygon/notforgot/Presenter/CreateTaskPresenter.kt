package com.rvygon.notforgot.Presenter

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rvygon.notforgot.Model.*
import com.rvygon.notforgot.View.CreateTaskActivity
import kotlinx.android.synthetic.main.activity_create_task.*
import java.text.SimpleDateFormat
import java.util.*

class CreateTaskPresenter (val activity:CreateTaskActivity) {
    lateinit var dbHandler: DBHandler
    val categoryIds = mutableListOf<Int>()
    val priorityIds = mutableListOf<Int>()

    var deadline: Long = 0
    fun showDialog(context: Context){

        var categoryName: String

        val builder = android.app.AlertDialog.Builder(context)
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

        val newTask = Task(
            activity.nameInputEditText.text.toString(),
            System.currentTimeMillis() / 1000L,
            activity.bodyInputEditText.text.toString(),
            deadline,

            Priority(
                priorityIds[activity.prioritySpinner.selectedItemId.toInt()],
                activity.prioritySpinner.selectedItem.toString(),
                "#FFFFFF"
            ),
            Category(
                categoryIds[activity.categorySpinner.selectedItemId.toInt()],
                activity.categorySpinner.selectedItem.toString()
            ),
            -1,
            0
        )

        val intent = Intent("ADD_TASK").putExtra("Task", newTask)
        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent)
        Toast.makeText(activity, "Задача создана", Toast.LENGTH_SHORT).show()
        activity.finish()
    }
    fun setup() {
        dbHandler = DBHandler(activity)

        dbHandler.setCategories(activity, activity.categorySpinner, categoryIds, "")
        dbHandler.setPriorities(activity, activity.prioritySpinner, priorityIds, "")

// Вызываем адаптер

        val actionBar = activity.supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    fun dateSelect() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            val str = "" + dayOfMonth + "." + (monthOfYear + 1).toString() + "." + year
            activity.dateEditText.setText(str)
            val date = SimpleDateFormat("dd.MM.yyyy").parse(str)
            deadline = date.time / 1000L
        }, year, month, day)

        dpd.show()
    }
    fun optionSelected() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Сохранить?")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("ОТМЕНА") { dialog, which ->
            activity.finish()
        }

        builder.setNegativeButton("ДА!") { dialog, which ->
          activity.saveBtn.callOnClick()
        }

        builder.show()
    }
}