package com.rvygon.notforgot.Model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao{

    @Query("Select * FROM UserEntity")
    fun getAllUsers(): List<UserEntity>

    @Query("DELETE FROM UserEntity")
    fun deleteUsers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(newUser: UserEntity)
}