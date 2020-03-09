package com.rvygon.notforgot.Model

import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class FormValidator {
    companion object {

        fun checkEmail(input: TextInputLayout, text: TextInputEditText):Boolean {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text.text.toString()).matches()){
                input.error = "Неверный email"
                return false
            }
            return true
        }

        fun checkPasswords(firstPwd: TextInputEditText, secondPwd: TextInputEditText, layout: TextInputLayout): Boolean
        {
            if (firstPwd.text.toString() != secondPwd.text.toString())
            {
                layout.error = "Пароли не совпадают"
                return false
            }
            return true
        }

        fun checkNonEmpty(input: TextInputLayout, text: TextInputEditText):Boolean {
            if (text.text.toString() == ""){
                input.error = "Это обязательное поле"
                return false
            }
            return true
        }
    }
}