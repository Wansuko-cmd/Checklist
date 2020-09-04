package com.example.checklist

import android.app.Application
import android.icu.text.IDNA
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.add_checklist.view.*
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

    //チェックの有り無しのみを記録するための関数
    fun changeCheck(check: Boolean, item: String) = viewModelScope.launch(Dispatchers.IO){
        repository.changeCheck(check, item)
    }
}