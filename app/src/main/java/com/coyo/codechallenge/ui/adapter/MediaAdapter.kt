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
        val postViewHolder = PostViewHolder(view)
        postViewHolder.setDelegate(delegate)
        return postViewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    /**
     * callback to distinguish post items in list
     * it leads to do not show duplicate items which fetched from the server in different requests
     */
    private class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }
}