package com.example.frequency.repositorys.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.frequency.repositorys.room.entites.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user_app_info WHERE id = :id")
    suspend fun findUserById(id: Long): UserEntity?

    @Insert
    suspend fun addUser(userEntity: UserEntity)

    @Query("DELETE FROM user_app_info WHERE id =:id")
    suspend fun deleteUser(id: Long): Int

}