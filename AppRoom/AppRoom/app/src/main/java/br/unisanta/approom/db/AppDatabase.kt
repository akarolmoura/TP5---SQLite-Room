package br.unisanta.approom.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.unisanta.approom.dao.CourseDao
import br.unisanta.approom.dao.UserDao
import br.unisanta.approom.model.Course
import br.unisanta.approom.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [User::class, Course::class], version = 1)
abstract class AppDatabase : RoomDatabase() {


    abstract fun userDao(): UserDao

    abstract fun courseDao(): CourseDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .addCallback(AppDatabaseCallback(context.applicationContext))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                val courseDao = getDatabase(context).courseDao()
                populateCourses(courseDao)
            }
        }

        suspend fun populateCourses(courseDao: CourseDao) {
            val courses = listOf(
                Course(name = "Ciência da Computação"),
                Course(name = "Sistemas de Informação"),
                Course(name = "Engenharia de Software"),
                Course(name = "Análise e Desenvolvimento de Sistemas"),
                Course(name = "Jogos Digitais"),
                Course(name = "Redes de Computadores")
            )

            courseDao.insertAll(*courses.toTypedArray())
        }
    }
}
