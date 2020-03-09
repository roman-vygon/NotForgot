package com.rvygon.notforgot.Model

import androidx.room.ColumnInfo
import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Priority(@PrimaryKey(autoGenerate = false)  @ColumnInfo(name = "id_priority") var id: Int, @ColumnInfo(name = "name_priority") val name: String, val color: String) : Serializable
