package com.rvygon.notforgot.Model

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rvygon.notforgot.R
import com.rvygon.notforgot.View.TaskDetailsActivity

/**
 * Provide views to RecyclerView with data from dataSet.
 *
 * Initialize the dataset of the Adapter.
 *
 * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
 */
class CustomAdapter(private val dataSet: MutableList<Task>, private val managerObj: DataProvider) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    abstract class ViewHolder(v: View) : RecyclerView.ViewHolder(v)

    class ItemViewHolder(v: View, managerObj: DataProvider) : ViewHolder(v) {
        val textView: TextView = v.findViewById(R.id.primaryTextView)
        val colorView: ImageView = v.findViewById(R.id.colorBar)
        val doneCheckbox: CheckBox = v.findViewById(R.id.checkBox)
        lateinit var data: Task
        init {
            doneCheckbox.setOnClickListener {
                managerObj.changeDone(data)
            }
            v.setOnClickListener{
                    val intent = Intent(v.context, TaskDetailsActivity::class.java)
                    intent.putExtra("Task", this.data)
                    v.context.startActivity(intent)
            }
        }
    }
    class CategoryViewHolder(v: View) : ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.categoryTextView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position].description == "isCategory!!!") 0 else 1
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == 1) {
            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.task_row, viewGroup, false)
            return ItemViewHolder(view, managerObj)
        }
        else {
            val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.task_category_row, viewGroup, false)
            return CategoryViewHolder(view)
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Log.d(TAG, "Element $position set.")
        if (viewHolder is ItemViewHolder) {
            viewHolder.data = dataSet[position]
            viewHolder.doneCheckbox.isChecked = dataSet[position].done == 1
            val priority = dataSet[position].priority
            viewHolder.colorView.setBackgroundColor(Color.parseColor(priority.color))
            viewHolder.textView.text = dataSet[position].title
        }
        if (viewHolder is CategoryViewHolder)
            viewHolder.name.text = dataSet[position].title
    }


    override fun getItemCount() = dataSet.size

    companion object {
        private const val TAG = "CustomAdapter"
    }
}
