package com.example.frequency.repositorys.room

import com.example.frequency.model.User

interface UserRepository  {

    suspend fun findUserById(userId: Long): User?

    suspend fun addUser(user: User)

    suspend fun deleteUser(userId: Long)

}