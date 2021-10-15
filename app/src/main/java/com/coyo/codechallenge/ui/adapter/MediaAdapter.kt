package com.coyo.codechallenge.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.coyo.codechallenge.data.model.Post
import com.coyo.codechallenge.databinding.PostListItemBinding
import com.coyo.codechallenge.ui.adapter.holder.PostViewHolder
import dagger.hilt.android.scopes.ActivityScoped


@ActivityScoped
class MediaAdapter(private val delegate: PostViewHolder.ItemDelegate) :
    PagingDataAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        lateinit var view: ViewBinding
        view = PostListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val homeViewHolder = PostViewHolder(view)
        homeViewHolder.setDelegate(delegate)
        return homeViewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
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