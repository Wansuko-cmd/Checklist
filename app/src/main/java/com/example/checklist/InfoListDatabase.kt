package com.example.checklist

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(InfoList::class), version = 1, exportSchema = false)
public abstract class InfoListDatabase : RoomDatabase(){

    abstract fun infoListDao(): InfoListDao

    private class InfoListDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase){
            super.onOpen(db)
            INSTANCE?.let{ database ->
                scope.launch{
                    populateDatabase(database.infoListDao())
                }
            }
        }

        fun populateDatabase(infoListDao: InfoListDao){
            Completable.fromAction{
                infoListDao.deleteAll()
                infoListDao.insert(InfoList(false, "Homework"))
                Thread.sleep(1000)
                infoListDao.insert(InfoList(false, "Watch"))
                infoListDao.insert(InfoList(false, "Laptop"))
                infoListDao.insert(InfoList(false, "Money"))
            }
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }

    companion object{

        @Volatile
        private var INSTANCE: InfoListDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): InfoListDatabase{
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
                    .addCallback(InfoListDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}