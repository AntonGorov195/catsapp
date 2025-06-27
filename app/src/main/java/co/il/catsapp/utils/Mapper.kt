package co.il.catsapp.utils

import android.util.Log
import co.il.catsapp.data.local_db.models.BreedLocal
import co.il.catsapp.data.local_db.models.CatBreedLocalJoin
import co.il.catsapp.data.local_db.models.CatLocal
import co.il.catsapp.data.models.Breed
import co.il.catsapp.data.models.Cat
import co.il.catsapp.data.remote_db.models.BreedRemote
import co.il.catsapp.data.remote_db.models.CatRemote

// Local To Domain
fun CatBreedLocalJoin.toDomain(): Cat {
    return Cat(
        name = this.cat.name,
        image = this.cat.image,
        id = this.cat.id,
        breed = this.breed.toDomain(),
    )
}

fun BreedLocal.toDomain(): Breed {
    return Breed(
        id = id,
        name = name,
        wikipediaUrl = wikipediaUrl,
//        weight = weight.toDomain(),
//        origin = origin,
//        lifeSpan = lifeSpan.toDomain(),
//        countryCode = countryCode,
    )
}

// Remote To Domain
fun CatRemote.toDomain(name: String = "") =
    Cat(
        id = id,
        name = name,
        breed = (breeds.firstOrNull() ?: BreedRemote(
            "",
            "Undefined Breed!",
            wikipediaUrl = ""
        )).toDomain(),
        image = url
    )

fun BreedRemote.toDomain() =
    Breed(
        id = id,
        wikipediaUrl = wikipediaUrl ?: "",
        name = name,
    )

// Domain to Local
fun Cat.toLocal() = CatLocal(
    id = id,
    image = image,
    name = name,
    breedId = breed.id,
)

fun Breed.toLocal() = BreedLocal(
    name = name,
    id = id,
    wikipediaUrl = wikipediaUrl
)
