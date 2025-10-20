package com.example.api_test_level_3.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.api_test_level_3.R
import com.example.api_test_level_3.data.network.Booking

class BookingAdapter(private val bookings: List<Booking>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    class BookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textBarbershop: TextView = view.findViewById(R.id.textBarbershop)
        val textService: TextView = view.findViewById(R.id.textService)
        val textDate: TextView = view.findViewById(R.id.textDate)
        val textStatus: TextView = view.findViewById(R.id.textStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.textBarbershop.text = booking.barbershop?.name ?: "Sin nombre"
        holder.textService.text = booking.service?.serviceName ?: "Sin servicio"
        holder.textDate.text = booking.bookingDate ?: "Sin fecha"
        holder.textStatus.text = booking.status ?: "Sin estado"
    }

    override fun getItemCount() = bookings.size
}
