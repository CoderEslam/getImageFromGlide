package com.doubleclick.getimagefromglide

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var image: ImageView
    lateinit var i: Uri;

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        i = uri!!
        Log.e(TAG, "onActivityResult: $uri")
        image.setImageURI(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image = findViewById(R.id.imageURI);
        image.setOnClickListener {
            openImage()
        }
        Glide.with(this@MainActivity).asFile()
            .load("https://firebasestorage.googleapis.com/v0/b/myfirebasechat-90c34.appspot.com/o/GroupPhotos%2F-NIDUUKevZ6JzN9CINNT%2Fehric5JNzjWTrU6KKeL9ATvckKa2_1672916812547.null?alt=media&token=80fe268c-b16c-46c1-87ba-cef8413333a4")
            .into(object : CustomTarget<File>() {
                override fun onResourceReady(resource: File, transition: Transition<in File>?) {
//                    Log.e(
//                        TAG,
//                        "onResourceReady:path " + getImageContentUri(this@MainActivity, resource)
//                    )
                    Log.e(
                        TAG,
                        "onResourceReady:absolutePath " + getImageContentUri(
                            this@MainActivity,
                            resource
                        )
                    )
                    image.setImageURI(
//                        Uri.parse(resource.path)
                        getImageContentUri(
                            this@MainActivity,
                            resource
                        )
                    )
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })

//        Glide.with(this)
//            .load("https://firebasestorage.googleapis.com/v0/b/myfirebasechat-90c34.appspot.com/o/GroupPhotos%2F-NIDUUKevZ6JzN9CINNT%2Fehric5JNzjWTrU6KKeL9ATvckKa2_1672916812547.null?alt=media&token=80fe268c-b16c-46c1-87ba-cef8413333a4")
//            .into(image)
    }


    @SuppressLint("Range")
    fun getImageContentUri(context: Context, imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ", arrayOf(filePath), null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val id: Int = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            cursor.close()
            Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                )
            } else {
                null
            }
        }
    }

    fun openImage() {
//        val intent = Intent()
//        intent.type = "image/*"
//        intent.action = Intent.ACTION_GET_CONTENT
//        startActivityForResult(intent, IMAGE_REQUEST)
        getContent.launch("image/*")
    }

}