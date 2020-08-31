package com.example.checklist

data class SampleData(
    val check: Boolean,
    val item: String
)

class Datas(){
    private val test1 = SampleData(true, "HomeWork")
    private val test2 = SampleData(true, "Watch")
    private val test3 = SampleData(false, "Laptop")
    private val test4 = SampleData(false,"Money")

    private var testList = mutableListOf(test1, test2, test3, test4)

    fun insert(check: Boolean, item: String){
        testList.add(SampleData(check, item))
    }

    fun getAll():MutableList<SampleData>{
        return testList
    }
}
