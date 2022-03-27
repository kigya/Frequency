package com.example.frequency.datasource.local.repositories.room.user_db.entites

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.frequency.model.User

@Entity(tableName = "user_app_info")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val email: String,
    val icon: String,
    val gToken: String
) {

    fun toUser() = User(
        name = this.name,
        email = this.email,
        icon = Uri.parse(this.icon) ?: Uri.EMPTY,
        secureKey = this.gToken,
    )

}

fun User.toUserEntity() = UserEntity(
    id = 1,
    name = this.name,
    email = this.email,
    icon = this.icon.toString(),
    gToken = this.secureKey,
)
