package com.wsr.checklist.view_model

import android.app.Application
import androidx.lifecycle.*
import com.wsr.checklist.info_list_database.InfoList
import com.wsr.checklist.info_list_database.InfoListDao
import com.wsr.checklist.info_list_database.InfoListDatabase
import com.wsr.checklist.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    //RepositoryとLiveDataのインスタンスの定義と初期化
    private val repository: AppRepository
    var infoList : LiveData<MutableList<InfoList>>
    init{
        val infoListDao: InfoListDao = InfoListDatabase.getDatabase(application,viewModelScope).infoListDao()
        repository = AppRepository(infoListDao)
        infoList = repository.infoList
    }

    //データベースに値を代入するための関数
    fun insert(infoList: InfoList) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(infoList)
    }

    //タイトルのみを変更するための関数
    fun changeTitle(id: String, title: String) = viewModelScope.launch(Dispatchers.IO){
        repository.changeTitle(id, title)
    }

    //チェックの有り無しのみを記録するための関数
    fun changeCheck(id: String, check: Boolean) = viewModelScope.launch(Dispatchers.IO){
        repository.changeCheck(id, check)
    }

    //指定したId名のものを削除する関数
    fun deleteWithId(id: String) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteWithId(id)
    }

    //タイトル名が一致するものをすべて消すための関数
    fun deleteWithTitle(title: String) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteWithTitle(title)
    }
}