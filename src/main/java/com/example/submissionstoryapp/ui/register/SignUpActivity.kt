package com.example.submissionstoryapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.submissionstoryapp.R
import com.example.submissionstoryapp.databinding.ActivitySignUpBinding
import com.example.submissionstoryapp.response.daftar.DaftarResponse
import com.example.submissionstoryapp.ui.login.MainActivity
import com.example.submissionstoryapp.utils.ViewModelFactory
import com.example.submissionstoryapp.response.repository.Result

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModels{
        ViewModelFactory(this)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

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

        binding.inputNama.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val nama = binding.inputNama.text.toString()
                if (nama.isEmpty()) {
                    binding.inputNama.error = "Input tidak boleh kosong"
                } else {
                    binding.inputNama.error = null
                }
                validateInput()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.inputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = binding.inputPassword.text.toString()
                if (password.isEmpty()) {
                    binding.inputPassword.error = "Password tidak boleh kosong"
                }
                else {
                    binding.inputPassword.error = null
                }
                validateInput()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.inputConfirmpassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = binding.inputPassword.text.toString()
                val Confirmpassword = binding.inputConfirmpassword.text.toString()
                if (Confirmpassword.isEmpty() || Confirmpassword != password) {
                    binding.inputConfirmpassword.error = "Password tidak sesuai"
                } else {
                    binding.inputConfirmpassword.error = null
                }
                validateInput()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.inputPassword.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.inputPassword.right - binding.inputPassword.compoundDrawables[2].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.inputConfirmpassword.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.inputConfirmpassword.right - binding.inputConfirmpassword.compoundDrawables[2].bounds.width())) {
                    toggleConfirmPasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.btnRegister.setOnClickListener{ it ->
            val name = binding.inputNama.text.toString()
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            signUpViewModel.signUp(name, email, password).observe(this) {
                if (it != null) {
                    when(it) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            processSignUp(it.data)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        binding.btnLogin.setOnClickListener{
            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun processSignUp(data: DaftarResponse) {
        if (data.error) {
            Toast.makeText(this, "Gagal Sign Up", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Sign Up berhasil, silahkan login!", Toast.LENGTH_LONG).show()
            val intent = Intent(this@SignUpActivity, MainActivity::class.java)
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
        binding.inputNama.isInvisible = state
        binding.labelNama.isInvisible = state
        binding.labelPassword.isInvisible = state
        binding.labelEmail.isInvisible = state
        binding.labelConfirmpassword.isInvisible = state
        binding.inputConfirmpassword.isInvisible = state
        binding.btnRegister.isInvisible = state
        binding.containerLogin.isInvisible = state
    }
    private fun togglePasswordVisibility() {
        val start = ResourcesCompat.getDrawable(resources, R.drawable.password, null)
        val endVisible = ResourcesCompat.getDrawable(resources, R.drawable.lihat_password, null)
        val endHidden = ResourcesCompat.getDrawable(resources, R.drawable.sembunyikan, null)

        if (binding.inputPassword.inputType == android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            binding.inputPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.inputPassword.setCompoundDrawablesWithIntrinsicBounds(start, null, endHidden, null)
        } else {
            binding.inputPassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.inputPassword.setCompoundDrawablesWithIntrinsicBounds(start, null, endVisible, null)
        }

        // Move the cursor to the end of the input
        binding.inputPassword.setSelection(binding.inputPassword.text!!.length)
    }
    private fun toggleConfirmPasswordVisibility() {
        val start = ResourcesCompat.getDrawable(resources, R.drawable.password, null)
        val endVisible = ResourcesCompat.getDrawable(resources, R.drawable.lihat_password, null)
        val endHidden = ResourcesCompat.getDrawable(resources, R.drawable.sembunyikan, null)

        if (binding.inputConfirmpassword.inputType == android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            binding.inputConfirmpassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.inputConfirmpassword.setCompoundDrawablesWithIntrinsicBounds(start, null, endHidden, null)
        } else {
            binding.inputConfirmpassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.inputConfirmpassword.setCompoundDrawablesWithIntrinsicBounds(start, null, endVisible, null)
        }

        // Move the cursor to the end of the input
        binding.inputConfirmpassword.setSelection(binding.inputConfirmpassword.text!!.length)
    }

    private fun validateInput() {
        val email = binding.inputEmail.text.toString()
        val nama = binding.inputNama.text.toString()
        val password = binding.inputPassword.text.toString()
        val confirmPw = binding.inputConfirmpassword.text.toString()

        val emailValid = !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val passwordValid = !password.isEmpty()
        val passwordValid2 = password.length >= 8
        val namaValid = !nama.isEmpty()
        val passwordSame = password == confirmPw

        binding.btnRegister.isEnabled = emailValid && passwordValid && namaValid && passwordSame && passwordValid2
    }

    private fun playAnimation() {
        val gambar = ObjectAnimator.ofFloat(binding.logoGambar, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.judulApp, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.labelEmail, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.inputEmail, View.ALPHA, 1f).setDuration(500)
        val namaTextView =
            ObjectAnimator.ofFloat(binding.labelNama, View.ALPHA, 1f).setDuration(500)
        val namaEditTextLayout =
            ObjectAnimator.ofFloat(binding.inputNama, View.ALPHA, 1f).setDuration(500)

        val passwordTextView =
            ObjectAnimator.ofFloat(binding.labelPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.inputPassword, View.ALPHA, 1f).setDuration(500)
        val confirmpasswordTextView =
            ObjectAnimator.ofFloat(binding.labelConfirmpassword, View.ALPHA, 1f).setDuration(500)
        val confirmpasswordEditTextLayout =
            ObjectAnimator.ofFloat(binding.inputConfirmpassword, View.ALPHA, 1f).setDuration(500)

        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val messageLogin = ObjectAnimator.ofFloat(binding.containerLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                gambar,
                title,
                emailTextView,
                emailEditTextLayout,
                namaTextView,
                namaEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                confirmpasswordTextView,
                confirmpasswordEditTextLayout,
                register,
                messageLogin
            )
            startDelay = 100
        }.start()
    }
}