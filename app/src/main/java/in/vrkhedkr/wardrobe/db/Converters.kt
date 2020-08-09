package `in`.vrkhedkr.wardrobe.db

import `in`.vrkhedkr.wardrobe.constant.WareType
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromWareTypeToInt(value: Int): WareType {
        return when(value){
            WareType.TOP.value-> WareType.TOP
            WareType.BOTTOM.value -> WareType.BOTTOM
            else -> WareType.TOP
        }
    }

    @TypeConverter
    fun fromIntToWareType(ware: WareType): Int {
        return ware.value
    }

}