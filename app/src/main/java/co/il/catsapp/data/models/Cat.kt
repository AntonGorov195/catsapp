package co.il.catsapp.data.models

data class Cat(
    val id: String,
    val breed: Breed,
    val image: String,
    val name: String,
    val alarmTime: Long? = null,
)