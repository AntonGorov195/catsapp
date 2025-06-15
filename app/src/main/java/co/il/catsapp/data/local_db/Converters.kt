package co.il.catsapp.data.local_db

import androidx.room.TypeConverter
import co.il.catsapp.data.local_db.models.LifeSpanLocal
import co.il.catsapp.data.local_db.models.WeightLocal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromLifeSpan(lifeSpan: LifeSpanLocal): String {
        return Gson().toJson(lifeSpan)
    }

    @TypeConverter
    fun toLifeSpan(value: String): LifeSpanLocal {
        val type = object : TypeToken<LifeSpanLocal>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromWeightLocal(weight: WeightLocal): String {
        return Gson().toJson(weight)
    }

    @TypeConverter
    fun toWeightLocal(value: String):  WeightLocal {
        val type = object : TypeToken< WeightLocal>() {}.type
        return Gson().fromJson(value, type)
    }
}