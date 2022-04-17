package com.example.frequency.data.local.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.frequency.data.model.entity.user.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user_app_info WHERE id = :id")
    suspend fun findUserById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(userEntity: UserEntity)

    @Query("DELETE FROM user_app_info WHERE id =:id")
    suspend fun deleteUser(id: Long): Int

}