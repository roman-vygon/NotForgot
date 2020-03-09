package com.rvygon.notforgot.Model

import androidx.room.Embedded
import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task (var title: String, var created: Long, var description: String, var deadline: Long, @Embedded var priority: Priority, @Embedded var category: Category, @PrimaryKey(autoGenerate = false) var id: Int, var done: Int) : Serializable