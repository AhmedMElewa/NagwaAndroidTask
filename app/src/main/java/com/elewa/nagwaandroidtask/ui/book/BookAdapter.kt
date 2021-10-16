package com.elewa.nagwaandroidtask.ui.book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elewa.nagwaandroidtask.data.model.ItemModel
import com.elewa.nagwaandroidtask.databinding.RowBookBinding

class BookAdapter (
    private val onClickListener: OnBookClickListener
) : ListAdapter<ItemModel, BookAdapter.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(
            oldItem: ItemModel,
            newItem: ItemModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ItemModel,
            newItem: ItemModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }

    class ViewHolder private constructor(private val binding: RowBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemModel, clickListener: OnBookClickListener) {

            binding.txtName.text = item.name

            binding.txtPercentage.text = item.status.toString()+" %"

            binding.imgDownload.setOnClickListener {
                clickListener.onDownloadClick(item)
            }

            if (item.status == 100) {
                binding.imgDownload.visibility = View.GONE
                binding.imgSaved.visibility = View.VISIBLE
                binding.txtPercentage.visibility= View.GONE
            }

            if (item.status == 0) {
                binding.imgDownload.visibility = View.VISIBLE
                binding.imgSaved.visibility = View.GONE
                binding.txtPercentage.visibility= View.GONE
            }

            if (item.status!! in 1..99) {
                binding.imgSaved.visibility = View.GONE
                binding.imgDownload.visibility = View.GONE
                binding.txtPercentage.visibility= View.VISIBLE
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding =
                    RowBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface OnBookClickListener {
        fun onDownloadClick(book: ItemModel)
    }
}
