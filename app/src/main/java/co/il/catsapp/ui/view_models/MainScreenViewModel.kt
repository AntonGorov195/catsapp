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
import co.il.catsapp.utils.Resource
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
    private val _displayedImages = MutableLiveData<Resource<List<Cat>>>()
    val displayedImages: LiveData<Resource<List<Cat>>> get() = _displayedImages

    private val _selectedCat = MutableLiveData<Int?>()
    val selectedCat: LiveData<Int?> get() = _selectedCat

    private val _selectedCatName = MutableLiveData<String>()
    val selectedCatName: LiveData<String> get() = _selectedCatName

    fun refreshImages(count: Int) {
        viewModelScope.launch {
            catRepository.getRandomNonFavCats(count).collect {
                _displayedImages.value = it
            }
        }
    }

    fun removeFromDisplay(pos: Int) {
        val status = _displayedImages.value?.status ?: return
        when(status){
            is Resource.Error -> {}
            is Resource.Loading -> {}
            is Resource.Success -> {
                val cats = status.data!!.toMutableList()
                cats.removeAt(pos)
                _displayedImages.value = Resource.success(cats)
            }
        }
    }

    fun addCat(cat: Cat, name: String, success: () -> Unit, error: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val namedCat = cat.copy(name = name)
                catRepository.addCat(namedCat)
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

    fun selectCat(catIndex: Int) {
        _selectedCat.value = catIndex
    }

    fun deselectCat() {
        _selectedCat.value = null
        _selectedCatName.value = ""
    }

    fun changeCatName(name: String) {
        _selectedCatName.value = name
    }
}