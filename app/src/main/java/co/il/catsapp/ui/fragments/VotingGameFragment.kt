package co.il.catsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.il.catsapp.R
import co.il.catsapp.databinding.FragmentVotingGameBinding

// A fun game where the user tries to guess to number of votes a cat has.
class VotingGameFragment : Fragment() {
    private var _binding: FragmentVotingGameBinding? = null
    private val binding get() = _binding!! // Safe access

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVotingGameBinding.inflate(inflater, container, false)
        return binding.root
    }

}