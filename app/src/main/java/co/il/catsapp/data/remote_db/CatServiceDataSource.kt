package co.il.catsapp.data.remote_db

import javax.inject.Inject

class CatServiceDataSource @Inject constructor(
    private val catService:  CatService
) : BaseDataSource() {
    suspend fun getCats(count: Int) = getResult { catService.getCats(count) }
//    suspend fun getCat(id: String) = getResult { catService.getCats(count) }
}