package com.example.frequency.data.repository.local.user

import com.example.frequency.data.model.User
import com.example.frequency.data.local.dao.user.UserDao
import com.example.frequency.data.model.entity.user.toUserEntity
import com.example.frequency.domain.repository.local.user_repository.UserRepository
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