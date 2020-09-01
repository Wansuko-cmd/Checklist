package com.example.checklist

import android.content.Context
import android.icu.text.IDNA
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = arrayOf(InfoList::class), version = 1, exportSchema = false)//将来的にexportSchemaに対応したい
public abstract class InfoListDatabase : RoomDatabase() {

    abstract fun infoListDao(): InfoListDao

    private class InfoListDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        //データーベースを開けた際に行うことを記述
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.infoListDao())
                }
            }
        }

        //内部のリストを常に同じものに保つ
        suspend fun populateDatabase(infoListDao: InfoListDao) {
            //infoListDao.deleteAll()
            infoListDao.insert(true, "HomeWord")
            infoListDao.insert(true, "Watch")
            infoListDao.insert(false, "Laptop")
            infoListDao.insert(false, "Money")
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: InfoListDatabase? = null

        //Databaseを作成して返り値に持つ。既に存在する場合はそれを返り値に持つ？
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): InfoListDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
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