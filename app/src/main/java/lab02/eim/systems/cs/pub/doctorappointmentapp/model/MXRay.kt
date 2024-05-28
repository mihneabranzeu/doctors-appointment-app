package lab02.eim.systems.cs.pub.doctorappointmentapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
@Entity(tableName = "xrays_tbl")
data class MXRay(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "xray_data")
    val imageData : ByteArray,
    @ColumnInfo(name = "xray_diagnostic")
    val diagnostic: String
)
