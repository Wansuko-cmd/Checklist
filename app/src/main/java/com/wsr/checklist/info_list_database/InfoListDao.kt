package com.wsr.checklist.info_list_database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InfoListDao {

    //データを取得するための関数
    @Query("SELECT * FROM info_list_table")
    fun getAll(): LiveData<MutableList<InfoList>>

    //データを挿入するための関数
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(infoList: InfoList)

    //チェックの有り無しのデータのみ変更するための関数
    @Query("UPDATE info_list_table SET 'check' = :Check WHERE id = :UUID")
    fun changeCheck(UUID: String, Check: Boolean)

    //全データを削除するための関数
    @Query("DELETE FROM info_list_table")
    fun deleteAll()
}