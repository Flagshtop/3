package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
@Parcelize
data class Note(@PrimaryKey(autoGenerate = true)
        val id: Int,
    val noteTittle: String,
    val noteDesc: String):Parcelable

//ЭТО ПРОСТО ПИЗДЕЦ