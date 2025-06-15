package co.il.catsapp.data.remote_db

import javax.inject.Inject

class CatServiceDataSource @Inject constructor(
    private val catService:  CatService
)  {
    suspend fun getCats(count: Int) = catService.getCats(count)
}