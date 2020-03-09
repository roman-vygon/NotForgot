package com.rvygon.notforgot.Presenter

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.rvygon.notforgot.View.LoginActivity
import com.rvygon.notforgot.View.SplashScreen

class SplashPresenter (val activity: SplashScreen){
    val REQUEST_PERMISSION_CODE = 1
    fun onPermissionResult(requestCode: Int,
                           permissions: Array<String>,
                           grantResults: IntArray)
    { when (requestCode) {
        REQUEST_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
            val permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED
            val permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED
            val permissionToRead = grantResults[2] == PackageManager.PERMISSION_GRANTED
            if (permissionToRecord && permissionToStore && permissionToRead) {
                val intent = Intent(activity, LoginActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            } else {
                Toast.makeText(activity, "Permission Denied", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }}
fun runPermissions () {
    if (checkPermissions())
        requestPermissions()
    else {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}
    private fun requestPermissions() {
        requestPermissions(
            activity,
            arrayOf(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }
    fun checkPermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val result1 = ContextCompat.checkSelfPermission(activity,
            Manifest.permission.INTERNET
        )
        val result2 = ContextCompat.checkSelfPermission(activity,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
    }
}