package br.unisanta.approom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.unisanta.approom.model.User


@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)
    @Update
    suspend fun update(user: User)
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User?
    @Query("SELECT * FROM users WHERE name = :name AND password = :password")
    suspend fun login(name: String, password: String): User?
}
