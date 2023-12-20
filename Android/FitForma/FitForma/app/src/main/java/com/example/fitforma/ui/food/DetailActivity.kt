package com.example.fitforma.ui.food

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitforma.R
import org.tensorflow.lite.support.common.FileUtil
import java.io.IOException
import java.nio.MappedByteBuffer

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Retrieve the Uri from the Intent
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val imageUri = Uri.parse(imageUriString)


        // Display the selected image in the ImageView
        val detailImageView = findViewById<ImageView>(R.id.detail_img)
        detailImageView.setImageURI(imageUri)

    }

    private fun loadModelFile(): MappedByteBuffer {
        try {
            // Load the model file from the assets folder
            val modelFilename = "model_9_class.tflite" // Replace with your actual model file
            return FileUtil.loadMappedFile(this, modelFilename)
        } catch (e: IOException) {
            throw IOException("Error loading model", e)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}
