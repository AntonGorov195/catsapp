package co.il.catsapp.data.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.il.catsapp.data.local_db.models.BreedLocal
import co.il.catsapp.data.local_db.models.CatLocal
import co.il.catsapp.data.models.Cat

@Database(entities = [CatLocal::class, BreedLocal::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catDao(): CatDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "cats")
                    .fallbackToDestructiveMigration(false).build().also {
                        instance = it
                    }
            }
    }
}