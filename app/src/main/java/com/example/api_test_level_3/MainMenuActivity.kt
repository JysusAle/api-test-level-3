package com.example.api_test_level_3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.Toast



class MainMenuActivity : ComponentActivity() {

    private val TAG = "MainMenuActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, "FLAG: onCreate ejecutado", Toast.LENGTH_SHORT).show()

        setContentView(R.layout.activity_main)
        // Obtener referencias a los botones usando findViewById
        val btnGet = findViewById<Button>(R.id.btnGet)
        val btnPost = findViewById<Button>(R.id.btnPost)

        // Configurar listeners para los botones
        btnGet.setOnClickListener {
            startActivity(Intent(this, BookingsActivity::class.java))
        }

        btnPost.setOnClickListener {
            startActivity(Intent(this, CreateBookingActivity::class.java))
        }
    }
}
