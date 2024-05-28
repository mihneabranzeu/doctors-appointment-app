package lab02.eim.systems.cs.pub.doctorappointmentapp.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.XRayDatabaseDao
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MXRay
import javax.inject.Inject

class XRayRepository @Inject constructor(private val xRayDatabaseDao: XRayDatabaseDao) {
    suspend fun addXRay(xRay: MXRay) = xRayDatabaseDao.insert(xRay)
    suspend fun deleteXRay(xRay: MXRay) = xRayDatabaseDao.deleteXRay(xRay)
    suspend fun deleteAllXRays() = xRayDatabaseDao.deleteAll()
    fun getAllXRays(): Flow<List<MXRay>> = xRayDatabaseDao.getXRays().flowOn(Dispatchers.IO)
        .conflate()
}