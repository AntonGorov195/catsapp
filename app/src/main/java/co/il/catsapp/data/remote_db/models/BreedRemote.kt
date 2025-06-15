package co.il.catsapp.data.remote_db.models

import com.google.gson.annotations.SerializedName

data class BreedRemote(
    val id: String,
    val name: String,
    @SerializedName("wikipedia_url")
    val wikipediaUrl: String?,
//    val weight: WeightRemote,
//    val origin: String,
//    @SerializedName("country_code")
//    val countryCode: String,
//    @SerializedName("life_span")
//    val lifeSpan: String,
)