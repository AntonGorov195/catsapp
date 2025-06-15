package co.il.catsapp.utils

import android.util.Log
import co.il.catsapp.data.local_db.models.BreedLocal
import co.il.catsapp.data.local_db.models.CatBreedLocalJoin
import co.il.catsapp.data.local_db.models.CatLocal
import co.il.catsapp.data.local_db.models.LifeSpanLocal
import co.il.catsapp.data.local_db.models.WeightLocal
import co.il.catsapp.data.models.Breed
import co.il.catsapp.data.models.Cat
import co.il.catsapp.data.models.LifeSpan
import co.il.catsapp.data.models.Weight
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

fun WeightLocal.toDomain(): Weight {
    return Weight(
        minImperial = this.minImperial,
        maxImperial = this.maxImperial,
        minMetric = minMetric,
        maxMetric = maxMetric,
    )
}

fun LifeSpanLocal.toDomain(): LifeSpan {
    return LifeSpan(
        min = min,
        max = max,
    )
}

// Remote To Domain
// TODO: Handle API not returning a breed
fun CatRemote.toDomain(name: String = "") =
    Cat(
        id = id,
        name = name,
        breed = breeds.first().toDomain(),
        image = url
    )

// TODO: Handle parsing life span and weight
fun BreedRemote.toDomain() =
    Breed(
        id = id,
        wikipediaUrl = wikipediaUrl ?: "",
        name = name,
//        lifeSpan = LifeSpan(0, 0),
//        countryCode = countryCode,
//        origin = origin,
//        weight = Weight(0,0,0,0)
    )

// Domain to Local
fun Cat.toLocal(name: String? = null) = CatLocal(
    id = id,
    image = image,
    name = name ?: this.name,
    breedId = breed.id,
)

fun Breed.toLocal() = BreedLocal(
    name = name,
    id = id,
    wikipediaUrl = wikipediaUrl
)
