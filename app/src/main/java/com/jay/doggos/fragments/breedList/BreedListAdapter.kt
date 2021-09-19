package com.jay.doggos.fragments.breedList

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jay.doggos.databinding.BreedItemLayoutBinding
import java.util.*

class BreedListAdapter(private val onBreedClicked : (String) -> Unit) : ListAdapter<String,
        BreedListAdapter.BreedViewHolder>(DiffCallback) {

    class BreedViewHolder(private var binding: BreedItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(breed: String) {
            binding.breedName.text = breed.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BreedViewHolder {
        val viewHolder = BreedViewHolder(BreedItemLayoutBinding.inflate(LayoutInflater.from(parent.context)))
        viewHolder.itemView.setOnClickListener {
            onBreedClicked(getItem(viewHolder.adapterPosition))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}