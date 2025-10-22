package com.example.api_test_level_3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.api_test_level_3.data.models.*
import com.example.api_test_level_3.data.network.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBookingActivity : ComponentActivity() {
    private lateinit var inputBarbershop: EditText
    private lateinit var inputBarberId: EditText
    private lateinit var inputUserId: EditText
    private lateinit var inputBookingDate: EditText
    private lateinit var inputStartTime: EditText
    private lateinit var inputEndTime: EditText
    private lateinit var inputNotes: EditText
    private lateinit var btnCreateBooking: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_booking)

        // Inicializar vistas usando findViewById
        inputBarbershop = findViewById(R.id.inputBarbershop)
        inputBarberId = findViewById(R.id.inputBarberId)
        inputUserId = findViewById(R.id.inputUserId)
        inputBookingDate = findViewById(R.id.inputBookingDate)
        inputStartTime = findViewById(R.id.inputStartTime)
        inputEndTime = findViewById(R.id.inputEndTime)
        inputNotes = findViewById(R.id.inputNotes)
        btnCreateBooking = findViewById(R.id.btnCreateBooking)

        btnCreateBooking.setOnClickListener {
            val barbershopId = inputBarbershop.text.toString().trim()
            val barberId = inputBarberId.text.toString().trim()
            val userId = inputUserId.text.toString().trim()
            val bookingDate = inputBookingDate.text.toString().trim()
            val startTime = inputStartTime.text.toString().trim()
            val endTime = inputEndTime.text.toString().trim()
            val notes = inputNotes.text.toString().trim()

            if (barbershopId.isEmpty() || userId.isEmpty() || bookingDate.isEmpty()) {
                Toast.makeText(this, "Rellena los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear y enviar la reserva
            createBooking(
                barbershopId = barbershopId,
                barberId = barberId,
                userId = userId,
                bookingDate = bookingDate,
                startTime = startTime,
                endTime = endTime,
                notes = notes.ifEmpty { null }
            )
        }
    }

    private fun createBooking(
        barbershopId: String,
        barberId: String,
        userId: String,
        bookingDate: String,
        startTime: String,
        endTime: String,
        notes: String?
    ) {
        val request = CreateBookingRequest(
            barbershopId = barbershopId,
            barberId = barberId,
            serviceId = "", // Ajusta según necesites
            userId = userId,
            date = bookingDate,
            timeSlot = "$startTime - $endTime",
            notes = notes
        )

        RetrofitClient.apiService.createBooking(request).enqueue(object : Callback<CreateBookingResponse> {
            override fun onResponse(call: Call<CreateBookingResponse>, response: Response<CreateBookingResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateBookingActivity, "Reserva creada con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CreateBookingActivity, "Error al crear la reserva", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CreateBookingResponse>, t: Throwable) {
                Toast.makeText(this@CreateBookingActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}