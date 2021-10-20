package ru.geekbrains.weather.view.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import ru.geekbrains.weather.*
import ru.geekbrains.weather.databinding.FragmentMapsMainBinding
import ru.geekbrains.weather.domain.City
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.utils.FRAGMENT_CONTAINER
import ru.geekbrains.weather.utils.VERSION_PRO
import ru.geekbrains.weather.view.details.DetailsFragment

class WeatherMapFragment : Fragment() {

    private lateinit var map: GoogleMap

    private val markers: ArrayList<Marker?> = arrayListOf()

    private var _binding: FragmentMapsMainBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = WeatherMapFragment()
    }

    private lateinit var startingLocation: LatLng

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isZoomGesturesEnabled = true

        val isPermissionGranted =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED


        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        if (isPermissionGranted) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    startingLocation =
                        LatLng(location?.latitude ?: 55.0, location?.longitude ?: 37.0)
                    moveToPosition(LatLng(startingLocation.latitude, startingLocation.longitude))
                }
        }

        map.setOnMapLongClickListener { location ->
//            addMarker(location)

            val appVersion = getString(R.string.app_version)
            if (appVersion == VERSION_PRO) {

                requireActivity().supportFragmentManager.showFragment(
                    FRAGMENT_CONTAINER,
                    DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(
                            DetailsFragment.BUNDLE_EXTRA,
                            Weather(
                                City(
                                    //TODO: Вынести бизнес-логику туда, где ей место
                                    //TODO: Объединить этот код с запросом адреса у геокодера и выводить вместо "погода на карте" название города
                                    name = getString(R.string.t_weather_on_map),
                                    lat = location.latitude,
                                    lon = location.longitude
                                )
                            )
                        )
                    }),
                    "details"
                )

            } else {
                view?.showSnackBarWithResText(R.string.upgrade_to_pro_message)
            }


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
            if (address.size > 0) {
                val location = LatLng(address[0].latitude, address[0].longitude)
                map.clear()
                moveToPosition(location)
            } else view.showSnackBarWithResText(R.string.error_to_find_address)
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