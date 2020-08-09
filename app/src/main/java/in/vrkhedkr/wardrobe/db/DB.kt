package `in`.vrkhedkr.wardrobe.db

import `in`.vrkhedkr.wardrobe.model.Outfit
import `in`.vrkhedkr.wardrobe.model.Ware
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Ware::class, Outfit::class],version = 1)
@TypeConverters(Converters::class)
abstract class DB : RoomDatabase(){
    abstract fun getWare():WareDao
    abstract fun getOutFit(): OutfitDao
}