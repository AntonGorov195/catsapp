package co.il.catsapp.ui.view_models

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.il.catsapp.data.models.Cat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor() :
    ViewModel() {
    private val _catCount = MutableLiveData<Int>()
    val catCount: LiveData<Int> get() = _catCount

    fun setCount(value: Int) {
        _catCount.value = value
    }
}