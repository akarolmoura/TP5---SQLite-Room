package br.unisanta.approom.view

import android.app.Activity
import android.content.Intent
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

class RegisterActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var spinnerCourses: Spinner
    private var courses: List<Course> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = AppDatabase.getDatabase(this)
        spinnerCourses = findViewById(R.id.spinnerCourses)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextAge = findViewById<EditText>(R.id.editTextAge)
        val editTextPhone = findViewById<EditText>(R.id.editTextPhone)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        lifecycleScope.launch {
            courses = db.courseDao().getAllCourses()
            val courseNames = courses.map { it.name }
            val adapter = ArrayAdapter(this@RegisterActivity, android.R.layout.simple_spinner_item, courseNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCourses.adapter = adapter
        }

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toIntOrNull()
            val phone = editTextPhone.text.toString()
            val password = editTextPassword.text.toString()
            
            if (spinnerCourses.selectedItem == null) {
                Toast.makeText(this, "Por favor, selecione um curso", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val selectedCourseName = spinnerCourses.selectedItem.toString()
            val courseId = courses.find { it.name == selectedCourseName }?.id

            if (name.isNotEmpty() && age != null && phone.isNotEmpty() && password.isNotEmpty() && courseId != null) {
                lifecycleScope.launch {
                    db.userDao().insert(User(name = name, age = age, phone = phone, password = password, courseId = courseId))
                    
                    val resultIntent = Intent()
                    resultIntent.putExtra("NEW_USERNAME", name)
                    setResult(Activity.RESULT_OK, resultIntent)
                    
                    Toast.makeText(this@RegisterActivity, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish() // Fecha a tela de registro
                }
            } else {
                Toast.makeText(this@RegisterActivity, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
