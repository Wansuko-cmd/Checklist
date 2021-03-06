
package com.wsr.shopping_friend.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
id:UUIDを用いて一対一のidを記録
number:リストで何番目に並ぶのかを記録
title:チェックリストの題名を記録
check:チェックの有り無しを記録
item:チェックする項目を記録
*/
@Entity(tableName = "info_list_table")
data class InfoList(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "number") var number: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "check") var check: Boolean,
    @ColumnInfo(name = "item") var item: String
)