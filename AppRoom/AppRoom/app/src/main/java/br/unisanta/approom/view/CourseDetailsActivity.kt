package br.unisanta.approom.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.unisanta.approom.R
import br.unisanta.approom.db.AppDatabase
import kotlinx.coroutines.launch

class CourseDetailsActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var courseId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details)

        db = AppDatabase.getDatabase(this)
        courseId = intent.getIntExtra("COURSE_ID", -1)

        val textViewCourseName = findViewById<TextView>(R.id.textViewCourseName)

        lifecycleScope.launch {
            val course = db.courseDao().getAllCourses().find { it.id == courseId }
            course?.let {
                textViewCourseName.text = it.name
            }
        }
    }
}