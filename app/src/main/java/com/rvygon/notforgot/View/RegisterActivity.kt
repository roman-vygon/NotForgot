package com.rvygon.notforgot.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rvygon.notforgot.Presenter.RegisterPresenter
import com.rvygon.notforgot.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity()  {


lateinit var presenter:RegisterPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        presenter = RegisterPresenter(this)
        loginButtonReg.setOnClickListener {
            presenter.login()
        }

        registerButtonReg.setOnClickListener {
           presenter.register()
        }
    }
}
