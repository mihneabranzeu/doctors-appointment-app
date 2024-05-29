package lab02.eim.systems.cs.pub.doctorappointmentapp.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import lab02.eim.systems.cs.pub.doctorappointmentapp.utils.Constants.DELAY_TIME_MILLIS

class FLWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
       makeStatusNotification("FLWorker is working", applicationContext)
        return withContext(Dispatchers.IO) {
            return@withContext try {
                delay(DELAY_TIME_MILLIS)
                Result.success()
            } catch(t: Throwable){
                Result.failure()
            }
        }
    }
}