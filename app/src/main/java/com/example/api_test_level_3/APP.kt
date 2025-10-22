package com.example.api_test_level_3

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class APP : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Redirigir inmediatamente al MainMenuActivity
        startActivity(Intent(this, MainMenuActivity::class.java))
        finish() // Cerrar esta actividad para que no quede en el stack
    }
}