package com.example.checklist.info_list_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
check:チェックの有り無しを記録
item:チェックする項目を記録
*/
@Entity(tableName = "info_list_table")
class InfoList(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "check") val check: Boolean,
    @ColumnInfo(name = "item") val item: String
)