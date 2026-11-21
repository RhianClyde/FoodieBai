package com.example.foodorderingsystem

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.foodorderingsystem.data.UserManager
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {

    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)

        val btnBack = findViewById<View>(R.id.btn_back)
        val etIdentifier = findViewById<EditText>(R.id.et_identifier)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnTogglePassword = findViewById<View>(R.id.btn_toggle_password)
        val btnSignIn = findViewById<Button>(R.id.btn_sign_in)
        val tvSignUpLink = findViewById<TextView>(R.id.tv_sign_up_link)

        val linkText = "New to FoodieBai? Create an account"
        val spannable = SpannableString(linkText)
        val startIdx = linkText.indexOf("Create an account")
        if (startIdx != -1) {
            val endIdx = startIdx + "Create an account".length
            val greenColor = ContextCompat.getColor(this, R.color.primary_green_dark)
            spannable.setSpan(ForegroundColorSpan(greenColor), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(StyleSpan(Typeface.BOLD), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(UnderlineSpan(), startIdx, endIdx, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        tvSignUpLink.text = spannable

        btnBack.setOnClickListener { finish() }

        btnTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            etPassword.transformationMethod = if (isPasswordVisible) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            etPassword.setSelection(etPassword.text.length)
        }

        btnSignIn.setOnClickListener {
            val identifier = etIdentifier.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (identifier.isEmpty()) {
                etIdentifier.error = "Please enter your Email or username"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                etPassword.error = "Please enter password"
                return@setOnClickListener
            }

            btnSignIn.isEnabled = false
            val db = FirebaseFirestore.getInstance()

            // First check if identifier matches an email in Firestore
            db.collection("users")
                .whereEqualTo("email", identifier)
                .whereEqualTo("password", password)
                .get()
                .addOnSuccessListener { emailDocs ->
                    if (!emailDocs.isEmpty) {
                        val doc = emailDocs.documents[0]
                        val displayName = doc.getString("username") ?: identifier
                        val userEmail = doc.getString("email") ?: ""
                        completeLogin(displayName, userEmail)
                    } else {
                        // If no email matched, check if identifier matches a username
                        db.collection("users")
                            .whereEqualTo("username", identifier)
                            .whereEqualTo("password", password)
                            .get()
                            .addOnSuccessListener { usernameDocs ->
                                if (!usernameDocs.isEmpty) {
                                    val doc = usernameDocs.documents[0]
                                    val displayName = doc.getString("username") ?: identifier
                                    val userEmail = doc.getString("email") ?: ""
                                    completeLogin(displayName, userEmail)
                                } else {
                                    btnSignIn.isEnabled = true
                                    Toast.makeText(this@SignInActivity, "Invalid email/username or password", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener { e ->
                                btnSignIn.isEnabled = true
                                Toast.makeText(this@SignInActivity, "Firestore error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    btnSignIn.isEnabled = true
                    Toast.makeText(this@SignInActivity, "Firestore error: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        tvSignUpLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun completeLogin(displayName: String, email: String) {
        UserManager.login(displayName, email)
        Toast.makeText(this@SignInActivity, "Welcome back, $displayName!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this@SignInActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
