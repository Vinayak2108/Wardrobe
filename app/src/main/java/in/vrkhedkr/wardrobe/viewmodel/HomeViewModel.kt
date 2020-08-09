package `in`.vrkhedkr.wardrobe.viewmodel

import `in`.vrkhedkr.wardrobe.model.Outfit
import `in`.vrkhedkr.wardrobe.model.Ware
import `in`.vrkhedkr.wardrobe.repository.Repo
import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application){

    var repo:Repo = Repo(context = application)
    val topWare:LiveData<List<Ware>>
    val bottomWare : LiveData<List<Ware>>
    val outfit : LiveData<List<Outfit>>

    init {
        topWare = repo.getTopWare()
        bottomWare = repo.getBottomWare()
        outfit = repo.getOutFits()
    }

    fun saveOutfitCombination(topWareIndex: Int, bottomWareIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if(topWare.value != null && bottomWare.value != null){
                repo.saveOutFit(Outfit(
                    topWare = (topWare.value as ArrayList)[topWareIndex].wareId,
                    bottomWare = (bottomWare.value as ArrayList)[bottomWareIndex].wareId
                ))
            }
        }
    }

    fun getRandomOutFitCombination(): Outfit? {
        //todo Implement
        return null
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


}
