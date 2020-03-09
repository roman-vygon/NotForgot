package com.rvygon.notforgot.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PriorityDao{

    @Query("Select * FROM Priority")
    fun getAllPriorities(): List<Priority>

    @Query("DELETE FROM Priority")
    fun deletePriorities()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createPriority(newUser: Priority)
}