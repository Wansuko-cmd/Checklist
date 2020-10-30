package com.wsr.shopping_friend.view_model

import android.app.Application
import androidx.lifecycle.*
import com.wsr.shopping_friend.info_list_database.InfoList
import com.wsr.shopping_friend.info_list_database.InfoListDao
import com.wsr.shopping_friend.info_list_database.InfoListDatabase
import com.wsr.shopping_friend.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//データベースとやり取りするためのViewModel
class AppViewModel(application: Application) : AndroidViewModel(application) {

    //RepositoryとLiveDataのインスタンスの定義と初期化
    private val repository: AppRepository
    var infoList : LiveData<MutableList<InfoList>>
    init{
        val infoListDao: InfoListDao = InfoListDatabase.getDatabase(application,viewModelScope).infoListDao()
        repository = AppRepository(infoListDao)
        infoList = repository.infoList
    }

    //データを一つずつ挿入するための関数
    fun insert(infoList: InfoList) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(infoList)
    }
    //データをリストで挿入するための関数　ただ今保留
    /*fun insertList(infoList: MutableList<InfoList>) = viewModelScope.launch(Dispatchers.IO){
        repository.insertList(infoList)
    }*/

    //データをアップデートするための関数　ただ今保留
    fun update(infoList: MutableList<InfoList>) = viewModelScope.launch(Dispatchers.IO){
        repository.update(infoList)
    }

    //タイトルのみを変更するための関数
    fun changeTitle(oldTitle: String, newTitle: String) = viewModelScope.launch(Dispatchers.IO){
        repository.changeTitle(oldTitle, newTitle)
    }

    //特定の要素を削除する関数
    fun delete(infoList: InfoList) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(infoList)
    }

    //リストに記載されている要素を削除する関数
    fun deleteList(infoList: MutableList<InfoList>) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteList(infoList)
    }

    //タイトル名が一致するものをすべて消すための関数
    fun deleteWithTitle(title: String) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteWithTitle(title)
    }
}