package co.il.catsapp.data.remote_db.models

data class CatRemote(
    val id: String,
    val url: String,
    val breeds: List<BreedRemote>
)