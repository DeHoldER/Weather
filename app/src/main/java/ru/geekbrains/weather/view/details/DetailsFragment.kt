package ru.geekbrains.weather.view.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.weather.*
import ru.geekbrains.weather.databinding.FragmentDetailsBinding
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.utils.FOOTER_SVG_URL
import ru.geekbrains.weather.utils.FOOTER_URL
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
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // GLIDE
            picDetailsFooter.loadImageByUrl(FOOTER_URL)

            // COIL
            // (можно и обычные картинки подгружать через эти самописные экстеншны, но на всякий случай я их разделил)
            picDetailsFooterSVG.loadSVG(FOOTER_SVG_URL)
            icGifLoading.loadGIF(R.drawable.sun_animation)

//            picDetailsFooter.load(FOOTER_URL)
//            { placeholder(R.drawable.city_bg).error(R.mipmap.ic_world) } //эта хрень работает как-то
            // не совсем корректно, кроме того, coil почему-то игнорит аргумент match_parent,
            // поэтому закомментил, но оставил для проверки
        }



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
                    with(weather.weatherData) {
                        weatherCondition.text = condition
                        temperatureValue.text = temp
                        temperatureFeelsLike.text = feelsLike
                        if (customIcon != 0) {
                            conditionIcon.setBackgroundResource(customIcon)
                        } else conditionIcon.loadImageByUrl(nativeIconUrl)
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


