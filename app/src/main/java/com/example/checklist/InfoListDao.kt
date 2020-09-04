package com.example.checklist

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
    @Query("UPDATE info_list_table SET 'check' = :Check WHERE item = :Item")
    fun changeCheck(Check: Boolean, Item: String)

    //全データを削除するための関数
    @Query("DELETE FROM info_list_table")
    fun deleteAll()
}