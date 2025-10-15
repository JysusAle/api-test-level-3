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
        val call: Call<ResponseBody> = RetrofitClient.apiService.getUserBookings()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    val rawJson = response.body()!!.string()
                    Log.d(TAG, "=== RESPUESTA CRUDA ===")
                    Log.d(TAG, rawJson)
                    val displayText = formatJson(rawJson)
                    onResult(displayText)
                } else {
                    Log.e(TAG, "Error HTTP: ${response.code()}")
                    Log.e(TAG, response.errorBody()?.string() ?: "Error desconocido")
                    onResult("Error HTTP: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "Falla en la petición: ${t.message}", t)
                onResult("Falla en la petición: ${t.message}")
            }
        })
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