package com.example.checklist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    @ColumnInfo(name = "check") val check: Boolean,
    @ColumnInfo(name = "item") val item: String
)
