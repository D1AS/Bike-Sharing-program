package com.example.bikeshare.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bikeshare.data.Bicycle
import com.example.bikeshare.databinding.ItemBicycleBinding

class BicycleAdapter(private val onBicycleStatusChanged: (Bicycle) -> Unit) :
    ListAdapter<Bicycle, BicycleAdapter.BicycleViewHolder>(BicycleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BicycleViewHolder {
        val binding = ItemBicycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BicycleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BicycleViewHolder, position: Int) {
        val bicycle = getItem(position)
        holder.bind(bicycle)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    init {
        setHasStableIds(true)
    }

    inner class BicycleViewHolder(private val binding: ItemBicycleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bicycle: Bicycle) {
            binding.textViewAddress.text = bicycle.address
            binding.textViewName.text = bicycle.name
            binding.textViewLatLng.text = "${bicycle.lat}, ${bicycle.lng}"

            // Bind switch state
            binding.switchIsFound.setOnCheckedChangeListener(null)
            binding.switchIsFound.isChecked = bicycle.isFound
            binding.switchIsFound.text = if (bicycle.isFound) "Found" else "Not Found"

            // Set new listener
            binding.switchIsFound.setOnCheckedChangeListener { _, isChecked ->
                bicycle.isFound = isChecked
                binding.switchIsFound.text = if (isChecked) "Found" else "Not Found"
                onBicycleStatusChanged(bicycle)

                // Remove item if marked as found
                if (isChecked) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val updatedList = currentList.toMutableList()
                        updatedList.removeAt(position)
                        submitList(updatedList)
                    }
                }
            }
        }
    }

    class BicycleDiffCallback : ItemCallback<Bicycle>() {
        override fun areItemsTheSame(oldItem: Bicycle, newItem: Bicycle): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Bicycle, newItem: Bicycle): Boolean =
            oldItem == newItem
    }
}
