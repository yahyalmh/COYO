package com.coyo.codechallenge.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.coyo.codechallenge.data.model.Post
import com.coyo.codechallenge.databinding.HomeListItemBinding
import com.coyo.codechallenge.ui.adapter.holder.HomeViewHolder
import dagger.hilt.android.scopes.ActivityScoped


@ActivityScoped
class MediaAdapter(private val delegate: HomeViewHolder.ItemDelegate) :
    PagingDataAdapter<Post, HomeViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        lateinit var view: ViewBinding
        view = HomeListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val homeViewHolder = HomeViewHolder(view)
        homeViewHolder.setDelegate(delegate)
        return homeViewHolder
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    private class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}