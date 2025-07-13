package co.il.catsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.il.catsapp.R
import co.il.catsapp.databinding.FragmentFavoriteListBinding
import co.il.catsapp.ui.adapters.FavListRvAdapter
import co.il.catsapp.ui.view_models.FavoriteListViewModel
import co.il.catsapp.utils.Resource
import co.il.catsapp.utils.autoCleared
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteListFragment : Fragment() {
    private val viewModel: FavoriteListViewModel by viewModels()

    private var binding: FragmentFavoriteListBinding by autoCleared()

    private lateinit var adapter: FavListRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favCatsList.layoutManager = LinearLayoutManager(requireContext())
        adapter = FavListRvAdapter({ cats, pos ->
            val action = FavoriteListFragmentDirections.fromFavListToCat(cats[pos].id)
            findNavController().navigate(action)
        })
        binding.favCatsList.adapter = adapter
        viewModel.favCats.observe(viewLifecycleOwner) {
            when(val list = it.status) {
                is Resource.Error -> {
                    Toast.makeText(requireContext(),
                        getString(R.string.couldn_t_get_your_favorite_cats), Toast.LENGTH_SHORT).show()
                    Log.e("favorite cat not found", "Couldn't read your favorite cats")
                }
                is Resource.Loading -> {
                    // No need for a loading screen.
                    // The loading is too fast to notice.
                }
                is Resource.Success -> {
                    adapter.submitList(list.data!!)
                }
            }
        }
    }
}