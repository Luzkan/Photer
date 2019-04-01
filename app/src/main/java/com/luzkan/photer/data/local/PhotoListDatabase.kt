package com.luzkan.photer.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.luzkan.photer.data.local.models.Photo

@Database(entities = [Photo::class], version = 1, exportSchema = false)
abstract class PhotoListDatabase: RoomDatabase(){

    abstract fun getPhoto(): PhotoInterface

    companion object {
        private const val databaseName = "photodb"
        var photoListDatabase: PhotoListDatabase? = null

        fun getInstance(context: Context): PhotoListDatabase?{
            if (photoListDatabase == null){
                photoListDatabase = Room.databaseBuilder(context, PhotoListDatabase::class.java, PhotoListDatabase.databaseName).allowMainThreadQueries().build()
            }
            return photoListDatabase
        }
    }
}