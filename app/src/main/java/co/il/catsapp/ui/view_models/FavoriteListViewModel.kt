package co.il.catsapp.ui.view_models

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.il.catsapp.data.models.Cat
import co.il.catsapp.data.repos.CatRepository
import co.il.catsapp.utils.getCatCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(private val catRepository: CatRepository) :
    ViewModel() {

}