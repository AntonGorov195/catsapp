package co.il.catsapp.ui.view_models

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.il.catsapp.data.models.Cat
import co.il.catsapp.data.remote_db.models.CatRemote
import co.il.catsapp.data.repos.CatRepository
import co.il.catsapp.utils.getCatCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(private val catRepository: CatRepository) :
    ViewModel() {
    private val _displayedImages = MutableLiveData<List<Cat>>()
    val displayedImages: LiveData<List<Cat>> get() = _displayedImages

    fun refreshImages(count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
//            try {

                val cats = catRepository.getRandomNonFavCats(count)
                _displayedImages.postValue(cats)
//            } catch (e: Exception) {
//                Log.e("Ret", e.toString())
//            }
//            val cats = catRepository.getRandomNonFavCats(getCatCount(context))
        }
    }

    fun removeFromDisplay(pos: Int) {
        val current = _displayedImages.value?.toMutableList() ?: return
        current.removeAt(pos)
        _displayedImages.value = current
    }

    fun addCat(cat: Cat, name: String, success: () -> Unit, error: (Exception) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                catRepository.addCat(cat, name)
                withContext(Dispatchers.Main) {
                    success()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    error(e)
                }
            }

        }
    }
}