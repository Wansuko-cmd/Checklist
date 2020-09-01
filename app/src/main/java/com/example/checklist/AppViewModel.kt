package com.example.checklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//viewModelScopeを使用するために、継承するクラスをViewModelからAndroidViewModelに変更
class AppViewModel(application: Application): AndroidViewModel(application) {

    //各種変数の初期化
    private val repository: AppRepository
    var infoList: LiveData<MutableList<InfoList>>

    init{
        val infoListDao = InfoListDatabase.getDatabase(application, viewModelScope).infoListDao()
        repository = AppRepository(infoListDao)
        infoList = repository.infoList
    }

    //Repository経由でInfoListDatabaseにデータを送り込むための関数
    fun insert(check: Boolean, item: String) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(check, item)
    }
}