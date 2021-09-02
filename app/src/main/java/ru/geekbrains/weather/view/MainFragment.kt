package ru.geekbrains.weather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.geekbrains.weather.R
import ru.geekbrains.weather.databinding.FragmentMainBinding
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.viewmodel.AppState
import ru.geekbrains.weather.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val observer = Observer<AppState> { renderData(it) }
        mainViewModel.getAppState().observe(viewLifecycleOwner, observer)
        mainViewModel.getWeather()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Loading -> {
                showLoadingBar()
            }
            is AppState.Success -> {
                hideLoadingBar()
                Snackbar.make(requireView(), "Success", Snackbar.LENGTH_LONG).show()
                setData(appState.weatherData)

            }
            is AppState.Error -> {
                Snackbar
                    .make(requireView(), "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { mainViewModel.getWeather() }
                    .show()
            }
        }
    }

    private fun setData(weather: Weather) {
        with(binding) {
            cityName.text = weather.city.name
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weather.city.lat.toString(),
                weather.city.lon.toString(),
            )
            temperatureValue.text = weather.temperature.toString()
            temperatureFeelsLike.text = weather.feelsLike.toString()
        }
    }


    private fun showLoadingBar() {
        with(binding) {
            loadingBar.visibility = View.VISIBLE
            infoBox.visibility = View.GONE
        }
    }

    private fun hideLoadingBar() {
        with(binding) {
            loadingBar.visibility = View.GONE
            infoBox.visibility = View.VISIBLE
        }
    }

}