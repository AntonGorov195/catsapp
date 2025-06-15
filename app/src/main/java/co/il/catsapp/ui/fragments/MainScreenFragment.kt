package co.il.catsapp.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.il.catsapp.R
import co.il.catsapp.databinding.CatMainScreenDialogBinding
import co.il.catsapp.databinding.FragmentMainScreenBinding
import co.il.catsapp.ui.adapters.MainScreenRvAdapter
import co.il.catsapp.ui.view_models.MainScreenViewModel
import co.il.catsapp.utils.AnimateCatIcon
import co.il.catsapp.utils.getCatCount
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {
    private val viewModel: MainScreenViewModel by viewModels()

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!! // Safe access

    private lateinit var adapter: MainScreenRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appSettings.setOnClickListener {
            findNavController().navigate(R.id.main_to_settings)
        }
        binding.favoriteCats.setOnClickListener {
            findNavController().navigate(R.id.main_to_fav_list)
        }
        binding.refreshCatsList.setOnClickListener {
            binding.loadingWidget.visibility = View.VISIBLE
            AnimateCatIcon(binding.loadingWidget)
            binding.findCatsList.visibility = View.GONE

            viewModel.refreshImages(getCatCount(requireContext()))
        }
        viewModel.displayedImages.observe(viewLifecycleOwner) {
            binding.loadingWidget.visibility = View.GONE
            binding.findCatsList.visibility = View.VISIBLE
            adapter.submitList(it)
        }
        adapter = MainScreenRvAdapter(
            { cats, pos ->
                val cat = cats.get(pos)
                val dialogView = CatMainScreenDialogBinding.inflate(layoutInflater)
                dialogView.readMore.setOnClickListener {
                    val url = cat.breed.wikipediaUrl
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    requireContext().startActivity(intent)
                }
                dialogView.breedName.text = cat.breed.name
                Glide.with(dialogView.root).load(cat.image).into(dialogView.catImage)
                val box = AlertDialog.Builder(requireContext()).setView(dialogView.root).create()
                box.show()
                dialogView.addToFav.setOnClickListener {
                    if (dialogView.catNameInput.text.toString() == "") {
                        val animation_duration = 100L // Duration of each movement
                        val animation_offset_x = 20f
                        dialogView.catNameInput.animate().translationX(-animation_offset_x)
                            .setDuration(animation_duration).withEndAction {
                            dialogView.catNameInput.animate().translationX(animation_offset_x)
                                .setDuration(animation_duration).withEndAction {
                                dialogView.catNameInput.animate()
                                    .translationX(-animation_offset_x / 2)
                                    .setDuration(animation_duration)
                            }.start()
                        }.start()
                        Toast.makeText(
                            requireContext(),
                            "You must give the cat a name!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }
                    viewModel.addCat(
                        cat,
                        dialogView.catNameInput.text.toString(),
                        {
                            Toast.makeText(context, "Cat successfully added to the favorite list.", Toast.LENGTH_LONG).show()
                            viewModel.removeFromDisplay(pos)
                        },
                        {
                            Toast.makeText(context, "Cat was not added to the favorite list.", Toast.LENGTH_LONG).show()
                            Log.e("DB error", it.toString())
                        })
                    box.cancel()
                }
            }
        )
        binding.findCatsList.adapter = adapter
        binding.findCatsList.layoutManager = LinearLayoutManager(requireContext())

        AnimateCatIcon(binding.loadingWidget)
        viewModel.refreshImages(getCatCount(requireContext()))
    }
}