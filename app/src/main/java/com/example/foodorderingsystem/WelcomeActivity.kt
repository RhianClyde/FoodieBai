package com.example.foodorderingsystem

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)

        FirebaseApp.initializeApp(this)

        val btnGetStarted = findViewById<Button>(R.id.btn_get_started)
        val btnAlreadyAccount = findViewById<Button>(R.id.btn_already_account)

        // UI UPDATE v2: Micro-interaction button press feedback
        setupButtonTouchAnimation(btnGetStarted)
        setupButtonTouchAnimation(btnAlreadyAccount)

        btnGetStarted.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnAlreadyAccount.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    // UI UPDATE v2: Helper for subtle press scale animation
    private fun setupButtonTouchAnimation(button: Button) {
        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.animate().scaleX(0.97f).scaleY(0.97f).setDuration(80).start()
                }
                MotionEvent.ACTION_UP -> {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(80).start()
                    v.performClick()
                }
                MotionEvent.ACTION_CANCEL -> {
                    v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(80).start()
                }
            }
            true
        }
    }
}
