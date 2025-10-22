package com.example.api_test_level_3

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.api_test_level_3.data.network.ApiResponse
import com.example.api_test_level_3.data.network.Booking
import com.example.api_test_level_3.data.network.RetrofitClient
import com.example.api_test_level_3.adapters.BookingAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingsActivity : AppCompatActivity() {

    private lateinit var recyclerBookings: RecyclerView
    private lateinit var btnGet: Button
    private lateinit var adapter: BookingAdapter
    private val TAG = "BookingsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)  // Usamos Vista 1

        recyclerBookings = findViewById(R.id.recyclerBookings)
        btnGet = findViewById(R.id.btnGet)

        adapter = BookingAdapter(emptyList())
        recyclerBookings.layoutManager = LinearLayoutManager(this)
        recyclerBookings.adapter = adapter

        btnGet.setOnClickListener {
            fetchBookings()
        }
    }

    private fun fetchBookings() {
        val call: Call<ApiResponse> = RetrofitClient.apiService.getUserBookings()
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val bookings = response.body()!!.data.bookings
                    adapter.updateData(bookings)
                    Toast.makeText(this@BookingsActivity, "Reservas cargadas: ${bookings.size}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@BookingsActivity, "Error HTTP: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e(TAG, "Error al obtener bookings: ${t.message}")
                Toast.makeText(this@BookingsActivity, "Falla: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
