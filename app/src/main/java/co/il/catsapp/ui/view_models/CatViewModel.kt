package co.il.catsapp.ui.view_models

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import co.il.catsapp.data.models.Cat
import co.il.catsapp.data.repos.CatRepository
import co.il.catsapp.services.AlarmReceiver
import co.il.catsapp.utils.CatAlarmHelper
import co.il.catsapp.utils.Resource
import co.il.catsapp.utils.getCatCount
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

    fun setAlarm(cat: Cat, delayMillis: Long) {
        viewModelScope.launch {
            val triggerTime = System.currentTimeMillis() + delayMillis
            val updatedCat = cat.copy(alarmTime = triggerTime)

            catRepository.addCat(updatedCat)
            _id.postValue(updatedCat.id)
        }
    }
}