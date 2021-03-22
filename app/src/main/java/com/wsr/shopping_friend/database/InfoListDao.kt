package com.wsr.shopping_friend.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface InfoListDao {

    //データを取得するための関数
    @Query("SELECT * FROM info_list_table")
    fun getAll(): LiveData<MutableList<InfoList>>

    //データを一つずつ挿入するための関数
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(infoList: InfoList)

    //データをアップデートするための関数
    @Update
    suspend fun update(infoList: MutableList<InfoList>): Int

    //タイトルのみを変更するための関数
    @Query("UPDATE info_list_table SET 'title' = :newTitle WHERE title = :oldTitle")
    fun changeTitle(oldTitle: String, newTitle: String)

    //特定の要素を削除する関数
    @Delete
    suspend fun delete(infoList: InfoList)

    //リストに記載されている要素を削除する関数
    @Delete
    suspend fun deleteList(infoList: MutableList<InfoList>)

    //タイトル名が一致するものをすべて消すための関数
    @Query("Delete From info_list_table WHERE title = :Title")
    suspend fun deleteWithTitle(Title: String)
}