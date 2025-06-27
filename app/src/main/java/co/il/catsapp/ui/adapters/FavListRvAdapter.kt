package co.il.catsapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.il.catsapp.data.models.Cat
import co.il.catsapp.databinding.FavListRvBinding
import co.il.catsapp.utils.setGlideImage

class FavListRvAdapter(
    private val onItemClick: (List<Cat>, Int) -> Unit
) : ListAdapter<Cat, FavListRvAdapter.CatViewHolder>(DiffCallback()) {

    class CatViewHolder(private val binding: FavListRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cat: Cat, onClick: (Cat) -> Unit) {
            binding.catName.text = cat.name
            setGlideImage(binding.root, binding.catImageView, cat.image)
            binding.root.setOnClickListener { onClick(cat) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Cat>() {
        override fun areItemsTheSame(old: Cat, new: Cat) = old.id == new.id
        override fun areContentsTheSame(old: Cat, new: Cat) = old == new
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val binding = FavListRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(getItem(position), {
            onItemClick(currentList, currentList.indexOf(it))
        })
    }
}