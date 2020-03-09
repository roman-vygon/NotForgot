package com.rvygon.notforgot.Model

import androidx.room.*


@Dao
interface TaskDao {

    @Query("Select * FROM Task")
    fun getAllTasks(): List<Task>

    @Query("DELETE FROM Task")
    fun deleteTasks()

    @Delete
    fun deleteTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createTask(newUser: Task)
}