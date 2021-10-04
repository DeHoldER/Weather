package ru.geekbrains.weather

import android.app.Application
import androidx.room.Room
import ru.geekbrains.weather.room.HistoryDAO
import ru.geekbrains.weather.room.HistoryDB
import java.lang.IllegalStateException

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: MyApp? = null
        private var db: HistoryDB? = null
        private const val DB_NAME = "HistoryDB.db"

        fun getHistoryDAO(): HistoryDAO {
            if (db == null) {
                if (appInstance != null) {
                    db = Room.databaseBuilder(appInstance!!, HistoryDB::class.java, DB_NAME)
//                        .allowMainThreadQueries()
                        .build()
                } else {
                    throw IllegalStateException("appInstance==null")
                }
            }
            return db!!.historyDAO()
        }

    }
}