package ru.geekbrains.weather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.geekbrains.weather.R
import ru.geekbrains.weather.view.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance())
                .commit()
        }
    }
}