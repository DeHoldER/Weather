package ru.geekbrains.weather.lesson10

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import ru.geekbrains.weather.R
import ru.geekbrains.weather.databinding.FragmentMapsBinding
import ru.geekbrains.weather.databinding.FragmentMapsMainBinding

class MapsFragment : Fragment() {

    lateinit var map: GoogleMap

    private val markers: ArrayList<Marker?> = arrayListOf()

    private var _binding: FragmentMapsMainBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MapsFragment()
    }

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val startingLocation = LatLng(55.0, 37.0)
        googleMap.addMarker(MarkerOptions().position(startingLocation).title("Marker in Moscow"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(startingLocation))

        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
            }
        }

        map.setOnMapLongClickListener { location ->
            addMarker(location)
            drawLine()
        }

    }

    private fun addMarker(location: LatLng) {
        markers.add(
            map.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin))
                    .position(location)
                    .title("Marker ${markers.size + 1}")
            )
        )
    }

    private fun drawLine() {
        val lastMarker = markers.size - 1
        if (lastMarker > 0) {
            val startMarker = markers[lastMarker - 1]?.position
            val endMarker = markers[lastMarker]?.position

            map.addPolyline(
                PolylineOptions()
                    .add(startMarker, endMarker)
                    .color(Color.RED)
                    .width(15f)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding.buttonSearch.setOnClickListener {
            val geocoder = Geocoder(requireContext())
            val addressRow = binding.searchAddress.text.toString()
            val address = geocoder.getFromLocationName(addressRow, 1)
            val location = LatLng(address[0].latitude, address[0].longitude)
            map.clear()
            moveToPosition(location)
        }
    }

    private fun moveToPosition(location: LatLng) {
        map.addMarker(MarkerOptions().position(location).title("Marker in $location"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}