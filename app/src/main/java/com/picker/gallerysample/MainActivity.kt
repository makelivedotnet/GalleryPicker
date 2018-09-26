package com.picker.gallerysample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import com.picker.gallery.GalleryPicker
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.BitmapFactory

class MainActivity : AppCompatActivity() {

    private val PERMISSIONS_READ_WRITE = 123

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isReadWritePermitted()) getGalleryResults() else checkReadWritePermission()
    }

    fun getGalleryResults() {
        val images = GalleryPicker(this).getImages()
        val videos = GalleryPicker(this).getVideos()
        text.text = "IMAGES COUNT: ${images[0].MINI_THUMB_MAGIC}\nVIDEOS COUNT: ${videos[0].MINI_THUMB_MAGIC}"

        val bitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, images[0].ID?.toLong()!!, MediaStore.Images.Thumbnails.MINI_KIND, null as BitmapFactory.Options?)
        iv.setImageBitmap(bitmap)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkReadWritePermission(): Boolean {
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_READ_WRITE)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_READ_WRITE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) getGalleryResults()
        }
    }

    private fun isReadWritePermitted(): Boolean = (checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
}