package com.wsr.checklist.info_list_database

import androidx.lifecycle.LiveData
import androidx.room.*

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

    //タイトルのみを変更するための関数
    @Query("UPDATE info_list_table SET 'title' = :Title WHERE id = :UUID")
    fun changeTitle(UUID: String, Title: String)

    //特定のエンティティを消すための関数
    @Delete
    fun delete(infoList: InfoList)

    //タイトル名が一致するものをすべて消すための関数
    @Query("Delete From info_list_table WHERE title = :Title")
    fun deleteWithTitle(Title: String)


    //全データを削除するための関数
    @Query("DELETE FROM info_list_table")
    fun deleteAll()
}