package com.rvygon.notforgot.View

import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rvygon.notforgot.Presenter.EditTaskPresenter
import com.rvygon.notforgot.R
import kotlinx.android.synthetic.main.activity_create_task.*


class EditTaskActivity : AppCompatActivity() {

    lateinit var presenter: EditTaskPresenter



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
       presenter = EditTaskPresenter(this)
        presenter.setup()

        dateEditText.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
               presenter.date()
                return@OnTouchListener true
            }
            false
        })

        saveBtn.setOnClickListener {

            presenter.save()
        }
    }
}
