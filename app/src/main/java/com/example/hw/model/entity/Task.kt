package com.example.hw.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "task")
data class Task (
    @ColumnInfo(name = "title")
    var title: String?,
    @ColumnInfo(name = "description")
    var description: String?
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}