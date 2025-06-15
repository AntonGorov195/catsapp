package co.il.catsapp.data.local_db.models

import androidx.room.Embedded
import androidx.room.Relation

data class CatBreedLocalJoin(
    @Embedded
    val cat: CatLocal,
    @Relation(
        parentColumn = "breedId",
        entityColumn = "id"
    )
    val breed: BreedLocal,
)