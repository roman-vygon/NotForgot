package com.rvygon.notforgot.Model

import androidx.room.ColumnInfo
import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(@PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id_category") var id: Int, @ColumnInfo(name = "name_category")val name: String) : Serializable
