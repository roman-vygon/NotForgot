package com.rvygon.notforgot.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rvygon.notforgot.Presenter.TaskDetailsPresenter
import com.rvygon.notforgot.R
import kotlinx.android.synthetic.main.activity_task_details.*

class TaskDetailsActivity : AppCompatActivity() {

    lateinit var presenter:TaskDetailsPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)



        presenter = TaskDetailsPresenter(this)
        presenter.setupViews()

        priorityTextView.setOnClickListener {
            presenter.priorityClick()
        }
        editButton.setOnClickListener {
            presenter.edit()
        }
    }
}
