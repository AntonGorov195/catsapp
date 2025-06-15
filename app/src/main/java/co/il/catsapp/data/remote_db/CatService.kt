package co.il.catsapp.data.remote_db

import co.il.catsapp.data.remote_db.models.CatRemote
import co.il.catsapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface CatService {
    @Headers("x-api-key: ${Constants.CAT_API_KEY}")
    @GET("https://api.thecatapi.com/v1/images/search?size=med&mime_types=jpg&format=json&has_breeds=true&order=RANDOM&page=0")
    suspend fun getCats(@Query("limit") count: Int) : Response<List<CatRemote>>
}