package com.luzkan.photer.data.local

import android.arch.persistence.room.*
import com.luzkan.photer.data.local.models.Photo

@Dao
interface PhotoInterface{

    @Query("SELECT*FROM photo ORDER BY tId ASC")
    fun getPhotoList(): List<Photo>

    @Query("SELECT*FROM photo ORDER BY photo_rating DESC")
    fun getPhotoListRatingSorted(): List<Photo>

    @Query("SELECT*FROM photo WHERE tId=:tid")
    fun getPhotoItem(tid: Int): Photo

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun savePhoto(photo: Photo)

    @Update
    fun updatePhoto(photo: Photo)

    @Delete
    fun removePhoto(photo: Photo)

}