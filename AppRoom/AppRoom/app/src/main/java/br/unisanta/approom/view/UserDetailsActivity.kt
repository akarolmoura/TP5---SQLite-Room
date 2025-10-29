package br.unisanta.approom.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.unisanta.approom.R
import br.unisanta.approom.db.AppDatabase
import br.unisanta.approom.model.Course
import br.unisanta.approom.model.User
import kotlinx.coroutines.launch

class UserDetailsActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var userId: Int = -1
    private var user: User? = null
    private var courses: List<Course> = emptyList()

    private lateinit var editTextName: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var spinnerCourse: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        db = AppDatabase.getDatabase(this)
        userId = intent.getIntExtra("USER_ID", -1)

        editTextName = findViewById(R.id.editTextName)
        editTextAge = findViewById(R.id.editTextAge)
        editTextPhone = findViewById(R.id.editTextPhone)
        spinnerCourse = findViewById(R.id.spinnerCourse)
        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)

        lifecycleScope.launch {
            courses = db.courseDao().getAllCourses()
            val courseNames = courses.map { it.name }

            val adapter = ArrayAdapter(this@UserDetailsActivity, android.R.layout.simple_spinner_item, courseNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCourse.adapter = adapter

            user = db.userDao().getUserById(userId)
            user?.let {
                editTextName.setText(it.name)
                editTextAge.setText(it.age.toString())
                editTextPhone.setText(it.phone)

                val currentCoursePosition = courses.indexOfFirst { c -> c.id == it.courseId }
                if (currentCoursePosition != -1) {
                    spinnerCourse.setSelection(currentCoursePosition)
                }
            }
        }

        buttonUpdate.setOnClickListener {
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toIntOrNull()
            val phone = editTextPhone.text.toString()

            val selectedCourse = if (courses.isNotEmpty() && spinnerCourse.selectedItemPosition != Spinner.INVALID_POSITION) {
                courses[spinnerCourse.selectedItemPosition]
            } else null

            if (name.isNotEmpty() && age != null && phone.isNotEmpty() && selectedCourse != null) {
                lifecycleScope.launch {
                    user?.let {
                        val updatedUser = it.copy(
                            name = name,
                            age = age,
                            phone = phone,
                            courseId = selectedCourse.id // Correção aqui!
                        )
                        db.userDao().update(updatedUser)
                        Toast.makeText(this@UserDetailsActivity, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this@UserDetailsActivity, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}