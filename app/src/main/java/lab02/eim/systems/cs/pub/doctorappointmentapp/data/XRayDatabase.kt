package lab02.eim.systems.cs.pub.doctorappointmentapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MXRay

@Database(entities = [MXRay::class], version = 1, exportSchema = false)
abstract class XRayDatabase : RoomDatabase() {
    abstract fun xRayDao(): XRayDatabaseDao
}