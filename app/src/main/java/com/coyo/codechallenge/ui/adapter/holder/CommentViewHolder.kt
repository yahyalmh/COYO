package com.coyo.codechallenge.ui.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.coyo.codechallenge.data.model.Comment
import com.coyo.codechallenge.databinding.CommentListItemBinding

/**
 * @author yaya (@yahyalmh)
 * @since 15th October 2021
 */

class CommentViewHolder(private val binding: CommentListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    lateinit var item: Comment

    fun bind(item: Comment) {
        if (this::item.isInitialized && item.id == this.item.id) {
            return
        }

        this.item = item
        binding.apply {
            nameTextView.text = item.name

            // Remove line breaks from post body
            bodyTextView.text = item.body.replace("\n", "").replace("\r", "")
        }
    }
}