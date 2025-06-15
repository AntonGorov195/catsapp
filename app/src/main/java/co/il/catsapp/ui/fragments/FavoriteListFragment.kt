package co.il.catsapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import co.il.catsapp.R
import co.il.catsapp.databinding.FragmentFavoriteListBinding
import co.il.catsapp.databinding.FragmentMainScreenBinding
import co.il.catsapp.ui.adapters.MainScreenRvAdapter
import co.il.catsapp.ui.view_models.FavoriteListViewModel
import co.il.catsapp.ui.view_models.MainScreenViewModel

// A list of all the saved jokes
class FavoriteListFragment : Fragment() {
    private val viewModel: FavoriteListViewModel by viewModels()

    private var _binding: FragmentFavoriteListBinding? = null
    private val binding get() = _binding!! // Safe access

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}