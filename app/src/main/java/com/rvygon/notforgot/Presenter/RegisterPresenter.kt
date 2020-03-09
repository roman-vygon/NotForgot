package com.rvygon.notforgot.Presenter

import android.content.Context
import android.content.Intent
import com.rvygon.notforgot.View.LoginActivity
import com.rvygon.notforgot.View.MainActivity
import com.rvygon.notforgot.Model.APIHandler
import com.rvygon.notforgot.Model.FormValidator
import com.rvygon.notforgot.View.RegisterActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterPresenter(val activity: RegisterActivity) {

    fun swapActivity(context: Context)
    {
        val intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
    fun login() {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
        activity.finish()

    }
    fun register() {
        val layouts = arrayListOf(activity.passwordInputLayout, activity.emailInputLayout, activity.nameInputLayout, activity.passwordSecondInputLayout)
        val edits = arrayListOf(activity.passwordInputEdit, activity.emailInputText, activity.nameInputEdit, activity.secondPasswordEdit)

        var ok = true
        for (i in 0..3)
        {
            layouts[i].error = null
            ok = ok and FormValidator.checkNonEmpty(layouts[i], edits[i])
        }
        ok = ok and FormValidator.checkEmail(activity.emailInputLayout, activity.emailInputText)
        ok = ok and FormValidator.checkPasswords(activity.secondPasswordEdit, activity.passwordInputEdit, activity.passwordSecondInputLayout)

        if (!ok)
            return
        APIHandler.register(activity.emailInputText.text.toString(), activity.nameInputEdit.text.toString(), activity.passwordInputEdit.text.toString(), activity, ::swapActivity)

    }
}