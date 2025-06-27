package co.il.catsapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.il.catsapp.R
import co.il.catsapp.data.models.Cat
import co.il.catsapp.databinding.MainScreenRvBinding
import co.il.catsapp.utils.setGlideImage

class MainScreenRvAdapter(
    private val onItemClick: (List<Cat>, Int) -> Unit
) : ListAdapter<Cat, MainScreenRvAdapter.CatViewHolder>(DiffCallback()) {
    class CatViewHolder(private val binding: MainScreenRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: Cat, onClick: (Cat) -> Unit) {
            binding.breedName.text = cat.breed.name
            setGlideImage(binding.root, binding.catImageView, cat.image)
            binding.root.setOnClickListener { onClick(cat) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Cat>() {
        override fun areItemsTheSame(old: Cat, new: Cat) = old.id == new.id
        override fun areContentsTheSame(old: Cat, new: Cat) = old == new
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val binding =
            MainScreenRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(getItem(position)) {
            onItemClick(currentList, currentList.indexOf(it))
        }
    }
}