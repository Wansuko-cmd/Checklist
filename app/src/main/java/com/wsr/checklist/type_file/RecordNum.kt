package com.wsr.checklist.type_file

//id:シングルトンの識別記号
//num:本来どの順番にリストが並んでいたのかを記録する変数
data class RecordNumber(
    val id: String,
    var number: Int,
)