package co.il.catsapp.di

import android.content.Context
import co.il.catsapp.data.local_db.AppDatabase
import co.il.catsapp.data.remote_db.CatService
import co.il.catsapp.data.remote_db.models.CatRemote
import co.il.catsapp.utils.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideCatRemoteService(retrofit: Retrofit): CatService =
        retrofit.create(CatService::class.java)

    @Provides
    @Singleton
    fun provideLocalDataBase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Provides
    @Singleton
    fun provideCatDao(database: AppDatabase) = database.catDao()
}