package com.coyo.codechallenge.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coyo.codechallenge.data.model.Comment
import com.coyo.codechallenge.databinding.CommentListItemBinding
import com.coyo.codechallenge.ui.adapter.holder.CommentViewHolder

/**
 * @author yaya (@yahyalmh)
 * @since 15th October 2021
 */

class CommentAdapter : RecyclerView.Adapter<CommentViewHolder>() {
    var comments = ArrayList<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = CommentListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = comments[position]
        holder.bind(item)
    }

    /**
     * Set new data and notify
     */
    @SuppressLint("NotifyDataSetChanged")
    fun submitData(data: ArrayList<Comment>) {
        comments = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}