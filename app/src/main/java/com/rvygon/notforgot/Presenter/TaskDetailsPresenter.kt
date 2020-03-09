package com.rvygon.notforgot.Presenter

import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import com.rvygon.notforgot.Model.Task
import com.rvygon.notforgot.View.EditTaskActivity
import com.rvygon.notforgot.View.TaskDetailsActivity
import kotlinx.android.synthetic.main.activity_task_details.*

class TaskDetailsPresenter(val activity: TaskDetailsActivity) {
    var count = 0
    lateinit var task:Task
    fun setupViews() {
        task = activity.intent.getSerializableExtra("Task") as Task
        activity.titleTextView.text = task.title
        activity.bodyTextView.text = task.description
        val actionBar = activity.supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        activity.categoryTextView.text = task.category.name
        activity.priorityTextView.setBackgroundColor(Color.parseColor(task.priority.color))
        activity.priorityTextView.text = task.priority.name
        activity.statusTextView.text = when(task.done) {
            0 -> "Не выполнено"
            1 -> "Выполнено"
            else -> "Ты кто"
        }
        val format = java.text.SimpleDateFormat("dd.MM.yyyy")
        activity.dateCreatedTextView.text = format.format(java.util.Date(task.created.toLong() * 1000)).toString()
        activity.dateUntilTextView.text = "До " + format.format(java.util.Date(task.deadline.toLong() * 1000)).toString()
    }

    fun priorityClick() {
        count+=1
        if (count == 5)
        {
            activity.easterEgg.frame = 0
            activity.easterEgg.visibility = View.VISIBLE
            activity.easterEgg.playAnimation()
            activity.easterEgg.playSoundEffect(1)
            object : CountDownTimer(3000,1000){
                override fun onFinish() {
                    activity.easterEgg.cancelAnimation()
                    activity.easterEgg.visibility= View.INVISIBLE
                }

                override fun onTick(millisUntilFinished: Long) {

                }

            }.start()
        }
    }
    fun edit() {
        val intent = Intent(activity, EditTaskActivity::class.java)
        intent.putExtra("Task", task)
        activity.startActivityForResult(intent, 0)
    }
}