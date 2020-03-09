package com.rvygon.notforgot.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao{

    @Query("Select * FROM Category")
    fun getAllCategories(): List<Category>

    @Query("DELETE FROM Category")
    fun deleteCategories()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createCategory(newUser: Category)
}