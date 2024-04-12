package lab02.eim.systems.cs.pub.doctorappointmentapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lab02.eim.systems.cs.pub.doctorappointmentapp.network.DoctorsApi
import lab02.eim.systems.cs.pub.doctorappointmentapp.repository.DoctorRepository
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
}