package com.rvygon.notforgot.View

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.rvygon.notforgot.Presenter.MainActivityPresenter
import com.rvygon.notforgot.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var presenter: MainActivityPresenter

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        presenter.optionSelected()
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.mainmenu, menu)
        return true
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = MainActivityPresenter(this)
        presenter.setup()


        addNoteButton.setOnClickListener {
            presenter.add()
        }

        presenter.setupRefresh()

       presenter.setupSwipe()

    }



}
