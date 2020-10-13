package com.wsr.shopping_friend.info_list_database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface InfoListDao {

    //データを取得するための関数
    @Query("SELECT * FROM info_list_table")
    fun getAll(): LiveData<MutableList<InfoList>>

    @Query("SELECT * FROM info_list_table WHERE title = ''")
    fun getHelp(): List<InfoList>

    //データを挿入するための関数
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(infoList: InfoList)

    @Query("UPDATE info_list_table SET 'number' = :Number, 'check' = :Check, 'item' = :Item WHERE id = :UUID")
    fun update(UUID: String, Number: Int, Check: Boolean, Item: String)

    //チェックの有り無しのデータのみ変更するための関数
    @Query("UPDATE info_list_table SET 'check' = :Check WHERE id = :UUID")
    fun changeCheck(UUID: String, Check: Boolean)

    //タイトルのみを変更するための関数
    @Query("UPDATE info_list_table SET 'title' = :newTitle WHERE title = :oldTitle")
    fun changeTitle(oldTitle: String, newTitle: String)

    //特定のエンティティを消すための関数
    @Query("Delete FROM info_list_table WHERE id = :id")
    fun deleteWithId(id: String)

    //タイトル名が一致するものをすべて消すための関数
    @Query("Delete From info_list_table WHERE title = :Title")
    fun deleteWithTitle(Title: String)


    //全データを削除するための関数
    @Query("DELETE FROM info_list_table")
    fun deleteAll()
}