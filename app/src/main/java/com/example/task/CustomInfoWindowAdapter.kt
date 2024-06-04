package com.example.task

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.namespace.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(private val inflater: LayoutInflater) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view = inflater.inflate(R.layout.info_window, null)
        val title = view.findViewById<TextView>(R.id.title)
        val snippet = view.findViewById<TextView>(R.id.snippet)
        title.text = marker.title
        snippet.text = marker.snippet
        return view
    }
}