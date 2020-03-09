package com.rvygon.notforgot.Model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, Task::class, Priority::class, Category::class],version = 1, exportSchema = false)
abstract class dataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun priorityDao(): PriorityDao
    abstract fun taskDao(): TaskDao
}