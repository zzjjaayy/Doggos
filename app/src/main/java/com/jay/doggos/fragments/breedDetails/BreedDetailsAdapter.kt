package com.jay.doggos.fragments.breedDetails

import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jay.doggos.R
import com.jay.doggos.databinding.BreedDetailsItemBinding

class BreedDetailsAdapter(private var onImageClicked : (String) -> Unit) : ListAdapter<String,
        BreedDetailsAdapter.BreedDetailsViewHolder>(DiffCallback) {

    class BreedDetailsViewHolder(private var binding: BreedDetailsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            Glide.with(binding.breedImage.context)
                .load(imageUrl)
                .into(binding.breedImage)
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
    ): BreedDetailsViewHolder {
        val viewHolder = BreedDetailsViewHolder(
            BreedDetailsItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
        viewHolder.itemView.findViewById<ImageView>(R.id.download_icon).setOnClickListener {
            onImageClicked(getItem(viewHolder.adapterPosition))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BreedDetailsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}