package com.example.api_test_level_3

import android.os.Bundle
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
import kotlin.concurrent.thread
import kotlin.concurrent.timer

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
            shimmerContainer.visibility = View.VISIBLE
            shimmerContainer.startShimmer()
            recyclerBookings.visibility = View.GONE
            fetchBookings()
        }
    }

    private fun fetchBookings() {

        val call: Call<ApiResponse> = RetrofitClient.apiService.getUserBookings()
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                shimmerContainer.stopShimmer()
                shimmerContainer.visibility = View.GONE
                recyclerBookings.visibility = View.VISIBLE
                Thread.sleep(2500)

                if (response.isSuccessful && response.body() != null) {
                    val bookings = response.body()!!.data.bookings
                    adapter.updateData(bookings)
                } else {
                    Toast.makeText(
                        this@BookingsActivity,
                        //esperar tiempo (timer)
                        "Error HTTP: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                shimmerContainer.stopShimmer()
                shimmerContainer.visibility = View.GONE
                recyclerBookings.visibility = View.VISIBLE
                Toast.makeText(this@BookingsActivity, "Falla: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}