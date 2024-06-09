package com.example.submissionstoryapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.submissionstoryapp.ui.Story.ListStoryActivity
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.databinding.ActivityMainBinding
import com.example.submissionstoryapp.response.login.LoginResponse
import com.example.submissionstoryapp.response.repository.Preference
import com.example.submissionstoryapp.ui.register.SignUpActivity
import com.example.submissionstoryapp.utils.ViewModelFactory
import com.example.submissionstoryapp.response.repository.Result
import com.example.submissionstoryapp.ui.register.MyEditText

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val token = Preference.getToken(this)
        if (token != null) {
            val intent = Intent(this, ListStoryActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        val myEditText = findViewById<MyEditText>(R.id.input_password)
        val errorTextView = findViewById<TextView>(R.id.textError)

        myEditText.setErrorTextView(errorTextView)
        playAnimation()



        val btnRegister = binding.btnRegister
        binding.inputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = binding.inputEmail.text.toString()
                if (email.isEmpty()) {
                    binding.inputEmail.error = "Input tidak boleh kosong"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.inputEmail.error = "Format email tidak valid"
                } else {
                    binding.inputEmail.error = null
                }
                validateInput()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.inputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInput()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnLogin.setOnClickListener{
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            loginViewModel.login(email, password).observe(this) { result ->
                if (result != null) {
                    when(result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            processLogin(result.data)
                            showLoading(false)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }


    }
    private fun processLogin(data: LoginResponse) {
        if (data.error) {
            Toast.makeText(this, data.message, Toast.LENGTH_LONG).show()
        } else {
            Preference.saveToken(data.loginResult.token, this)
            Log.d("test1",data.loginResult.token.toString())
            val intent = Intent(this@MainActivity, ListStoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.logoGambar.isInvisible = state
        binding.judulApp.isInvisible = state
        binding.pbLogin.isVisible = state
        binding.inputEmail.isInvisible = state
        binding.inputPassword.isInvisible = state
        binding.labelPassword.isInvisible = state
        binding.labelEmail.isInvisible = state
        binding.btnLogin.isInvisible = state
        binding.registerContainer.isInvisible = state
    }

    private fun validateInput() {
        val email = binding.inputEmail.text.toString()
        val password = binding.inputPassword.text.toString()
        val emailValid = !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val passwordValid = !password.isEmpty()
        val passwordValid2 = password.length >= 8

        binding.btnLogin.isEnabled = emailValid && passwordValid && passwordValid2
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logoGambar, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.judulApp, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.labelEmail, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.labelPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val messageDaftar = ObjectAnimator.ofFloat(binding.registerContainer, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login,
                messageDaftar
            )
            startDelay = 100
        }.start()
    }
}
