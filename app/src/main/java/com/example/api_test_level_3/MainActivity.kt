package com.example.api_test_level_3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.api_test_level_3.data.network.RetrofitClient
import com.example.api_test_level_3.ui.theme.Apitestlevel3Theme
import com.example.api_test_level_3.data.ApiResponse
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ComponentActivity() {
    private val TAG = "MiAPI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Apitestlevel3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun testApiCall(onResult: (String) -> Unit) {
        val call: Call<ApiResponse> = RetrofitClient.apiService.getUserBookings()
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()

                    // LOGS MEJORADOS - Información específica
                    Log.d(TAG, "=== RESPUESTA DE LA API ===")
                    Log.d(TAG, "Éxito: ${apiResponse?.success}")
                    Log.d(TAG, "Timestamp: ${apiResponse?.timestamp}")
                    Log.d(TAG, "Total de reservaciones: ${apiResponse?.data?.bookings?.size}")

                    // Log detallado de cada reservación
                    apiResponse?.data?.bookings?.forEachIndexed { index, booking ->
                        Log.d(TAG, "--- Reservación ${index + 1} ---")
                        Log.d(TAG, "ID: ${booking.bookingId}")
                        Log.d(TAG, "Fecha: ${booking.bookingDate}")
                        Log.d(TAG, "Hora: ${booking.startTime} - ${booking.endTime}")
                        Log.d(TAG, "Estado: ${booking.status}")
                        Log.d(TAG, "Servicio: ${booking.service.serviceName}")
                        Log.d(TAG, "Barbería: ${booking.barbershop.name}")
                        Log.d(TAG, "Barbero: ${booking.barber.barberName}")
                        Log.d(TAG, "Precio: $${booking.totalPrice}")
                        Log.d(TAG, "Notas: ${booking.notes ?: "Sin notas"}")
                    }

                    // Formatear para mostrar en pantalla
                    val displayText = formatBookingsForDisplay(apiResponse)
                    onResult(displayText)
                } else {
                    Log.e(TAG, "Error HTTP: ${response.code()}")
                    Log.e(TAG, response.errorBody()?.string() ?: "Error desconocido")
                    onResult("Error HTTP: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e(TAG, "Falla en la petición: ${t.message}", t)
                onResult("Falla en la petición: ${t.message}")
            }
        })
    }

    // Función para formatear las reservaciones para mostrar en pantalla
    private fun formatBookingsForDisplay(apiResponse: ApiResponse?): String {
        if (apiResponse == null) return "No hay datos"

        return buildString {
            append("=== MIS RESERVACIONES ===\n\n")
            append("Total: ${apiResponse.data.bookings.size} reservaciones\n")
            append("Timestamp: ${apiResponse.timestamp}\n\n")

            apiResponse.data.bookings.forEachIndexed { index, booking ->
                append("RESERVACIÓN ${index + 1}:\n")
                append("📅 Fecha: ${booking.bookingDate}\n")
                append("⏰ Hora: ${booking.startTime} - ${booking.endTime}\n")
                append("✅ Estado: ${booking.status}\n")
                append("💈 Servicio: ${booking.service.serviceName}\n")
                append("🏪 Barbería: ${booking.barbershop.name}\n")
                append("✂️ Barbero: ${booking.barber.barberName}\n")
                append("💰 Precio: $${booking.totalPrice}\n")
                append("📝 Notas: ${booking.notes ?: "Ninguna"}\n")
                append("🔗 ID: ${booking.bookingId}\n")
                append("-".repeat(40) + "\n\n")
            }
        }
    }

    // Formatea JSON crudo a string con identación
    private fun formatJson(json: String): String {
        return try {
            when {
                json.trim().startsWith("{") -> {
                    val jsonObject = JSONObject(json)
                    jsonObject.toString(4) // 4 espacios indentación
                }
                json.trim().startsWith("[") -> {
                    val jsonArray = JSONArray(json)
                    jsonArray.toString(4)
                }
                else -> json
            }
        } catch (e: JSONException) {
            json // si falla, retorna crudo
        }
    }

    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        var loading by remember { mutableStateOf(false) }
        var jsonResult by remember { mutableStateOf("") }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    loading = true
                    testApiCall { result ->
                        jsonResult = result
                        loading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (loading) "Cargando..." else "GET BOOKINGS")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = jsonResult)
            }
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        Apitestlevel3Theme {
            MainScreen()
        }
    }
}