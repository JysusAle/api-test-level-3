package com.example.api_test_level_3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.api_test_level_3.adapters.BookingAdapter
import com.example.api_test_level_3.data.network.ApiResponse
import com.example.api_test_level_3.data.network.RetrofitClient
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingsActivity : AppCompatActivity() {

    private lateinit var recyclerBookings: RecyclerView
    private lateinit var btnGet: Button
    private lateinit var adapter: BookingAdapter
    private lateinit var shimmerContainer: ShimmerFrameLayout

    private val TAG = "BookingsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)

        recyclerBookings = findViewById(R.id.recyclerBookings)
        btnGet = findViewById(R.id.btnGet)
        shimmerContainer = findViewById(R.id.shimmer_view_container)

        adapter = BookingAdapter(emptyList())
        recyclerBookings.layoutManager = LinearLayoutManager(this)
        recyclerBookings.adapter = adapter

        // Ocultar shimmer al inicio
        shimmerContainer.stopShimmer()
        shimmerContainer.visibility = View.GONE

        btnGet.setOnClickListener {
            showLoading()
            fetchBookings()
        }
    }

    private fun showLoading() {
        shimmerContainer.visibility = View.VISIBLE
        shimmerContainer.startShimmer()
        recyclerBookings.visibility = View.GONE
    }

    private fun hideLoading() {
        shimmerContainer.stopShimmer()
        shimmerContainer.visibility = View.GONE
        recyclerBookings.visibility = View.VISIBLE
    }

    private fun fetchBookings() {
        val call: Call<ApiResponse> = RetrofitClient.apiService.getUserBookings()

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                // Delay de 2.5 segundos ANTES de ocultar el shimmer
                Handler(Looper.getMainLooper()).postDelayed({
                    hideLoading()

                    if (response.isSuccessful && response.body() != null) {
                        val bookings = response.body()!!.data.bookings
                        adapter.updateData(bookings)
                    } else {
                        Toast.makeText(
                            this@BookingsActivity,
                            "Error HTTP: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, 2500) // 2500 milisegundos = 2.5 segundos
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                // También agregar delay aquí si quieres ver el shimmer en caso de error
                Handler(Looper.getMainLooper()).postDelayed({
                    hideLoading()
                    Toast.makeText(
                        this@BookingsActivity,
                        "Falla: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }, 2500)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        // Detener shimmer cuando la actividad no está visible
        shimmerContainer.stopShimmer()
    }
}