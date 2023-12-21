//package com.example.fitforma.ui.food
//
//import android.net.Uri
//import android.os.Bundle
//import android.widget.ImageView
//import androidx.appcompat.app.AppCompatActivity
//import com.example.fitforma.R
//import org.tensorflow.lite.support.common.FileUtil
//import java.io.IOException
//import java.nio.MappedByteBuffer
//
//class DetailActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail)
//
//        // Retrieve the Uri from the Intent
//        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
//        val imageUri = Uri.parse(imageUriString)
//
//
//        // Display the selected image in the ImageView
//        val detailImageView = findViewById<ImageView>(R.id.detail_img)
//        detailImageView.setImageURI(imageUri)
//
//        val model = loadModelFile()
//
//    }
//
//    private fun loadModelFile(): MappedByteBuffer {
//        try {
//            // Load the model file from the assets folder
//            val modelFilename = "model_9_class.tflite" // Replace with your actual model file
//            return FileUtil.loadMappedFile(this, modelFilename)
//        } catch (e: IOException) {
//            throw IOException("Error loading model", e)
//        }
//    }
//
//    companion object {
//        const val EXTRA_IMAGE_URI = "extra_image_uri"
//    }
//}
//
package com.example.fitforma.ui.food

import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitforma.R
import com.example.fitforma.ml.Model9Class
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder


class DetailActivity : AppCompatActivity() {
    private val imageSize = 28
    private lateinit var detail_name: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Retrieve the Uri from the Intent
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val imageUri = Uri.parse(imageUriString)

        // Display the selected image in the ImageView
        imageView = findViewById(R.id.detail_img)
        imageView.setImageURI(imageUri)

        // Find TextViews by their IDs
        detail_name = findViewById(R.id.detail_name)
    }

    fun classifyImage(image: Bitmap) {
        try {
            val model = Model9Class.newInstance(this)

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 28, 28, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            val intValues = IntArray(imageSize * imageSize)
            image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0: TensorBuffer = outputs.getOutputFeature0AsTensorBuffer()
            val confidences = outputFeature0.floatArray
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf(
                "bakso",
                "gado",
                "nasi goreng polos",
                "nasi goreng telor ceplok",
                "nasi goreng udang",
                "rendang",
                "sate",
                "telur asin",
                "telur balado"
            )

            runOnUiThread {
                detail_name.text = classes[maxPos]
                var s = ""
                for (i in classes.indices) {
                    s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100)
                }
            }

            // Releases model resources if no longer used.
            model.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            var image = data!!.extras!!["data"] as Bitmap?
            val dimension = Math.min(image!!.width, image.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)

            runOnUiThread {
                imageView.setImageBitmap(image)
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
                classifyImage(image)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}


