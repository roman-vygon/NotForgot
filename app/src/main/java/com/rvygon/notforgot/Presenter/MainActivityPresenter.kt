package com.rvygon.notforgot.Presenter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rvygon.notforgot.Model.CustomAdapter
import com.rvygon.notforgot.Model.DBHandler
import com.rvygon.notforgot.Model.DataProvider
import com.rvygon.notforgot.Model.Task
import com.rvygon.notforgot.R
import com.rvygon.notforgot.View.CreateTaskActivity
import com.rvygon.notforgot.View.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivityPresenter(val activity:MainActivity) {
    private lateinit var deleteIcon: Drawable
    private val swipeBg: ColorDrawable = ColorDrawable(Color.parseColor("#EF2727"))

    lateinit var managerObj: DataProvider
    fun add() {
        val intent = Intent(activity, CreateTaskActivity::class.java)
        activity.startActivityForResult(intent, 1)
    }
    fun optionSelected() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Выйти?")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton("ОТМЕНА") { dialog, which ->
            dialog.cancel()
        }

        builder.setNegativeButton("ДА!") { dialog, which ->
            val myPrefs = activity.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val editor = myPrefs.edit()
            managerObj.dbHandler.clearDatabase()
            editor.putString(activity.getString(R.string.token_key), "")
            editor.commit()
            activity.moveTaskToBack(true)
            System.exit(0)
        }

        builder.show()
    }
    fun setupSwipe() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val fileName = managerObj.removeItem(viewHolder)


                Toast.makeText(activity,"$fileName deleted", Toast.LENGTH_LONG).show()
            }
            override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                return if (viewHolder is CustomAdapter.CategoryViewHolder) 0 else super.getSwipeDirs(
                    recyclerView,
                    viewHolder
                )
            }
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                swipeBg.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                deleteIcon.setBounds(itemView.right - iconMargin-deleteIcon.intrinsicWidth, itemView.top + iconMargin, itemView.right - iconMargin, itemView.bottom - iconMargin)
                swipeBg.draw(c)
                deleteIcon.draw(c)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(activity.taskRecyclerView)
    }
    fun destroy() {
        val dbHandler = DBHandler(activity)
        dbHandler.closeDatabase()
    }
    fun setup() {
        deleteIcon = ContextCompat.getDrawable(activity, R.drawable.delete_icon)!!
        val root = Environment.getExternalStorageDirectory()
        val file = File(root.absolutePath + "/NotForgot/Database")
        if (!file.exists()) {
            file.mkdirs()
        }
        managerObj = DataProvider(activity, activity.emptyText, activity.placeholderImage)
        val broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                val task = intent?.getSerializableExtra("Task") as Task
                managerObj.addItem(activity, task)
            }
        }
        managerObj.checkAndInitTasks()
        setupRecyclerView()
        LocalBroadcastManager.getInstance(activity).registerReceiver(broadCastReceiver, IntentFilter("ADD_TASK"))
    }
    private fun setupRecyclerView() {
        activity.taskRecyclerView.layoutManager = LinearLayoutManager(activity,
            LinearLayoutManager.VERTICAL, false)
        activity.taskRecyclerView.setHasFixedSize(true)
        activity.taskRecyclerView.adapter =
            CustomAdapter(managerObj.dataset, managerObj)
        managerObj.adapter = activity.taskRecyclerView.adapter as CustomAdapter
    }
    fun setupRefresh()
    {
        activity.swiperefresh.setOnRefreshListener {
            activity.swiperefresh.isRefreshing = false
            activity.syncAnimation.visibility = View.VISIBLE
            activity.syncAnimation.speed = 3.0f
            activity.syncAnimation.frame = 0

            activity.syncAnimation.playAnimation()


            managerObj.checkAndInitTasks()

            activity.swiperefresh.isEnabled = false
            object : CountDownTimer(3000,1000){
                override fun onFinish() {
                    activity.swiperefresh.isEnabled = true
                    activity.syncAnimation.cancelAnimation()
                    activity.syncAnimation.visibility = View.INVISIBLE
                }

                override fun onTick(millisUntilFinished: Long) {

                }
            }.start()


        }
    }
}