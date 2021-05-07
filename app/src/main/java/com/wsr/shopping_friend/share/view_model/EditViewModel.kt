package com.wsr.shopping_friend.share.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsr.shopping_friend.database.InfoList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.lang.Exception

//リストのデータを、並び順とかを加工して保存するためのViewModel
class EditViewModel : ViewModel() {

    //InfoListをデータベースに共有せずに保持するための変数
    private val _list: MutableLiveData<MutableList<InfoList>> = MutableLiveData<MutableList<InfoList>>()

    //_listを使いやすくするための変数
    var list: MutableList<InfoList>
        get(){
            return (_list.value?.toMutableList() ?: mutableListOf())
        }
        set(value) {
            _list.postValue(value.sortedWith(infoListComparator).toMutableList())
        }

    //infoListを並べる際に用いるコンパレーター
    //チェックのついているものを先頭にして、その中で番号順に並ぶ
    val infoListComparator : Comparator<InfoList> = compareBy(InfoList::check, InfoList::number)

    //消した要素を一つ保存するための変数（UNDOに使う）
    var deleteValue: InfoList? = null

    //コルーチンを用いてLiveDataに値を設定するための処理
    suspend fun setList(list: MutableList<InfoList>): MutableList<InfoList>{
        val setList = list.sortedWith(infoListComparator).toMutableList()
        withContext(Dispatchers.IO) {
            _list.postValue(setList)
        }
        return setList
    }

    //タイトル名を変更するための関数
    fun changeTitle(newTitle: String){
        list = list.map { it.copy(title = newTitle) }.toMutableList()
    }
}