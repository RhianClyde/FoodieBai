package com.example.foodorderingsystem

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.Tasks
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private var isPasswordVisible = false

    private var usernameJob: Job? = null
    private var emailJob: Job? = null

    private var isUsernameAvailable = true
    private var isEmailAvailable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        try {
            FirebaseApp.initializeApp(this)
        } catch (e: Exception) {
            // Already initialized
        }

        val btnBack = findViewById<View>(R.id.btn_back)
        val llUsername = findViewById<LinearLayout>(R.id.ll_username)
        val etUsername = findViewById<EditText>(R.id.et_username)
        val tvUsernameError = findViewById<TextView>(R.id.tv_username_error)

        val llEmail = findViewById<LinearLayout>(R.id.ll_email)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val tvEmailError = findViewById<TextView>(R.id.tv_email_error)

        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnTogglePassword = findViewById<View>(R.id.btn_toggle_password)
        val btnCreateAccount = findViewById<Button>(R.id.btn_create_account)
        val tvSignInLink = findViewById<TextView>(R.id.tv_sign_up_link)

        val linkText = "Already have an account? Sign in"
        val spannable = SpannableString(linkText)
        val startIdx = linkText.indexOf("Sign in")
        if (startIdx != -1) {
            val endIdx = startIdx + "Sign in".length
            val greenColor = ContextCompat.getColor(this, R.color.primary_green_dark)
            spannable.setSpan(ForegroundColorSpan(greenColor), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(StyleSpan(Typeface.BOLD), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(UnderlineSpan(), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        tvSignInLink.text = spannable

        btnBack.setOnClickListener { finish() }

        // LIVE AS-YOU-TYPE USERNAME VALIDATION (Coroutines + Debounce)
        etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                usernameJob?.cancel()
            }
            override fun afterTextChanged(s: Editable?) {
                val username = s.toString().trim()
                if (username.isEmpty()) {
                    llUsername.setBackgroundResource(R.drawable.bg_input_field)
                    tvUsernameError.visibility = View.GONE
                    isUsernameAvailable = true
                    return
                }

                usernameJob = lifecycleScope.launch {
                    delay(400) // 400ms debounce
                    val isTaken = withContext(Dispatchers.IO) {
                        try {
                            val task = FirebaseFirestore.getInstance().collection("users")
                                .whereEqualTo("username", username)
                                .get()
                            val query = Tasks.await(task)
                            !query.isEmpty
                        } catch (e: Exception) {
                            false
                        }
                    }

                    if (isTaken) {
                        // RED BOX + Error message
                        llUsername.setBackgroundResource(R.drawable.bg_input_field_error)
                        tvUsernameError.text = "Username is already taken"
                        tvUsernameError.visibility = View.VISIBLE
                        isUsernameAvailable = false
                    } else {
                        // GREEN BOX
                        llUsername.setBackgroundResource(R.drawable.bg_input_field_success)
                        tvUsernameError.visibility = View.GONE
                        isUsernameAvailable = true
                    }
                }
            }
        })

        // LIVE AS-YOU-TYPE EMAIL VALIDATION (Coroutines + Debounce)
        etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailJob?.cancel()
            }
            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().trim()
                if (email.isEmpty()) {
                    llEmail.setBackgroundResource(R.drawable.bg_input_field)
                    tvEmailError.visibility = View.GONE
                    isEmailAvailable = true
                    return
                }

                emailJob = lifecycleScope.launch {
                    delay(400) // 400ms debounce
                    val isTaken = withContext(Dispatchers.IO) {
                        try {
                            val task = FirebaseFirestore.getInstance().collection("users")
                                .whereEqualTo("email", email)
                                .get()
                            val query = Tasks.await(task)
                            !query.isEmpty
                        } catch (e: Exception) {
                            false
                        }
                    }

                    if (isTaken) {
                        // RED BOX + Error message
                        llEmail.setBackgroundResource(R.drawable.bg_input_field_error)
                        tvEmailError.text = "Email address is already taken"
                        tvEmailError.visibility = View.VISIBLE
                        isEmailAvailable = false
                    } else {
                        // GREEN BOX
                        llEmail.setBackgroundResource(R.drawable.bg_input_field_success)
                        tvEmailError.visibility = View.GONE
                        isEmailAvailable = true
                    }
                }
            }
        })

        btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            etPassword.transformationMethod = if (isPasswordVisible) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            etPassword.setSelection(etPassword.text.length)
        }

        btnCreateAccount.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty()) {
                llUsername.setBackgroundResource(R.drawable.bg_input_field_error)
                tvUsernameError.text = "Please enter a username"
                tvUsernameError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                llEmail.setBackgroundResource(R.drawable.bg_input_field_error)
                tvEmailError.text = "Please enter an email"
                tvEmailError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            if (password.length < 6) {
                etPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            if (!isUsernameAvailable) {
                Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isEmailAvailable) {
                Toast.makeText(this, "Email address is already taken", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnCreateAccount.isEnabled = false
            val db = FirebaseFirestore.getInstance()

            val userMap = hashMapOf(
                "username" to username,
                "email" to email,
                "password" to password,
                "createdAt" to FieldValue.serverTimestamp()
            )

            db.collection("users")
                .add(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this@SignUpActivity, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    btnCreateAccount.isEnabled = true
                    Toast.makeText(this@SignUpActivity, "Error saving account: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        tvSignInLink.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
