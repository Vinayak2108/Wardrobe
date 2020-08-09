package `in`.vrkhedkr.wardrobe.repository

import `in`.vrkhedkr.wardrobe.constant.WareType
import `in`.vrkhedkr.wardrobe.db.DB
import `in`.vrkhedkr.wardrobe.db.DBProvider
import `in`.vrkhedkr.wardrobe.model.Outfit
import `in`.vrkhedkr.wardrobe.model.Ware
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.RoomDatabase

class Repo(context: Context) {

    private var db: DB = DBProvider(context).getDB()

    fun addWare(ware: Ware): Boolean{
        val id = db.getWare().addWare(ware)
        return id != -1L
    }

    fun saveOutFit(outfit: Outfit): Boolean {
        val id = db.getOutFit().addOutFit(outfit)
        return id != -1L
    }

    fun getTopWare(): LiveData<List<Ware>> {
        return db.getWare().getWare(WareType.TOP)
    }

    fun getBottomWare(): LiveData<List<Ware>> {
        return db.getWare().getWare(WareType.BOTTOM)
    }

    fun getOutFits(): LiveData<List<Outfit>> {
        return db.getOutFit().getAll()
    }

}