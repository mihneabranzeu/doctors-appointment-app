package lab02.eim.systems.cs.pub.doctorappointmentapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import dagger.hilt.android.HiltAndroidApp
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.FLRepository
import lab02.eim.systems.cs.pub.doctorappointmentapp.workers.FLWorker
import javax.inject.Inject

@HiltAndroidApp
class DoctorAppointmentApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: FLWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()
}

class FLWorkerFactory @Inject constructor(private val flRepository: FLRepository): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = FLWorker(flRepository, appContext, workerParameters)

}