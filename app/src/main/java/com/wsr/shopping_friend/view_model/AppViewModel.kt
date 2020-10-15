package com.wsr.shopping_friend.view_model

import android.app.Application
import androidx.lifecycle.*
import com.wsr.shopping_friend.info_list_database.InfoList
import com.wsr.shopping_friend.info_list_database.InfoListDao
import com.wsr.shopping_friend.info_list_database.InfoListDatabase
import com.wsr.shopping_friend.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class AppViewModel(application: Application) : AndroidViewModel(application) {

    //RepositoryとLiveDataのインスタンスの定義と初期化
    private val repository: AppRepository
    var infoList : LiveData<MutableList<InfoList>>
    init{
        val infoListDao: InfoListDao = InfoListDatabase.getDatabase(application,viewModelScope).infoListDao()
        repository = AppRepository(infoListDao)
        infoList = repository.infoList

        //テストコード
        //deleteAll()
        if(false){
            //deleteAll()
            val testList: MutableList<InfoList> = mutableListOf()
            for (i in 1..10){
                testList.add(InfoList(UUID.randomUUID().toString(), i, "Test", false, i.toString()))
            }
            val code = mutableListOf<String>("Alpha", "Bravo", "Charlie", "Delta")
            for((count, i) in code.withIndex()){
                testList.add(InfoList(UUID.randomUUID().toString(), count, "code", false, i))
            }
            insert(testList)
        }
    }

    //データベースに値を代入するための関数
    fun insert(infoList: MutableList<InfoList>) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(infoList)
    }

    //ヘルプの情報を取得するための関数
    fun getHelp(): List<InfoList>{
        var list: List<InfoList> = emptyList()
        runBlocking {
            val job = viewModelScope.launch(Dispatchers.IO){
                list = repository.getHelp()
            }
            job.join()
        }
        return list
    }

    //タイトルのみを変更するための関数
    fun changeTitle(oldTitle: String, newTitle: String) = viewModelScope.launch(Dispatchers.IO){
        repository.changeTitle(oldTitle, newTitle)
    }

    //特定の要素を削除する関数
    fun delete(infoList: InfoList) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(infoList)
    }

    //リストに記載されている要素を削除する関数
    fun deleteList(infoList: MutableList<InfoList>) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteList(infoList)
    }

    //タイトル名が一致するものをすべて消すための関数
    fun deleteWithTitle(title: String) = viewModelScope.launch(Dispatchers.IO){
        repository.deleteWithTitle(title)
    }

    //要素をすべて削除する関数
    private fun deleteAll() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAll()
    }
}