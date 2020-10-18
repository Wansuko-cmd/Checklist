package com.wsr.shopping_friend.info_list_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [InfoList::class], version = 1, exportSchema = true)
abstract class InfoListDatabase : RoomDatabase(){

    abstract fun infoListDao(): InfoListDao

    companion object{
        @Volatile
        private var INSTANCE: InfoListDatabase? = null

        //データベースのインスタンスを提供する関数
        //インスタンスがなければ形成し、あればそのインスタンスを渡す
        fun getDatabase(context: Context, scope: CoroutineScope): InfoListDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InfoListDatabase::class.java,
                    "info_list_database"
                )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}