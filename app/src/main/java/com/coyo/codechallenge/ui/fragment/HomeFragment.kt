package com.coyo.codechallenge.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coyo.codechallenge.R
import com.coyo.codechallenge.data.model.Post
import com.coyo.codechallenge.databinding.FragmentHomeBinding
import com.coyo.codechallenge.ui.adapter.MediaAdapter
import com.coyo.codechallenge.ui.adapter.MediaLoadStateAdapter
import com.coyo.codechallenge.ui.adapter.holder.PostViewHolder
import com.coyo.codechallenge.ui.component.OptionalDialog
import com.coyo.codechallenge.ui.component.VerticalSpaceItemDecoration
import com.coyo.codechallenge.util.AndroidUtils
import com.coyo.codechallenge.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

/**
 * @author yaya (@yahyalmh)
 * @since 14th October 2021
 */

@AndroidEntryPoint
class HomeFragment : Fragment(), PostViewHolder.ItemDelegate {
    private var binding: FragmentHomeBinding? = null
    private lateinit var adapter: MediaAdapter
    private val viewModel: HomeViewModel by viewModels()

    private var lastFirstVisiblePosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initAdapter()

        initRecyclerView()

        initSwipeToRefresh()

        initArrowUpKey()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fetchPosts()
        observeAdapterLoadStatFlow()
    }

    /**
     * Observe the adapter's load state flow to set the fragment ui based on it
     */
    private fun observeAdapterLoadStatFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                adapter.loadStateFlow.distinctUntilChanged { old, new ->
                    val res =
                        (old.refresh is LoadState.Error && new.refresh is LoadState.Error && ((old.refresh as LoadState.Error).error == (new.refresh as LoadState.Error).error))
                    res
                }.collectLatest { loadStates ->
                    when (loadStates.mediator?.refresh) {
                        is LoadState.Loading -> {
                            setLoadingState()
                        }
                        is LoadState.Error -> {
                            setErrorState(loadStates)
                        }
                        is LoadState.NotLoading -> {
                            setNotLoadingState()
                        }
                    }

                    binding!!.invalidateAll()
                }
            }
        }
    }

    /**
     * Fetch posts on view life cycle with coroutines
     */
    private fun fetchPosts() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.posts().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setNotLoadingState() {
        binding!!.isLoading = false
        binding!!.isError = false
        binding!!.swipeRefresh.isRefreshing = false
    }

    /**
     * Set load state when user want to refresh data
     */
    private fun setLoadingState() {
        binding!!.swipeRefresh.isRefreshing = true
        binding!!.isLoading = true
        binding!!.isError = false
    }

    /**
     * Set error state on ui and show dialog based on the error message
     */
    private fun setErrorState(loadStates: CombinedLoadStates) {
        binding!!.swipeRefresh.isRefreshing = false
        if (binding!!.postsListView.adapter!!.itemCount <= 0) {
            binding!!.isError = true
        }
        binding!!.isLoading = false
        binding!!.errorTxtView.text =
            (loadStates.mediator?.refresh as LoadState.Error).error.message

        val dialog = OptionalDialog.Builder(requireContext()).setIcon(R.drawable.ic_error)
            .setHint((loadStates.mediator?.refresh as LoadState.Error).error.message!!)
            .setFirstOption(
                getString(R.string.ok),
                object : OptionalDialog.OptionalDialogClickListener {
                    override fun onClick(dialog: OptionalDialog) {
                        dialog.dismiss()
                    }
                })

        dialog.show()
    }

    private fun initAdapter() {
        adapter = MediaAdapter(this)

        //  restores the RecyclerView state only when the adapter is not empty (adapter.getItemCount() > 0)
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // Hide error state when adapter has item(it happens when data fetched from db and api return error)
        adapter.addLoadStateListener {
            if (adapter.itemCount >= 1) {
                binding!!.isError = false
            }
        }
    }

    private fun initRecyclerView() {
        // Set layout manager
        binding!!.postsListView.layoutManager = LinearLayoutManager(activity)

        //Set item decoration
        binding!!.postsListView.addItemDecoration(
            VerticalSpaceItemDecoration(
                AndroidUtils.dp(
                    requireActivity().applicationContext, 5f
                )
            )
        )

        // Set adapter
        binding!!.postsListView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = MediaLoadStateAdapter(adapter), footer = MediaLoadStateAdapter(adapter)
        )

        // Set arrow fab visibility when scrolling
        binding!!.postsListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                lastFirstVisiblePosition =
                    (binding!!.postsListView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                setArrowUpKey()
            }
        })
    }

    private fun initSwipeToRefresh() {
        binding!!.swipeRefresh.setOnRefreshListener { adapter.refresh() }
    }

    /**
     * Set listener on arrow fab to scroll to position zero
     */
    private fun initArrowUpKey() {
        binding!!.arrowUp.setOnClickListener {
            binding!!.postsListView.smoothScrollToPosition(0)
        }
    }

    /**
     * Show arrow fab when more than 1 item scrolled
     */
    private fun setArrowUpKey() {
        binding!!.needUpKey =
            (binding!!.postsListView.layoutManager is LinearLayoutManager && lastFirstVisiblePosition > 1)
    }

    override fun onResume() {
        super.onResume()
        // Scroll to the last visible item
        (binding!!.postsListView.layoutManager!! as LinearLayoutManager).scrollToPositionWithOffset(
            lastFirstVisiblePosition, 0
        )
    }

    override fun onPause() {
        super.onPause()
        // Get the last visible item's position to scroll to its position
        lastFirstVisiblePosition =
            (binding!!.postsListView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    /**
     * Navigate to the detail fragment when an post item clicked
     * Pass the clicked post to dest fragment
     */
    override fun itemClicked(item: Post) {
        val actionHomeToDetail = HomeFragmentDirections.actionHomeToDetail(item)
        findNavController().navigate(actionHomeToDetail)
    }
}