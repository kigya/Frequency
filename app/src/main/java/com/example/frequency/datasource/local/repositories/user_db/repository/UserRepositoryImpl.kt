package com.example.frequency.datasource.local.repositories.user_db.repository

import com.example.frequency.model.User
import com.example.frequency.datasource.local.repositories.user_db.entites.toUserEntity
import com.example.frequency.datasource.local.repositories.user_db.UserDao
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun findUserById(userId: Long): User? {
        return userDao.findUserById(userId)?.toUser()
    }

    override suspend fun addUser(user: User) {
        userDao.addUser(user.toUserEntity())
    }

    override suspend fun deleteUser(userId: Long) {
        userDao.deleteUser(userId)
    }
}