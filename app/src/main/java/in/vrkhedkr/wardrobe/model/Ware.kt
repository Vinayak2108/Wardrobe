package `in`.vrkhedkr.wardrobe.model

import `in`.vrkhedkr.wardrobe.constant.WareType
import `in`.vrkhedkr.wardrobe.view.TOP_WARE_CAMERA
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "ware")
data class Ware(@PrimaryKey(autoGenerate = true) val wareId:Long = 0L, val imgUrl:String, val type: WareType)
