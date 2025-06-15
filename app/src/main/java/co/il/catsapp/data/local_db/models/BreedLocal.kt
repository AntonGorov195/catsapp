package co.il.catsapp.data.local_db.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breeds")
data class BreedLocal(
    @PrimaryKey
    val id: String,
    val name:String,
//    val weight: WeightLocal,
//    val origin: String,
//    val countryCode: String,
//    val lifeSpan: LifeSpanLocal,
    val wikipediaUrl: String,
)