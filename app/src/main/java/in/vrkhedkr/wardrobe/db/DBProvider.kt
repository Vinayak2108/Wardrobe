package `in`.vrkhedkr.wardrobe.db

import android.content.Context
import androidx.room.*

class DBProvider(context: Context){
    private val DB_NAME = "wardrobe"
    private val db by lazy {
        Room.databaseBuilder(context.applicationContext,DB::class.java,DB_NAME).build()
    }

    fun getDB():DB{
        return db
    }

}

