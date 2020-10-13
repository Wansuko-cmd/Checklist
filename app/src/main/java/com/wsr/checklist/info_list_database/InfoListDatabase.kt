package com.wsr.checklist.info_list_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [InfoList::class], version = 1, exportSchema = false)
abstract class InfoListDatabase : RoomDatabase(){

    abstract fun infoListDao(): InfoListDao

    private class InfoListDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        //データベースがインスタンス化した際に実行される処理
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
                for (i in 0..100){
                    val id = UUID.randomUUID().toString()
                    infoListDao.insert(InfoList(id, i, "Test", false, i.toString()))
                }
                infoListDao.insert(InfoList(UUID.randomUUID().toString(), 0,"code", false, "Alpha"))
                infoListDao.insert(InfoList(UUID.randomUUID().toString(), 1, "code",false, "Bravo"))
                infoListDao.insert(InfoList(UUID.randomUUID().toString(), 2, "code",false, "Charlie"))
                infoListDao.insert(InfoList(UUID.randomUUID().toString(), 3, "code",false, "Delta"))
                //infoListDao.deleteWithTitle("Te")*/

                /*infoListDao.insert(InfoList(false, "Watch"))
                infoListDao.insert(InfoList(false, "Laptop"))
                infoListDao.insert(InfoList(false, "Money"))*/
            }
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }

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
                    //.addCallback(InfoListDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}