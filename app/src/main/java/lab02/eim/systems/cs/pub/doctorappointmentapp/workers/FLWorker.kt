package lab02.eim.systems.cs.pub.doctorappointmentapp.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.federatedlearningplatform.fl_tensorflow.createFLService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.FLRepository

@HiltWorker
class FLWorker @AssistedInject constructor(
    @Assisted private val flRepository: FLRepository,
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
       makeStatusNotification("FLWorker is working", applicationContext)
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val flClient = flRepository.createFLClient()
                val flService = createFLService(flClient, projectId = "839ffc6f-eadb-490e-aec4-51134b0a03c4_31873db1-7c5b-4422-ba9c-aff3fcfa1") {
                    Log.d("FLWorker", it)
                }
                flRepository.loadData(flClient, 1)
                flService.evaluate()
                Result.success()
            } catch(t: Throwable){
                Log.e("FLWorker", t.message.toString())
                Result.failure()
            }
        }
    }
}