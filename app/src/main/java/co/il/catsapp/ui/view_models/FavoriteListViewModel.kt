package co.il.catsapp.ui.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import co.il.catsapp.data.models.Cat
import co.il.catsapp.data.repos.CatRepository
import co.il.catsapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(private val catRepository: CatRepository) :
    ViewModel() {
    val favCats: LiveData<Resource<List<Cat>>> = catRepository.getFavoriteList()
}