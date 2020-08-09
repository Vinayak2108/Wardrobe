package `in`.vrkhedkr.wardrobe.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outfit")
data class Outfit (@PrimaryKey(autoGenerate = true) val outfitID:Long = 0L, val topWare:Long, val bottomWare:Long)
