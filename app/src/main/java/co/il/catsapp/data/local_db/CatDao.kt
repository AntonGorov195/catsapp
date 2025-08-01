package co.il.catsapp.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import co.il.catsapp.data.local_db.models.BreedLocal
import co.il.catsapp.data.local_db.models.CatBreedLocalJoin
import co.il.catsapp.data.local_db.models.CatLocal

@Dao
interface CatDao {
    @Transaction
    @Query("SELECT * FROM cats")
    fun getAllFavoriteCats() : LiveData<List<CatBreedLocalJoin>>

    @Query("SELECT id FROM cats")
    fun getAllFavoriteCatsId() : LiveData<List<String>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBreed(breed: BreedLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCat(cat: CatLocal)

    @Transaction
    @Query("SELECT * FROM cats WHERE id = :catId LIMIT 1")
    fun getCat(catId: String) : CatBreedLocalJoin?

    @Query("DELETE FROM cats WHERE id = :catId")
    suspend fun deleteCat(catId: String)
}