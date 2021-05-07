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

    //データが条件を満たすまで待機する関数
    suspend fun checkData(checker: (MutableList<InfoList>?) -> Boolean?, function: () -> Unit) {
        try {
            Log.i("ok", "I'll get the title...")

            //一秒間の間繰り返す
            withTimeout(1000) {
                while (true) {

                    //渡された関数の要件を満たせば抜ける
                    if (checker(_list.value) == true) break

                    //Logを出して0.1秒待つ
                    Log.i("I wait for", "getting title...")
                    delay(100)
                }
            }
        } catch (e: Exception) {

            //失敗時のログの出力
            Log.e("I missed getting title", "error: $e")
        } finally {

            //渡された関数の実行
            function()
        }
    }

    //LiveDataの中身を初期化するための処理
    suspend fun initializeList(list: MutableList<InfoList>) = withContext(Dispatchers.IO){
        _list.postValue(list.sortedWith(infoListComparator).toMutableList())
    }

    //タイトル名を変更するための関数
    fun changeTitle(newTitle: String){
        list = list.map { it.copy(title = newTitle) }.toMutableList()
    }
}