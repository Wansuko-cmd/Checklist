package com.example.checklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class SampleData(
    val check: Boolean,
    val item: String
)

class Datas(){
    private val test1 = SampleData(true, "HomeWork")
    private val test2 = SampleData(true, "Watch")
    private val test3 = SampleData(false, "Laptop")
    private val test4 = SampleData(false,"Money")



    private val testList: MutableLiveData<MutableList<SampleData>> by lazy{
        MutableLiveData<MutableList<SampleData>>()
    }

    init{
        testList.value = mutableListOf(test1, test2, test3, test4)
    }


    fun insert(holder: ListViewHolder){
        val lst = testList.value
        if (lst != null){
            lst[holder.adapterPosition] = lst[holder.adapterPosition].copy(check = holder.check.isChecked)
            testList.value = lst
        }
    }

    fun getAll(): LiveData<MutableList<SampleData>> {
        return testList
    }

    companion object{
        fun getDatabase() : Datas{
            return Datas()
        }
    }
}
