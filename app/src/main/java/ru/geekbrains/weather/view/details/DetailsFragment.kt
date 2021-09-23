package ru.geekbrains.weather.view.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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

    // -------------- ПОСЛЕ ПРОВЕРКИ УДАЛИТЬ --------------- //
    val localWeather: Weather by lazy {
        (arguments?.getParcelable(BUNDLE_EXTRA)) ?: Weather()
    }
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { it ->
                val weatherDTO = it.getParcelableExtra<WeatherDTO>(DETAILS_LOAD_RESULT_EXTRA)
                if (weatherDTO != null) {
                    showWeather(weatherDTO)
                    toggleLoader()
                } else {
                    view?.createAndShow("Fail", "Reload", {
                        intent.getParcelableExtra<WeatherDTO>(DETAILS_LOAD_RESULT_EXTRA)
                            ?.let { it1 ->
                                showWeather(it1)
                            }
                    })
                }
            }

        }
    }

    private fun showWeather(weatherDTO: WeatherDTO) {

        with(binding) {
            cityName.text = localWeather.city.name
            cityCoordinates.text = "lat ${localWeather.city.lat}\n lon ${localWeather.city.lon}"
            temperatureValue.text = weatherDTO.main?.temp.toString()
            temperatureFeelsLike.text = "${weatherDTO.main?.feels_like}"
            weatherCondition.text = "${weatherDTO.weather?.get(0)?.description}"
        }
    }
    // -------------- ПОСЛЕ ПРОВЕРКИ УДАЛИТЬ --------------- //

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

        // -------------- ПОСЛЕ ПРОВЕРКИ УДАЛИТЬ --------------- //
        val intent = Intent(requireActivity(), DetailsService::class.java)
        intent.putExtra(LATITUDE_EXTRA, localWeather.city.lat)
        intent.putExtra(LONGITUDE_EXTRA, localWeather.city.lon)
        requireActivity().startService(intent)
        LocalBroadcastManager.getInstance(requireActivity())
            .registerReceiver(receiver, IntentFilter(DETAILS_INTENT_FILTER))
        // -------------- ПОСЛЕ ПРОВЕРКИ УДАЛИТЬ --------------- //
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
                    toggleLoader()
                    cityName.text = weatherBundle.city.name
                    cityCoordinates.text = String.format(
                        getString(R.string.city_coordinates),
                        weatherBundle.city.lat.toString(),
                        weatherBundle.city.lon.toString()
                    )
                    weatherCondition.text = weather.weatherDTO.weather?.get(0)?.description
                    weather.weatherDTO.weather?.get(0)?.id?.let { setConditionPicture(it) }
                    temperatureValue.text = weather.weatherDTO.main?.temp.toString()
                    temperatureFeelsLike.text = weather.weatherDTO.main?.feels_like.toString()

                }

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

    // пока так, в дальнейшем планирую по всем состояниям показывать кастомные картинки
    private fun setConditionPicture(conditionId: Int) {
        with(binding.conditionIcon) {
            conditionMap[conditionId]?.let {
                setBackgroundResource(it)
            }
        }
    }
}


val conditionMap = mapOf<Int, Int>(
    505 to R.drawable.ic_rainy,
    800 to R.drawable.ic_clear,
    801 to R.drawable.ic_cloudy,
    804 to R.drawable.ic_overcast,
)