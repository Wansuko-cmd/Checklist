package com.example.checklist

import androidx.lifecycle.LiveData

class InfoListRepository(private val datas: Datas) {
    val infoList: LiveData<MutableList<SampleData>> = datas.getAll()

    suspend fun insert(holder: ListViewHolder){
        datas.insert(holder)
    }
}