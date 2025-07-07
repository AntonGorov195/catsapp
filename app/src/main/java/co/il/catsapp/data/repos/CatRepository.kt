package co.il.catsapp.data.repos

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import co.il.catsapp.data.local_db.CatDao
import co.il.catsapp.data.local_db.models.CatBreedLocalJoin
import co.il.catsapp.data.models.Cat
import co.il.catsapp.data.remote_db.CatServiceDataSource
import co.il.catsapp.data.remote_db.models.CatRemote
import co.il.catsapp.utils.Resource
import co.il.catsapp.utils.toDomain
import co.il.catsapp.utils.toLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatRepository @Inject constructor(
    private val remote: CatServiceDataSource,
    private val local: CatDao,
) {
    fun getRandomNonFavCats(count: Int): Flow<Resource<List<Cat>>> =
        flow {
            emit(Resource.loading())

            val localItems = local.getAllFavoriteCatsId()
            val localIds = localItems.value?.toSet() ?: emptySet()

            when (val result = remote.getCats(count).status) {
                is Resource.Success -> {
                    val filtered = result.data?.filter { it.id !in localIds && it.breeds.isNotEmpty() }
                        ?.map { it.toDomain() } ?: emptyList()
                    emit(Resource.success(filtered))
                }
                is Resource.Error -> emit(Resource.error(result.message))
                is Resource.Loading -> emit(Resource.loading())
            }
        }.flowOn(Dispatchers.IO)


    suspend fun addCat(cat: Cat) {
        local.insertBreed(cat.breed.toLocal())
        local.insertCat(cat.toLocal())
    }

    fun getCat(id: String): LiveData<Resource<Cat>> = liveData {
        emit(Resource.loading())

        val cat = withContext(Dispatchers.IO) {
            local.getCat(id)
        }

        if (cat != null){
            emit(Resource.success(cat.toDomain()))
            return@liveData
        }
        emit(Resource.error("Couldn't find cat"))
    }

    suspend fun removeFavoriteCat(id: String) {
        local.deleteCat(id)
    }

    fun getFavoriteList(): LiveData<Resource<List<Cat>>> =
        local.getAllFavoriteCats().map {
            Resource.success(it.map { cat ->
                cat.toDomain()
            })
        }
}