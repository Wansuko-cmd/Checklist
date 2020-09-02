package com.example.checklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.add_checklist.view.*

class AppViewModel(application: Application) : AndroidViewModel(application) {
    var infoList : LiveData<MutableList<SampleData>>
    val datas: Datas
    init{
        datas = Datas.getDatabase()
        infoList = datas.getAll()
    }


    fun insert(holder: ListViewHolder){
        datas.insert(holder)
        //infoList[holder.adapterPosition] = infoList[holder.adapterPosition].copy(check = holder.check.CheckBox.isChecked)
    }
}