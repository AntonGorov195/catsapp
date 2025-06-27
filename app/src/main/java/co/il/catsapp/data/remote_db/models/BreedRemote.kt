package co.il.catsapp.data.remote_db.models

import com.google.gson.annotations.SerializedName

data class BreedRemote(
    val id: String,
    val name: String,
    @SerializedName("wikipedia_url")
    val wikipediaUrl: String?,
)