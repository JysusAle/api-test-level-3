package com.example.api_test_level_3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.api_test_level_3.data.models.CreateBookingRequest
import com.example.api_test_level_3.data.models.CreateBookingResponse
import com.example.api_test_level_3.data.network.RetrofitClient
import com.example.api_test_level_3.ui.theme.Apitestlevel3Theme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBookingActivity : ComponentActivity() {

    private val TAG = "CreateBooking"

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Apitestlevel3Theme {
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(title = { Text("Create Booking") })
                    }
                ) { innerPadding ->
                    CreateBookingScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    @Composable
    fun CreateBookingScreen(modifier: Modifier = Modifier) {
        var barbershopId by remember { mutableStateOf("") }
        var barberId by remember { mutableStateOf("") }
        var serviceId by remember { mutableStateOf("") }
        var userId by remember { mutableStateOf("") }
        var date by remember { mutableStateOf("") }
        var timeSlot by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }

        var loading by remember { mutableStateOf(false) }
        var resultText by remember { mutableStateOf("") }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = barbershopId,
                onValueChange = { barbershopId = it },
                label = { Text("Barbershop ID") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = barberId,
                onValueChange = { barberId = it },
                label = { Text("Barber ID") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = serviceId,
                onValueChange = { serviceId = it },
                label = { Text("Service ID") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = userId,
                onValueChange = { userId = it },
                label = { Text("User ID") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = timeSlot,
                onValueChange = { timeSlot = it },
                label = { Text("Time Slot") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    loading = true
                    resultText = ""
                    createBooking(
                        barbershopId,
                        barberId,
                        serviceId,
                        userId,
                        date,
                        timeSlot,
                        notes
                    ) { result ->
                        resultText = result
                        loading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (loading) "Creando..." else "CREATE BOOKING")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = resultText)
        }
    }

    private fun createBooking(
        barbershopId: String,
        barberId: String,
        serviceId: String,
        userId: String,
        date: String,
        timeSlot: String,
        notes: String?,
        onResult: (String) -> Unit
    ) {
        val bookingRequest = CreateBookingRequest(
            barbershopId = barbershopId,
            barberId = barberId,
            serviceId = serviceId,
            userId = userId,
            date = date,
            timeSlot = timeSlot,
            notes = notes
        )

        val call: Call<CreateBookingResponse> = RetrofitClient.apiService.createBooking(bookingRequest)
        call.enqueue(object : Callback<CreateBookingResponse> {
            override fun onResponse(
                call: Call<CreateBookingResponse>,
                response: Response<CreateBookingResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    val message = if (body.success) {
                        "Reserva creada correctamente: ${body.data?.bookingId ?: "N/A"}"
                    } else {
                        "Error: ${body.message}"
                    }
                    onResult(message)
                    Log.d(TAG, message)
                } else {
                    val error = "Error HTTP: ${response.code()}"
                    onResult(error)
                    Log.e(TAG, error)
                }
            }

            override fun onFailure(call: Call<CreateBookingResponse>, t: Throwable) {
                val error = "Fallo en la petición: ${t.message}"
                onResult(error)
                Log.e(TAG, error, t)
            }
        })
    }
}
