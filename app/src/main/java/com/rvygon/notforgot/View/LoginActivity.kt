package com.rvygon.notforgot.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rvygon.notforgot.Presenter.LoginPresenter
import com.rvygon.notforgot.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {


    lateinit var presenter:LoginPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = LoginPresenter(this)
        presenter.checkToken()


        setContentView(R.layout.activity_login)
        loginButtonLog.setOnClickListener {
            presenter.login()

        }
        registerButtonLog.setOnClickListener {
            presenter.register()
        }
    }

}
