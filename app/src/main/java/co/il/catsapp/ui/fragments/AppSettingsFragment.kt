package co.il.catsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import co.il.catsapp.R
import co.il.catsapp.databinding.FragmentAppSettingsBinding
import co.il.catsapp.ui.view_models.AppSettingsViewModel
import co.il.catsapp.utils.AutoClearedValue
import co.il.catsapp.utils.autoCleared
import co.il.catsapp.utils.getCatCount
import co.il.catsapp.utils.setCatCount
import kotlinx.coroutines.launch

class AppSettingsFragment : Fragment() {
    private val viewModel: AppSettingsViewModel by viewModels()

    private var binding: FragmentAppSettingsBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items =  listOf(10, 25, 50, 100)
        binding.catCount.adapter = ArrayAdapter(requireContext(), R.layout.custom_spitter, items)
        binding.catCount.setSelection(items.indexOf(getCatCount(requireContext())))
        binding.catCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                setCatCount(requireContext(), selectedItem)
                val saved = getCatCount(requireContext())
                Toast.makeText(requireContext(), "Selected: $saved", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}