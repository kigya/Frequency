package com.example.frequency.repositories.local.room.user_db

import com.example.frequency.model.User

interface UserRepository  {

    suspend fun findUserById(userId: Long): User?

    suspend fun addUser(user: User)

    suspend fun deleteUser(userId: Long)

}