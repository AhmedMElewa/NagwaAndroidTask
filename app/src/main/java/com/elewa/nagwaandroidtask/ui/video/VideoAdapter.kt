package com.elewa.nagwaandroidtask.ui.video

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elewa.nagwaandroidtask.R
import com.elewa.nagwaandroidtask.data.model.ItemModel
import com.elewa.nagwaandroidtask.databinding.RowVideoBinding
import java.io.File

class VideoAdapter(
    private val onClickListener: OnVideoAdapterClickListener
) : ListAdapter<ItemModel, VideoAdapter.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(
            oldItem: ItemModel,
            newItem: ItemModel
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.status == oldItem.status
        }

        override fun areContentsTheSame(
            oldItem: ItemModel,
            newItem: ItemModel
        ): Boolean {
            return oldItem.status == newItem.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickListener)
    }

    class ViewHolder private constructor(private val binding: RowVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemModel, clickListener: OnVideoAdapterClickListener) {


            binding.txtName.text = item.name

            binding.txtPercentage.text = item.status.toString()+" %"

            Glide.with(itemView)
                .asBitmap()
                .load(item.url)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.imgVideo)

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

            binding.imgDownload.setOnClickListener {
                clickListener.onDownloadClick(item)
            }

            itemView.setOnClickListener {
                clickListener.onVideoAdapterClick(item)
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val binding =
                    RowVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface OnVideoAdapterClickListener {
        fun onVideoAdapterClick(item: ItemModel)
        fun onDownloadClick(item: ItemModel)
    }
}
