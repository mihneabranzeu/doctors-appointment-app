package lab02.eim.systems.cs.pub.doctorappointmentapp.repository

import android.content.Context
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import lab02.eim.systems.cs.pub.doctorappointmentapp.workers.FLWorker
import java.time.Duration

class WorkManagerRepository(@ApplicationContext val context: Context) {
    private val workManager = WorkManager.getInstance(context)

    fun startWork() {
        val workBuilder = PeriodicWorkRequestBuilder<FLWorker>(repeatInterval = Duration.ofSeconds(15))

        workManager.enqueue(workBuilder.build())
    }
}