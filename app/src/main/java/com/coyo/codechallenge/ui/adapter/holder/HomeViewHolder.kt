package com.coyo.codechallenge.ui.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.coyo.codechallenge.data.model.Post
import com.coyo.codechallenge.databinding.HomeListItemBinding

class HomeViewHolder(private val binding: HomeListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var delegate: ItemDelegate
    lateinit var item: Post

    interface ItemDelegate {
        fun itemClicked(item: Post)
    }

    fun setDelegate(delegate: ItemDelegate) {
        this.delegate = delegate
    }


    fun bind(item: Post) {
        if (this::item.isInitialized && item.id == this.item.id) {
            return
        }

        this.item = item
        binding.apply {
            titleTxtView.text = item.title
        }
    }

    init {
        binding.contentView.setOnClickListener { navigateToPostDetails(item) }
    }

    private fun navigateToPostDetails(post: Post) {
        delegate.itemClicked(post)
    }
}