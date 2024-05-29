package lab02.eim.systems.cs.pub.doctorappointmentapp.screens.fl

import android.util.Log
import androidx.lifecycle.ViewModel
import com.federatedlearningplatform.fl_tensorflow.FLClient
import com.federatedlearningplatform.fl_tensorflow.FLService
import com.federatedlearningplatform.fl_tensorflow.createFLService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.FLRepository
import javax.inject.Inject

@HiltViewModel
class FLViewModel @Inject constructor(private val flRepository: FLRepository) : ViewModel() {
    private val _textResult = MutableStateFlow<String>("")
    val textResult: StateFlow<String> = _textResult

    private var flClient: FLClient<Float3DArray, FloatArray> = flRepository.createFLClient()
    private var flService: FLService<Float3DArray, FloatArray> = createFLService(flClient) {
        setTextResult(it)
    }

    suspend fun loadDataInBackground(deviceId: String): String {
        return runWithStacktraceOr("Failed to load training dataset.") {
            flClient.clearSamples()
            flRepository.loadData(flClient, deviceId.toInt())
            "Training dataset is loaded in memory. Ready to train!\nTrain Size: ${flClient.trainingSamples.size}\n${flClient.trainingSamples[0].label.map { it.toString() }}"
        }

    }

    private suspend fun <T> runWithStacktraceOr(or: T, call: suspend () -> T): T {
        return try {
            call()
        } catch (err: Error) {
            Log.e(TAG, Log.getStackTraceString(err))
            or
        }
    }

    private suspend fun runWithStacktrace(call: suspend () -> Unit) {
        try {
            call()
        } catch (err: Error) {
            Log.e(TAG, Log.getStackTraceString(err))
        }
    }

    fun setTextResult(text: String) {
        _textResult.value = text
    }

    fun getParameters(): List<ByteArray> {
        return flService.getParameters()
    }

    fun sendParameters(data: List<ByteArray>) = flService.sendModelParameters()
    suspend fun handleFit() {
       runWithStacktrace {
           val result = runWithStacktraceOr("Failed to fit the model.") {
               flService.fit()
               "Model fitted successfully."
           }
           setTextResult(result)
       }
    }

    fun handleEvaluate() {
       flService.evaluate()
    }


}

private const val TAG = "FLViewModel"