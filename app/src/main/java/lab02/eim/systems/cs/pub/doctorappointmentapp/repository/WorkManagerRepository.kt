package lab02.eim.systems.cs.pub.doctorappointmentapp.repository

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import lab02.eim.systems.cs.pub.doctorappointmentapp.workers.FLWorker

class WorkManagerRepository(@ApplicationContext val context: Context) {
    private val workManager = WorkManager.getInstance(context)

    fun startWork() {
        val workBuilder = OneTimeWorkRequestBuilder<FLWorker>()

        workManager.enqueue(workBuilder.build())
    }
}