package br.unisanta.approom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.unisanta.approom.model.Course

@Dao
interface CourseDao {

    @Insert
    suspend fun insert(course: Course)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg courses: Course)

    @Query("SELECT * FROM courses")
    suspend fun getAllCourses(): List<Course>
}
