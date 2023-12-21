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

//version 1
package com.example.fitforma.ui.food

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.provider.MediaStore.Images.Thumbnails.queryMiniThumbnails
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fitforma.databinding.ActivityDetailBinding
import com.example.fitforma.ml.Model9Class
import org.tensorflow.lite.DataType
import java.io.ByteArrayInputStream
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val imageSize = 28
    private lateinit var detail_name: TextView
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the Uri from the Intent
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val imageUri = Uri.parse(imageUriString)

        // Display the selected image in the ImageView
        imageView = binding.detailImg
        imageView.setImageURI(imageUri)

        // Find TextViews by their IDs
        detail_name = binding.detailName
    }


    fun classifyImage(image: Bitmap) {
        try {
            // Resize the image to 28x28
            val scaledBitmap = Bitmap.createScaledBitmap(image, 28, 28, false)

            val model = Model9Class.newInstance(this)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 28, 28, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * 28 * 28 * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            val intValues = IntArray(28 * 28)
            scaledBitmap.getPixels(intValues, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)
            var pixel = 0
            for (i in 0 until 28) {
                for (j in 0 until 28) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

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
                "bakso", "gado", "nasi goreng polos", "nasi goreng telor ceplok",
                "nasi goreng udang", "rendang", "sate", "telur asin", "telur balado"
            )

            runOnUiThread {
                // Make sure the TextView is visible in your layout
                //detail_name.text = classes[maxPos]
                binding.detailName.text = classes[maxPos]
            }

            model.close()
        } catch (e: Exception) {
            // Handle exceptions (log or display error)
            e.printStackTrace()
        }
    }


    fun getBitmap(contentResolver: ContentResolver, uri: Uri): Bitmap {
        val inputStream = contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    fun getBitmap(contentResolver: ContentResolver, blob: ByteArray): Bitmap {
        val inputStream = ByteArrayInputStream(blob)
        return BitmapFactory.decodeStream(inputStream)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // The image is captured successfully
            try {
                val imageUri = data!!.data
                val imageBitmap = getBitmap(contentResolver, imageUri)

                // Retrieve a thumbnail for display
                val thumbnail = queryMiniThumbnails(contentResolver, imageUri, MediaStore.Images.Thumbnails.MINI_KIND, null)
                val thumbnailBlobIndex = thumbnail.getColumnIndex(MediaStore.Images.Thumbnails.DATA)
                val thumbnailBlob = thumbnail.getBlob(thumbnailBlobIndex)
                val thumbnailBitmap = if (thumbnailBlob != null) {
                    getBitmap(contentResolver, thumbnailBlob)
                } else {
                    imageBitmap
                }

                imageView.setImageBitmap(thumbnailBitmap)

                // Create a scaled bitmap for classification
                val scaledBitmap = Bitmap.createScaledBitmap(thumbnailBitmap, imageSize, imageSize, false)
                classifyImage(scaledBitmap)
            } catch (e: Exception) {
                // Handle the exception appropriately, e.g., log an error
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}


//OG Code from youtube ===========================================================
//package com.example.fitforma.ui.food
//
//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.Bundle
//import android.provider.MediaStore
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import com.example.fitforma.databinding.ActivityDetailBinding
//import com.example.fitforma.ml.Model9Class
//import org.tensorflow.lite.DataType
//import org.tensorflow.lite.support.image.ImageProcessor
//import org.tensorflow.lite.support.image.TensorImage
//import org.tensorflow.lite.support.image.ops.ResizeOp
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
//
//class DetailActivity : AppCompatActivity() {
//    private lateinit var binding:ActivityDetailBinding
//    lateinit var predBtn: Button
//    lateinit var bitmap: Bitmap
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityDetailBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Retrieve the Uri from the Intent
//        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
//        val imageUri = Uri.parse(imageUriString)
//
//
//        // Display the selected image in the ImageView
//        val detailImageView = binding.detailImg
//        detailImageView.setImageURI(imageUri)
//
//        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
//
//        val detailname = binding.detailName
//
//        predBtn = binding.PredictBtn
//
//        //labels
//        var labels = application.assets.open("labels.txt").bufferedReader().readLines()
//
//        //Image Processor
//        var imageProcessor = ImageProcessor.Builder()
//            .add(ResizeOp(28,28,ResizeOp.ResizeMethod.BILINEAR))
//            .build()
//
//        predBtn.setOnClickListener{
//
//            var tensorImage = TensorImage(DataType.UINT8)
//            tensorImage.load(bitmap)
//
//            tensorImage = imageProcessor.process(tensorImage)
//
//            val model = Model9Class.newInstance(this)
//
//            // Creates inputs for reference.
//            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 28, 28, 3), DataType.FLOAT32)
//            inputFeature0.loadBuffer(tensorImage.buffer)
//
//            // Runs model inference and gets result.
//            val outputs = model.process(inputFeature0)
//            val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
//
//            var maxIdx = 0
//            outputFeature0.forEachIndexed{index, fl ->
//                if (outputFeature0[maxIdx] < fl){
//                    maxIdx = index
//                }
//            }
//
//            detailname.text = labels[maxIdx]
//
//            // Releases model resources if no longer used.
//            model.close()
//        }
//
//    }
//
//    companion object {
//        const val EXTRA_IMAGE_URI = "extra_image_uri"
//    }
//}


