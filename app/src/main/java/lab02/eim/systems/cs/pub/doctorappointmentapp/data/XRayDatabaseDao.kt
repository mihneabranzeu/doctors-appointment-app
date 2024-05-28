package lab02.eim.systems.cs.pub.doctorappointmentapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import lab02.eim.systems.cs.pub.doctorappointmentapp.model.MXRay

@Dao
interface XRayDatabaseDao {
    @Query("SELECT * FROM xrays_tbl")
    fun getXRays(): Flow<List<MXRay>>

    @Query("SELECT * FROM xrays_tbl WHERE id=:id")
    suspend fun getXRayById(id: String): MXRay

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(xRay: MXRay)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(xRay: MXRay)

    @Query("DELETE from xrays_tbl")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteXRay(xRay: MXRay)
}
