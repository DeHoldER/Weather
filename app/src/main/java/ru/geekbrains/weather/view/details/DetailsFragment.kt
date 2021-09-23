package ru.geekbrains.weather.view.details

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.weather.R
import ru.geekbrains.weather.createAndShow
import ru.geekbrains.weather.databinding.FragmentDetailsBinding
import ru.geekbrains.weather.domain.*
import ru.geekbrains.weather.viewmodel.AppState
import ru.geekbrains.weather.viewmodel.DetailsViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var weatherBundle: Weather

    private val viewModel: DetailsViewModel by lazy { ViewModelProvider(this).get(DetailsViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        const val BUNDLE_EXTRA = "BUNDLE_EXTRA"

        fun newInstance(bundle: Bundle): DetailsFragment =
            DetailsFragment().apply { arguments = bundle }
    }

    override fun onDestroy() {
        super.onDestroy()
//        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments.let { it?.getParcelable<Weather>(BUNDLE_EXTRA) ?: Weather() }
            .also { weatherBundle = it }
        with(viewModel) {
            getAppState().observe(viewLifecycleOwner, Observer {
                displayWeather(it)
            })
            updateWeather(weatherBundle)
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun displayWeather(weather: AppState) {
        when (weather) {
            is AppState.DetailSuccess -> {
                with(binding) {
                    cityName.text = weatherBundle.city.name
                    cityCoordinates.text = String.format(
                        getString(R.string.city_coordinates),
                        weatherBundle.city.lat.toString(),
                        weatherBundle.city.lon.toString()
                    )
                    with(weather.weather) {
                        weatherCondition.text = condition
                        conditionIcon.setBackgroundResource(icon)
                        temperatureValue.text = temp
                        temperatureFeelsLike.text = feelsLike
                    }
                }
                toggleLoader()
            }
            is AppState.Error -> {
                toggleLoader(true)
                with(binding) {
                    errorMessage.text =
                        String.format(getString(R.string.error_message), weather.error)
                    view?.createAndShow(
                        "Error",
                        "Reload",
                        { viewModel.updateWeather(weatherBundle) })
                }
            }
            else -> {
                toggleLoader(true)
            }
        }
    }


    private fun toggleLoader(isLoading: Boolean = false) {
        with(binding) {
            if (!isLoading) {
                infoBox.visibility = View.VISIBLE
                loadingBar.visibility = View.GONE
            } else {
                infoBox.visibility = View.GONE
                loadingBar.visibility = View.VISIBLE
            }
        }
    }
}