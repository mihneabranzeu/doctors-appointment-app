package lab02.eim.systems.cs.pub.doctorappointmentapp.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.federatedlearningplatform.fl_tensorflow.FLClient
import com.federatedlearningplatform.fl_tensorflow.SampleSpec
import com.federatedlearningplatform.fl_tensorflow.helpers.classifierAccuracy
import com.federatedlearningplatform.fl_tensorflow.helpers.loadMappedAssetFile
import com.federatedlearningplatform.fl_tensorflow.helpers.negativeLogLikelihoodLoss
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lab02.eim.systems.cs.pub.doctorappointmentapp.screens.fl.Float3DArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.ExecutionException

private const val TAG = "Load Data"
const val IMAGE_SIZE = 32
const val LOWER_BYTE_MASK = 0xFF
val CLASSES = listOf(
    "cat",
    "dog",
    "truck",
    "bird",
    "airplane",
    "ship",
    "frog",
    "horse",
    "deer",
    "automobile"
)
class FLRepository(@ApplicationContext private val context: Context) {
    suspend fun loadData(
        flowerClient: FLClient<Float3DArray, FloatArray>,
        device_id: Int
    ) {
        readAssetLines(context, "data/partition_${device_id - 1}_train.txt") { index, line ->
            if (index % 500 == 499) {
                Log.i(TAG, "${index}th training image loaded")
            }
            addSample(context, flowerClient, "data/$line", true)
        }
        readAssetLines(context, "data/partition_${device_id - 1}_test.txt") { index, line ->
            if (index % 500 == 499) {
                Log.i(TAG, "${index}th test image loaded")
            }
            addSample(context, flowerClient, "data/$line", false)
        }
    }

    private fun addSample(
        context: Context,
        flowerClient: FLClient<Float3DArray, FloatArray>,
        photoPath: String,
        isTraining: Boolean
    ) {
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeStream(context.assets.open(photoPath), null, options)!!
        val sampleClass = getClass(photoPath)

        // Get rgb values of each pixel in the bitmap
        val rgbImage = prepareImage(bitmap)

        // Add the sample to the client
        try {
            flowerClient.addSample(rgbImage, classToLabel(sampleClass), isTraining)
        } catch (e: ExecutionException) {
            throw RuntimeException("Failed to add sample to model", e.cause)
        } catch (_: InterruptedException) {

        }
    }

    private fun prepareImage(bitmap: Bitmap): Float3DArray {
        val normalizedRgb = Array(IMAGE_SIZE) { Array(IMAGE_SIZE) { FloatArray(3) } }
        for (y in 0 until IMAGE_SIZE) {
            for (x in 0 until IMAGE_SIZE) {
                val rgb = bitmap.getPixel(x, y)
                val r = (rgb shr 16 and LOWER_BYTE_MASK) * (1 / 255.0f)
                val g = (rgb shr 8 and LOWER_BYTE_MASK) * (1 / 255.0f)
                val b = (rgb and LOWER_BYTE_MASK) * (1 / 255.0f)
                normalizedRgb[y][x][0] = r
                normalizedRgb[y][x][1] = g
                normalizedRgb[y][x][2] = b
            }
        }
        return normalizedRgb
    }



    private fun getClass(path: String): String {
        return path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[2]
    }

    private suspend fun readAssetLines(
        context: Context,
        fileName: String,
        call: suspend (Int, String) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            BufferedReader(InputStreamReader(context.assets.open(fileName))).useLines {
                it.forEachIndexed { i, l -> launch { call(i, l) } }
            }
        }
    }

    /**
     * Create one-hot encoding for the classes
     */
    private fun classToLabel(className: String): FloatArray {
        return CLASSES.map {
            if (className == it) 1f else 0f
        }.toFloatArray()
    }

    fun createFLClient(): FLClient<Float3DArray, FloatArray> {
        val buffer = loadMappedAssetFile(context, "model/cifar10.tflite")
        val layersSizes = intArrayOf(1800, 24, 9600, 64, 768000, 480, 40320, 336, 3360, 40)
        return FLClient(buffer, layersSizes, SampleSpec(
            { it.toTypedArray() },
            { it.toTypedArray() },
            { Array(it) { FloatArray(CLASSES.size) } },
            ::negativeLogLikelihoodLoss,
            ::classifierAccuracy
        )
        )
    }
}
