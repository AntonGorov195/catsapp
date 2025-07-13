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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.il.catsapp.R
import co.il.catsapp.data.models.Cat
import co.il.catsapp.databinding.CatMainScreenDialogBinding
import co.il.catsapp.databinding.FragmentMainScreenBinding
import co.il.catsapp.ui.adapters.MainScreenRvAdapter
import co.il.catsapp.ui.view_models.MainScreenViewModel
import co.il.catsapp.utils.Resource
import co.il.catsapp.utils.animateRandom
import co.il.catsapp.utils.animateCatIcon
import co.il.catsapp.utils.autoCleared
import co.il.catsapp.utils.getCatCount
import co.il.catsapp.utils.setGlideImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {
    private val viewModel: MainScreenViewModel by viewModels()
    private var addCatDialog: AlertDialog? = null

    private var binding: FragmentMainScreenBinding by autoCleared()
    private lateinit var adapter: MainScreenRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
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
            animateCatIcon(binding.loadingWidget)
            binding.findCatsList.visibility = View.GONE

            refresh()
        }
        viewModel.displayedImages.observe(viewLifecycleOwner) {
            when (val resource = it.status ){
                is Resource.Error -> {
                    Log.e("cat err", resource.message)
                }
                is Resource.Loading -> {
                    animateCatIcon(binding.loadingWidget)
                    binding.loadingWidget.visibility = View.VISIBLE
                    binding.findCatsList.visibility = View.GONE
                }
                is Resource.Success -> {
                    binding.loadingWidget.visibility = View.GONE
                    binding.findCatsList.visibility = View.VISIBLE
                    adapter.submitList(resource.data)
                }
            }
        }
        viewModel.selectedCat.observe(viewLifecycleOwner) {
            addCatDialog?.dismiss()
            if (it == null) {
                addCatDialog = null
                return@observe
            }
            val cats = viewModel.displayedImages.value ?: return@observe
            val cat = cats.status.data?.get(it) ?: return@observe
            addCatDialog = makeAddCatDialog(cat, it)
            addCatDialog?.show()
        }
        adapter = MainScreenRvAdapter { _, pos ->
            viewModel.selectCat(pos)
        }
        binding.findCatsList.adapter = adapter
        binding.findCatsList.layoutManager = LinearLayoutManager(requireContext())

        if (viewModel.displayedImages.value == null) {
            refresh()
        }
    }

    private fun makeAddCatDialog(cat: Cat, pos: Int): AlertDialog {
        val dialogView = CatMainScreenDialogBinding.inflate(layoutInflater)
        dialogView.readMore.setOnClickListener {
            val url = cat.breed.wikipediaUrl
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            requireContext().startActivity(intent)
        }
        dialogView.breedName.text = cat.breed.name
        dialogView.catNameInput.setText(viewModel.selectedCatName.value ?: "")
        dialogView.catNameInput.addTextChangedListener {
            viewModel.changeCatName(it?.toString() ?: "")
        }
        setGlideImage(dialogView.root, dialogView.catImage, cat.image)
        dialogView.addToFav.setOnClickListener {
            val name = dialogView.catNameInput.text.toString()
            if (name == "") {
                animateRandom(dialogView.catNameInput, 5f, 30f, 0.85f, 300L)
                return@setOnClickListener
            }
            viewModel.addCat(cat, name, {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.successfully_added_to_favorites, name),
                    Toast.LENGTH_SHORT
                ).show()
            }, {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.failed_adding_to_favorites, name),
                    Toast.LENGTH_SHORT
                ).show()
            })
            viewModel.removeFromDisplay(pos)
            viewModel.deselectCat()
        }
        return AlertDialog.Builder(requireContext()).setView(dialogView.root).create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        addCatDialog?.dismiss()
    }

    private fun refresh() {
        viewModel.refreshImages(getCatCount(requireContext()),)
    }
}