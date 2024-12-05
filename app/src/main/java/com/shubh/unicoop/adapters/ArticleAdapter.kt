package com.shubh.unicoop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shubh.unicoop.R

import com.shubh.unicoop.data.ResultArticle
import com.shubh.unicoop.databinding.ItemArticleBinding

class ArticleAdapter :
    ListAdapter<ResultArticle, ArticleAdapter.AlarmViewHolder>(AlarmDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AlarmViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: ResultArticle) {
            binding.tvTitle.text = article.title
            binding.tvBodyOfTitle.text = article.abstract
            if (article.media.isNotEmpty()) {
                if (article.media[0].mediaMetadata.isNotEmpty()) {
                    Glide.with(itemView.context).load(article.media[0].mediaMetadata[0].url)
                        .into(binding.ivArtcileImage)
                }
            } else {
                Glide.with(itemView.context).load(R.drawable.ic_launcher_foreground)
                    .into(binding.ivArtcileImage)
            }


            /*binding.root.setOnLongClickListener {
                onRemoveAlarm(alarm.id)
                true
            }*/
        }
    }
}

class AlarmDiffCallback : DiffUtil.ItemCallback<ResultArticle>() {
    override fun areItemsTheSame(oldItem: ResultArticle, newItem: ResultArticle) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ResultArticle, newItem: ResultArticle) =
        oldItem == newItem
}
