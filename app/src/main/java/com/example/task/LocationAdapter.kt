package com.example.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.namespace.R
import io.realm.RealmResults

class LocationAdapter(
    private val data: RealmResults<LocationData>,
    private val listener: LocationMainActivity
) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(locationData: LocationData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)
        return LocationViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = data[position]
        if (location != null) {
            holder.latitude.text = location.latitude.toString()
            holder.longitude.text = location.longitude.toString()
            holder.timestamp.text = location.timestamp.toString()
            holder.itemView.setOnClickListener {
                listener.onItemClick(location)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val latitude: TextView = itemView.findViewById(R.id.latitude)
        val longitude: TextView = itemView.findViewById(R.id.longitude)
        val timestamp: TextView = itemView.findViewById(R.id.timestamp)
    }
}