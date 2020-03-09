package com.rvygon.notforgot.View

import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.rvygon.notforgot.Presenter.CreateTaskPresenter
import com.rvygon.notforgot.R
import kotlinx.android.synthetic.main.activity_create_task.*


class CreateTaskActivity : AppCompatActivity() {
  lateinit var presenter:CreateTaskPresenter
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        presenter.optionSelected()

        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        presenter = CreateTaskPresenter(this)
        presenter.setup()

        categoryAddButton.setOnClickListener { presenter.showDialog(this)}
        saveBtn.setOnClickListener {
            presenter.save()

        }
        bodyInputEditText.addTextChangedListener {
            counterTextView.text = bodyInputEditText.text.toString().length.toString() + "/120"
        }
        dateEditText.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
               presenter.dateSelect()
                return@OnTouchListener true
            }
            false
        })
    }
}
