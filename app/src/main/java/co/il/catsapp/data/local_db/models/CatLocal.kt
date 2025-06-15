package co.il.catsapp.data.local_db.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cats",
    foreignKeys = [
        ForeignKey(
            entity = BreedLocal::class,
            parentColumns = ["id"],
            childColumns = ["breedId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("breedId")]
)
data class CatLocal(
    @PrimaryKey
    val id: String,
    val image: String,
    val breedId: String,
    val name: String,
)