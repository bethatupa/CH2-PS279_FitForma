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
<<<<<<< HEAD
import android.util.Log
import android.widget.Button
=======
import android.provider.MediaStore.Images.Media.getBitmap
import android.provider.MediaStore.Images.Thumbnails.queryMiniThumbnails
>>>>>>> 0839d809bd2260a6c48e3ff491b8bc7867093c8b
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
<<<<<<< HEAD
        val detailImageView = findViewById<ImageView>(R.id.detail_img)
        val detailname = findViewById<TextView>(R.id.detail_name)
        val food_weight = findViewById<TextView>(R.id.food_weight)
        val food_kal = findViewById<TextView>(R.id.food_kal)
        val food_karbo = findViewById<TextView>(R.id.food_karbo)
        val detail_desc = findViewById<TextView>(R.id.detail_desc)
        detailImageView.setImageURI(imageUri)
=======
        imageView = binding.detailImg
        imageView.setImageURI(imageUri)
>>>>>>> 0839d809bd2260a6c48e3ff491b8bc7867093c8b

        // Find TextViews by their IDs
        detail_name = binding.detailName
    }

<<<<<<< HEAD


        predBtn = findViewById(R.id.PredictBtn)

        //labels
        var labels = application.assets.open("labels.txt").bufferedReader().readLines()

        //Image Processor
        var imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(28,28,ResizeOp.ResizeMethod.BILINEAR))
            .build()

        predBtn.setOnClickListener{

            var tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)

            tensorImage = imageProcessor.process(tensorImage)
=======

    fun classifyImage(image: Bitmap) {
        try {
            // Resize the image to 28x28
            val scaledBitmap = Bitmap.createScaledBitmap(image, 28, 28, false)
>>>>>>> 0839d809bd2260a6c48e3ff491b8bc7867093c8b

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
<<<<<<< HEAD
            Log.d("YourTag", "Max Index: $maxIdx")
=======
            inputFeature0.loadBuffer(byteBuffer)
>>>>>>> 0839d809bd2260a6c48e3ff491b8bc7867093c8b

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

<<<<<<< HEAD
            if (maxIdx == 0) {
                food_kal.text = "57"
                food_karbo.text = "2,12g"
                food_weight.text = "1 Porsi"
                detail_desc.text = "Terdapat 57 kalori dalam Bakso Daging Sapi (1 Porsi).\n" + "Rincian Kalori: 60% lemak, 15% karb, 25% prot."
            }
            if (maxIdx == 1) {
                food_kal.text = "132"
                food_karbo.text = "10,9g"
                food_weight.text = "1 Porsi"
                detail_desc.text = "Terdapat 132 kalori dalam Gado Gado (100 gram).\n" + "Rincian Kalori: 48% lemak, 31% karb, 21% prot."
            }
            if (maxIdx == 2) {
                food_kal.text = "333"
                food_karbo.text = "41,7g"
                food_weight.text = "1 Porsi"
                detail_desc.text = "Terdapat 333 kalori dalam Nasi Goreng (1 Porsi).\n" + "Rincian Kalori: 34% lemak, 51% karb, 15% prot."
            }
            if (maxIdx == 3) {
                food_kal.text = "534"
                food_karbo.text = "42,58g"
                food_weight.text = "1 Porsi"
                detail_desc.text = "Terdapat 534 kalori dalam Nasi Goreng Telur (1 Porsi).\\n\" + \"Rincian Kalori: 34% lemak, 51% karb, 15% prot."
            }
            if (maxIdx == 4) {
                food_kal.text = "321"
                food_karbo.text = "41,88g"
                food_weight.text = "1 Porsi"
                detail_desc.text = "Terdapat 321 kalori dalam Nasi Goreng Udang (1 Porsi ).\n" + "Rincian Kalori: 33% lemak, 53% karb, 14% prot."
            }
            //rendang
            if (maxIdx == 5) {
                food_kal.text = "195"
                food_karbo.text = "4,49g"
                food_weight.text = "1 Buah"
                detail_desc.text = "Terdapat 195 kalori dalam Rendang (100 gram).\n" +
                        "Rincian Kalori: 51% lemak, 9% karb, 40% prot."
            }
            //sate
            if (maxIdx == 6) {
                food_kal.text = "34"
                food_karbo.text = "0,73g"
                food_weight.text = "1 Tusuk"
                detail_desc.text = "Terdapat 34 kalori dalam Sate Ayam (1 Tusuk).\n" +
                        "Rincian Kalori: 58% lemak, 8% karb, 34% prot."
            }
            //telur asin
            if (maxIdx == 7) {
                food_kal.text = "137"
                food_karbo.text = "1,08g"
                food_weight.text = "1 Buah"
                detail_desc.text = "Terdapat 137 kalori dalam Telur Asin (1 Buah ).\n" +
                        "Rincian Kalori: 68% lemak, 3% karb, 28% prot."
            }
            //telur balado
            if (maxIdx == 8) {
                food_kal.text = "71"
                food_karbo.text = "1,22g"
                food_weight.text = "1 Butir"
                detail_desc.text = "Terdapat 71 kalori dalam Telur Balado (1 Buah ).\n" +
                        "Rincian Kalori: 73% lemak, 7% karb, 20% prot."
            }
            // Releases model resources if no longer used.
=======
>>>>>>> 0839d809bd2260a6c48e3ff491b8bc7867093c8b
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


<<<<<<< HEAD
=======
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
>>>>>>> 0839d809bd2260a6c48e3ff491b8bc7867093c8b


