package com.example.checklist

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InfoListDao {

    @Query("SELECT * FROM info_list_table")
    fun getAll(): LiveData<MutableList<InfoList>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(infoList: InfoList)
}