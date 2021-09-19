package com.jay.doggos.fragments

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SharedViewModel(application : Application) : AndroidViewModel(application){

    fun downloadImage(context : Context, url : String, referenceName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bitmap : Bitmap
                        = Glide.with(context).asBitmap().load(url).submit().get()
                saveToGallery(context, bitmap, referenceName)
                viewModelScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Saved to DCIM>Doggos", Toast.LENGTH_SHORT).show()
                }
            } catch(e : Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, "Something went wrong :/", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun saveToGallery(context: Context, bitmap: Bitmap, referenceName: String) {
        val filename = "${referenceName}${System.currentTimeMillis()}.png"
        val write: (OutputStream) -> Boolean = {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DCIM}/Doggos")
            }

            context.contentResolver.let {
                it.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)?.let { uri ->
                    it.openOutputStream(uri)?.let(write)
                }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "Doggos"
            val file = File(imagesDir)
            if (!file.exists()) {
                file.mkdir()
            }
            val image = File(imagesDir, filename)
            write(FileOutputStream(image))
        }
    }
}