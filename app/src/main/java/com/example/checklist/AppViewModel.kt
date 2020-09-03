package com.example.checklist

import android.app.Application
import android.icu.text.IDNA
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.add_checklist.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository
    var infoList : LiveData<MutableList<InfoList>>
    init{
        val db = InfoListDatabase.getDatabase(application, viewModelScope)
        //val infoListDao: InfoListDao = InfoListDatabase.getDatabase(application).infoListDao()
        val infoListDao: InfoListDao = db.infoListDao()
        repository = AppRepository(infoListDao)
        infoList = repository.infoList
    }

    fun insert(infoList: InfoList) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(infoList)
    }

    fun changeCheck(check: Boolean, item: String) = viewModelScope.launch(Dispatchers.IO){
        repository.changeCheck(check, item)
    }
}