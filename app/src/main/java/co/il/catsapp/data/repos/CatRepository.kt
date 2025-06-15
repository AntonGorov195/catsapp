package co.il.catsapp.data.repos

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import co.il.catsapp.data.local_db.CatDao
import co.il.catsapp.data.local_db.models.CatBreedLocalJoin
import co.il.catsapp.data.models.Cat
import co.il.catsapp.data.remote_db.CatServiceDataSource
import co.il.catsapp.data.remote_db.models.CatRemote
import co.il.catsapp.utils.toDomain
import co.il.catsapp.utils.toLocal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatRepository @Inject constructor(
    private val remote: CatServiceDataSource,
    private val local: CatDao,
) {
    suspend fun getRandomNonFavCats(count: Int): List<Cat> {
        // TODO: change 10 to a user defined value
        val remoteCats = remote.getCats(count).body()
            ?: return emptyList()
        // toSet is used for better performance
        val fav = local.getAllFavoriteCatsId().toSet()
        return remoteCats.filter { it.id !in fav }.map { it.toDomain() }
    }
    suspend fun addCat(cat: Cat, name: String) {
        local.insertBreed(cat.breed.toLocal())
        local.insertCat(cat.toLocal(name))
    }
}