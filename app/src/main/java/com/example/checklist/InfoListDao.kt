package com.example.checklist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InfoListDao {
    @Query("SELECT * FROM infoList_table")
    fun getAll(): LiveData<MutableList<InfoList>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(check: Boolean, item: String)

    @Query("DELETE FROM infoList_table")
    suspend fun deleteAll()
}