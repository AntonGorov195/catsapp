package co.il.catsapp.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import co.il.catsapp.data.models.Cat
import co.il.catsapp.data.repos.CatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CatViewModel @Inject constructor(private val catRepository: CatRepository) :
    ViewModel() {
    private val _isRemoving = MutableLiveData<Boolean>()
    val isRemoving: LiveData<Boolean> get() = _isRemoving

    private val _id = MutableLiveData<String>()
    val cat = _id.switchMap {
        catRepository.getCat(it)
    }

    fun setId(id: String) {
        _id.value = id
    }

    fun startRemoving() {
        _isRemoving.value = true
    }

    fun endRemoving() {
        _isRemoving.value = false
    }

    fun removeCat(cat: Cat) {
        viewModelScope.launch {
            try {
                catRepository.removeFavoriteCat(cat.id)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    error(e)
                }
            }
        }
    }
}