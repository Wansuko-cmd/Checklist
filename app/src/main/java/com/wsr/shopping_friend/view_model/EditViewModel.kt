package com.wsr.shopping_friend.view_model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsr.shopping_friend.info_list_database.InfoList
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import java.lang.Exception

//リストのデータを、並び順とかを加工して保存するためのViewModel
class EditViewModel() : ViewModel() {

    //InfoListをデータベースに共有せずに保持するための変数
    private val _list: MutableLiveData<MutableList<InfoList>> = MutableLiveData<MutableList<InfoList>>()

    //_listを使いやすくするための変数
    var list: MutableList<InfoList>
        get(){
            return (_list.value?.toMutableList() ?: mutableListOf())
        }
        set(value) {
            _list.postValue(value.sortedBy { it.number }.sortedBy { it.check } as MutableList<InfoList>)
        }

    //データの取得が出来るまで待機する関数
    suspend fun checkSetData(): Boolean {
        return try {
            Log.i("ok", "I'll get the title...")
            withTimeout(1000) {
                while (true) {
                    if (_list.value != null) break
                    Log.i("I wait for", "getting title...")
                    delay(100)
                }
                true
            }
        } catch (e: Exception) {
            Log.e("I missed getting title", "error: $e")
            false
        }
    }

    fun initializeList(list: MutableList<InfoList>){
        _list.postValue(list.sortedBy { it.number }.sortedBy { it.check }.toMutableList())
    }
}