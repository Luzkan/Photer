package com.luzkan.photer.data.local.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "photo")
class Photo(
    @ColumnInfo(name = "photo_url")
    var url:String = "",
    @ColumnInfo(name = "photo_description")
    var description:String = "",
    @ColumnInfo(name = "photo_rating")
    var rating: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var tId: Int = 0)