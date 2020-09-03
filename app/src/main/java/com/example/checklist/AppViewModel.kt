package com.example.checklist

import android.app.Application
import androidx.lifecycle.*
import kotlinx.android.synthetic.main.add_checklist.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    var infoList : LiveData<MutableList<SampleData>>
    val datas: Datas
    init{
        datas = Datas.getDatabase()
        infoList = datas.getAll()
    }


    fun insert(holder: ListViewHolder) = viewModelScope.launch(Dispatchers.IO){
        datas.insert(holder)
    }
}