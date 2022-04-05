package com.example.frequency.datasource.local.repositories.room.user_db.room

import com.example.frequency.model.User
import com.example.frequency.datasource.local.repositories.room.user_db.UserRepository
import com.example.frequency.datasource.local.repositories.room.user_db.entites.toUserEntity
import javax.inject.Inject

class UserRoomRepository @Inject constructor(
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