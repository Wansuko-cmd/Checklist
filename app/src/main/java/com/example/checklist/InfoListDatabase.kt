package com.example.checklist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(InfoList::class), version = 1, exportSchema = false)
public abstract class InfoListDatabase : RoomDatabase(){

    abstract fun infoListDao(): InfoListDao

    companion object{

        //@Volatile
        private var INSTANCE: InfoListDatabase? = null

        fun getDatabase(context: Context): InfoListDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InfoListDatabase::class.java,
                    "info_list_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}