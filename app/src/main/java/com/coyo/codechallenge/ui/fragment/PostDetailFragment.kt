package com.coyo.codechallenge.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coyo.codechallenge.R
import com.coyo.codechallenge.data.model.Comment
import com.coyo.codechallenge.data.model.Post
import com.coyo.codechallenge.databinding.FragmentPostDetailBinding
import com.coyo.codechallenge.ui.adapter.CommentAdapter
import com.coyo.codechallenge.ui.component.OptionalDialog
import com.coyo.codechallenge.ui.component.VerticalSpaceItemDecoration
import com.coyo.codechallenge.util.AndroidUtils
import com.coyo.codechallenge.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * @author yaya (@yahyalmh)
 * @since 14th October 2021
 */

@AndroidEntryPoint
class PostDetailFragment : Fragment() {
    private var binding: FragmentPostDetailBinding? = null
    private lateinit var adapter: CommentAdapter
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var post: Post


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        post = arguments?.get(DetailViewModel.POST_SAVED_STATE_KEY) as Post

        binding = FragmentPostDetailBinding.inflate(inflater, container, false)

        initToolbar()

        initAdapter()

        initRecyclerView()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDescription()
        fetchData()
    }

    /**
     * Fetch post's user and comments from db or network with coroutine
     */
    private fun fetchData() {
        setLoading(true)

        lifecycleScope.launch {
            whenStarted {
                try {
                    fetchUser()
                    fetchComments()
                } catch (e: Exception) {
                    // Show error in dialog
                    setLoading(false)
                    showDialog(e.message)
                }
            }
        }
    }

    /**
     * Observe fetching post's user and set on post's author name
     */
    private suspend fun fetchUser() {
        detailViewModel.user().observe(viewLifecycleOwner) { user ->
            binding!!.author.text = user.name
        }
    }

    /**
     * Observe fetching post's comments and submit to adapter
     */
    private suspend fun fetchComments() {
        detailViewModel.comments().observe(viewLifecycleOwner) {
            setCommentCount(it.size)
            adapter.submitData(it as ArrayList<Comment>)
            setLoading(false)
        }
    }

    private fun initToolbar() {
        binding!!.toolbar.setNavigationOnClickListener { view ->
            view.findNavController().navigateUp()
        }
    }

    private fun initAdapter() {
        adapter = CommentAdapter()
        //  restores the RecyclerView state only when the adapter is not empty (adapter.getItemCount() > 0)
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    private fun initRecyclerView() {
        binding!!.commentsListView.layoutManager = LinearLayoutManager(activity)
        binding!!.commentsListView.addItemDecoration(
            VerticalSpaceItemDecoration(
                AndroidUtils.dp(requireActivity().applicationContext, 5f)
            )
        )
        binding!!.commentsListView.adapter = adapter
    }

    /**
     * Set post description text
     * Remove line breaks from description
     */
    private fun setDescription() {
        binding!!.description.text = post.body.replace("\n", "").replace("\r", "")
    }

    private fun setCommentCount(count: Int) {
        val countText = resources.getString(R.string.comments_count, count)
        binding!!.commentsCount.text = countText
    }

    /**
     * Set fragment loading state
     */
    private fun setLoading(isLoading: Boolean) {
        binding!!.isLoading = isLoading
    }

    /**
     * Show dialog with message
     */
    private fun showDialog(message: String?) {
        if (message.isNullOrEmpty()) {
            return
        }
        val dialog = OptionalDialog.Builder(requireContext()).setIcon(R.drawable.ic_error)
            .setHint(message)
            .setFirstOption(
                getString(R.string.cancel),
                object : OptionalDialog.OptionalDialogClickListener {
                    override fun onClick(dialog: OptionalDialog) {
                        dialog.dismiss()
                    }
                })
            .setSecondOption(
                getString(R.string.retry),
                object : OptionalDialog.OptionalDialogClickListener {
                    override fun onClick(dialog: OptionalDialog) {
                        fetchData()
                        dialog.dismiss()
                    }
                })

        dialog.show()
    }
}