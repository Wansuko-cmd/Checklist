package com.example.checklist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infoList_table")
class InfoList(
    @PrimaryKey
    @ColumnInfo(name = "check") val check: Boolean,
    @ColumnInfo(name = "item") val item: String
)