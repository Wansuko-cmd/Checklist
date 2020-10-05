package com.wsr.checklist.type_file

import android.text.TextWatcher

//TExtWatcherの、定義しなくてはならないところを省略するためのインスタンス
interface CustomTextWatcher: TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}