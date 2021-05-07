package com.wsr.shopping_friend.share.view_model

import android.app.Application
import androidx.lifecycle.*
import com.wsr.shopping_friend.database.InfoList
import com.wsr.shopping_friend.database.InfoListDao
import com.wsr.shopping_friend.database.InfoListDatabase
import com.wsr.shopping_friend.share.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//データベースとやり取りするためのViewModel
class AppViewModel(application: Application) : AndroidViewModel(application) {

    //RepositoryとLiveDataのインスタンスの定義と初期化
    private val repository: AppRepository
    var infoList : LiveData<MutableList<InfoList>>
    init{
        val infoListDao: InfoListDao = InfoListDatabase.getDatabase(application).infoListDao()
        repository = AppRepository(infoListDao)
        infoList = repository.infoList
    }

    //データを一つずつ挿入するための関数
    suspend fun insert(infoList: InfoList) = viewModelScope.launch{
        repository.insert(infoList)
    }

    //データをアップデートするための関数　ただ今保留
    suspend fun update(infoList: MutableList<InfoList>) = viewModelScope.launch{
        repository.update(infoList)
    }

    //タイトルのみを変更するための関数
    suspend fun changeTitle(oldTitle: String, newTitle: String) = viewModelScope.launch{
        repository.changeTitle(oldTitle, newTitle)
    }

    //特定の要素を削除する関数
    suspend fun delete(infoList: InfoList) = viewModelScope.launch{
        repository.delete(infoList)
    }

    //リストに記載されている要素を削除する関数
    suspend fun deleteList(infoList: MutableList<InfoList>) = viewModelScope.launch{
        repository.deleteList(infoList)
    }

    //タイトル名が一致するものをすべて消すための関数
    suspend fun deleteWithTitle(title: String) = viewModelScope.launch{
        repository.deleteWithTitle(title)
    }
}