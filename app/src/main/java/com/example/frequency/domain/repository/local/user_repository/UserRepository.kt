package com.example.frequency.domain.repository.local.user_repository

import com.example.frequency.data.model.User

interface UserRepository  {

    suspend fun findUserById(userId: Long): User?

    suspend fun addUser(user: User)

    suspend fun deleteUser(userId: Long)

}