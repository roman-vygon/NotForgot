package com.rvygon.notforgot.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rvygon.notforgot.Presenter.SplashPresenter

class SplashScreen : AppCompatActivity() {
lateinit var presenter: SplashPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SplashPresenter(this)
        presenter.runPermissions()

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        presenter.onPermissionResult(requestCode,permissions,grantResults)
    }



}
