package com.rvygon.notforgot.Presenter

import android.content.Context
import android.content.Intent
import com.rvygon.notforgot.Model.APIHandler
import com.rvygon.notforgot.Model.FormValidator
import com.rvygon.notforgot.View.LoginActivity
import com.rvygon.notforgot.View.MainActivity
import com.rvygon.notforgot.View.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginPresenter (val activity:LoginActivity) {
    fun swapActivity(context: Context) {
        val intent = Intent(context, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
    fun checkToken() {
        if (APIHandler.getAPIToken(activity) != "")
            swapActivity(activity)
    }
    fun login() {
        val layouts = arrayListOf(activity.passwordInputLayout, activity.loginInputLayout)
        val edits = arrayListOf(activity.passwordInputEditText, activity.loginInputEditText)

        var ok = true
        for (i in 0..1)
        {
            layouts[i].error = null
            ok = ok and FormValidator.checkNonEmpty(layouts[i], edits[i])
        }

        if (!ok)
            return

        APIHandler.login(activity.loginInputEditText.text.toString(), activity.passwordInputEditText.text.toString(), activity, ::swapActivity)
    }
    fun register() {
        val intent = Intent(activity, RegisterActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}