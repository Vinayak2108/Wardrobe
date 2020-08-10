package `in`.vrkhedkr.wardrobe.db

import `in`.vrkhedkr.wardrobe.constant.WareType
import `in`.vrkhedkr.wardrobe.model.Outfit
import `in`.vrkhedkr.wardrobe.model.Ware
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface WareDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addWare(ware:Ware):Long

    @Query("Select * From ware where type == :type")
    fun getWare(type:WareType):LiveData<List<Ware>>

}

@Dao
interface OutfitDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addOutFit(outFit:Outfit):Long

    @Query("select * from outfit")
    fun getAll(): LiveData<List<Outfit>>

    @Delete
    fun deleteOutFit(outfit: Outfit)

}