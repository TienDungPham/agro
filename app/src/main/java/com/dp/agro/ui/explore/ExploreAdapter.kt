package com.dp.agro.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dp.agro.R
import com.dp.agro.data.model.Disease
import com.dp.agro.databinding.ItemScanDataBinding
import java.util.*

class ExploreAdapter(private val callback: (String) -> Unit) :
    ListAdapter<Disease, ExploreAdapter.ExploreViewHolder>(ExploreDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        return ExploreViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        holder.bind(getItem(position), callback)
    }

    class ExploreViewHolder(private val binding: ItemScanDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Disease, callback: (String) -> Unit) {
            binding.apply {
                textDiseaseName.text = item.name
                textDiseaseLevel.text = item.threatLevel
                textDiseaseDescription.text = item.description

                Glide.with(root)
                    .load(item.imageUrl)
                    .transform(CenterCrop(), RoundedCorners(5))
                    .placeholder(R.drawable.icon_error_rounded)
                    .into(imgDisease)

                btnReadMore.setOnClickListener {
                    val diseaseSlug = item.name.trim()
                        .toLowerCase(Locale.ROOT)
                        .split(" ")
                        .joinToString("_")
                    callback.invoke(diseaseSlug)
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): ExploreViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemScanDataBinding.inflate(inflater, parent, false)
                return ExploreViewHolder(binding)
            }
        }
    }
}


class ExploreDiffUtilCallback : DiffUtil.ItemCallback<Disease>() {
    override fun areItemsTheSame(oldItem: Disease, newItem: Disease): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Disease, newItem: Disease): Boolean {
        return oldItem.description == newItem.description
    }
}