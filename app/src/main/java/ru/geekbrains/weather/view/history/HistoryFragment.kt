package ru.geekbrains.weather.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.weather.R
import ru.geekbrains.weather.createAndShow
import ru.geekbrains.weather.databinding.FragmentHistoryBinding
import ru.geekbrains.weather.viewmodel.AppState
import ru.geekbrains.weather.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter()
    }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, {
            renderData(it)
        })
        viewModel.getAllHistory()
    }

    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.SuccessHistory -> {
                binding.historyFragmentRecyclerview.adapter = adapter
                adapter.setWeather(appState.weatherData)
                toggleLoader()
            }

            is AppState.Error -> {
                toggleLoader(true)
            }
            else -> {
                toggleLoader(true)
            }
        }
    }


    private fun toggleLoader(isLoading: Boolean = false) {
        with(binding) {
            if (!isLoading) {
                loadingLayout.visibility = View.GONE
            } else {
                loadingLayout.visibility = View.VISIBLE
            }
        }
    }
}