package com.example.checklist

import android.app.Application
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.add_checklist.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InfoListRepository
    var infoList : LiveData<MutableList<SampleData>>
    init{
        val datas: Datas = Datas.getDatabase()
        repository = InfoListRepository(datas)
        infoList = repository.infoList
    }


    fun insert(holder: ListViewHolder) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(holder)
    }
}