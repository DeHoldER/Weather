package ru.geekbrains.weather.contentProvider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.weather.R
import ru.geekbrains.weather.createAndShow
import ru.geekbrains.weather.databinding.FragmentContentProviderBinding
import ru.geekbrains.weather.databinding.FragmentHistoryBinding
import ru.geekbrains.weather.view.history.HistoryAdapter
import ru.geekbrains.weather.viewmodel.AppState
import ru.geekbrains.weather.viewmodel.HistoryViewModel

class ContentProviderFragment : Fragment() {

    private var _binding: FragmentContentProviderBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ContentProviderFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}