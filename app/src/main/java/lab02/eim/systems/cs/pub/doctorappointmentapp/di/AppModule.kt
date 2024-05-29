package lab02.eim.systems.cs.pub.doctorappointmentapp.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.XRayDatabase
import lab02.eim.systems.cs.pub.doctorappointmentapp.data.XRayDatabaseDao
import lab02.eim.systems.cs.pub.doctorappointmentapp.network.DoctorsApi
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.DoctorRepository
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.FLRepository
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.FireRepository
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.LocationRepository
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.WorkManagerRepository
import lab02.eim.systems.cs.pub.doctorappointmentapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideAppointmentRepository(api: DoctorsApi)= DoctorRepository(api)
     @Singleton
     @Provides
     fun provideAppointmentApi(): DoctorsApi {
         return Retrofit.Builder()
             .baseUrl(Constants.BASE_URL)
             .addConverterFactory(GsonConverterFactory.create())
             .build()
             .create(DoctorsApi::class.java)
     }

    @Singleton
    @Provides
    fun provideLocationRepository(@ApplicationContext context: Context) = LocationRepository(context)

    @Singleton
    @Provides
    fun provideFireAppointmentRepository() = FireRepository(
        queryAppointment = FirebaseFirestore.getInstance().collection("appointments"),
        queryDoctor = FirebaseFirestore.getInstance().collection("doctors"))

    @Singleton
    @Provides
    fun provideXRayDao(xRayDatabase: XRayDatabase): XRayDatabaseDao = xRayDatabase.xRayDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): XRayDatabase
        = Room.databaseBuilder(
            context,
            XRayDatabase::class.java,
            "xray-database"
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideWorkManagerRepository(@ApplicationContext context: Context) = WorkManagerRepository(context)

    @Singleton
    @Provides
    fun provideFLRepository(@ApplicationContext context: Context) = FLRepository(context)
}