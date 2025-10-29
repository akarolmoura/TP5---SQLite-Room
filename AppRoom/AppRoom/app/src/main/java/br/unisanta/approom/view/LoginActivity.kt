package br.unisanta.approom.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.unisanta.approom.databinding.ActivityLoginBinding
import br.unisanta.approom.db.AppDatabase
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: AppDatabase


    private val registerActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Verifica se a atividade retornou um resultado OK.
            val newUsername = result.data?.getStringExtra("NEW_USERNAME")
            if (newUsername != null) {
                binding.editTextUsername.setText(newUsername)
                binding.editTextPassword.requestFocus()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getDatabase(this)

        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Por favor, insira o nome de usuário e a senha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = db.userDao().login(username, password)
                if (user != null) {
                    val intent = Intent(this@LoginActivity, UserDetailsActivity::class.java)
                    intent.putExtra("USER_ID", user.id)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Credenciais inválidas", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttonGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            registerActivityResultLauncher.launch(intent)
        }
    }
}
