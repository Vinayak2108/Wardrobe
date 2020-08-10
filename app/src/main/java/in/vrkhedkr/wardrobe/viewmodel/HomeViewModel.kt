package `in`.vrkhedkr.wardrobe.viewmodel

import `in`.vrkhedkr.wardrobe.model.Outfit
import `in`.vrkhedkr.wardrobe.model.Ware
import `in`.vrkhedkr.wardrobe.repository.Repo
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.ThreadPoolExecutor

class HomeViewModel(application: Application) : AndroidViewModel(application){

    var repo:Repo = Repo(context = application)
    val topWare:LiveData<List<Ware>>
    val bottomWare : LiveData<List<Ware>>
    val outfit : LiveData<List<Outfit>>
    val randomOutFitIndex:MutableLiveData<Pair<Int,Int>> = MutableLiveData()

    init {
        topWare = repo.getTopWare()
        bottomWare = repo.getBottomWare()
        outfit = repo.getOutFits()
    }

    fun saveOutfitCombination(topWareIndex: Int, bottomWareIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if(!topWare.value.isNullOrEmpty() && !bottomWare.value.isNullOrEmpty()){
                repo.saveOutFit(Outfit(
                    topWare = (topWare.value as ArrayList)[topWareIndex].wareId,
                    bottomWare = (bottomWare.value as ArrayList)[bottomWareIndex].wareId
                ))
            }
        }
    }

    fun setRandomOutFit() {
        if(!topWare.value.isNullOrEmpty() && !bottomWare.value.isNullOrEmpty()){
            val topIndex = getRandomInt(topWare.value?.size ?: 0 )
            val bottomIndex = getRandomInt(bottomWare.value?.size ?: 0 )
            randomOutFitIndex.value = Pair(topIndex,bottomIndex)
        }
    }


    fun getRandomInt(exclusiveBound:Int): Int {
        return ThreadLocalRandom.current().nextInt(0,exclusiveBound)
    }

    fun saveWare(ware: Ware) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addWare(ware)
        }
    }

    fun checkCurrentOutFitPresent(topIndex: Int, bottomIndex: Int): Boolean {

        if(topWare.value == null ||bottomWare.value == null) return false

        val output = outfit.value?.filter {
            it.topWare == (topWare.value as ArrayList)[topIndex].wareId
                    &&
                    it.bottomWare == (bottomWare.value as ArrayList)[bottomIndex].wareId
        }
        return (output?.size ?: 0) > 0
    }

    fun deleteOutFitCombination(topIndex: Int, bottomIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val output = outfit.value?.filter {
                it.topWare == (topWare.value as ArrayList)[topIndex].wareId
                        &&
                        it.bottomWare == (bottomWare.value as ArrayList)[bottomIndex].wareId
            }
            output?.let{
                if(output.isNotEmpty()){
                    repo.deleteOutFit(output[0])
                }
            }
        }
    }


}
