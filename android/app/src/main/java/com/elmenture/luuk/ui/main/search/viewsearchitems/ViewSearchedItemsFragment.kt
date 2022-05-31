package com.elmenture.luuk.ui.main.search.viewsearchitems

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.databinding.FragmentSearchBinding
import com.elmenture.luuk.databinding.FragmentSearchResultsBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.elmenture.luuk.ui.main.search.SearchViewModel
import models.Item
import models.Spot


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ViewSearchedItemsFragment : BaseFragment(), Type.Search,
    ViewSearchedItemsAdapter.CartActionListener {
    lateinit var binding: FragmentSearchResultsBinding
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private var itemList: ArrayList<Item> = ArrayList()
    lateinit var query: String
    lateinit var adapter: ViewSearchedItemsAdapter
    private lateinit var viewModel: SearchViewModel


    companion object {
        fun newInstance(query: String): ViewSearchedItemsFragment {
            val frag = ViewSearchedItemsFragment()
            frag.query = query
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
        setEventListeners()
        viewModel.fetchItemsByQuery(query)
    }

    private fun setEventListeners() {
        binding.toolBar.setNavClickListener { requireActivity().onBackPressed() }
    }

    private fun initView() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSearch.layoutManager = gridLayoutManager
        adapter = ViewSearchedItemsAdapter(itemList, this)
        binding.rvSearch.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {
        viewModel.searchItemsByQueryLiveData.observe(viewLifecycleOwner) {
            itemList.clear()
            itemList.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }


    private fun createSpot(item: Item): Spot {
        return Spot(
            url = item.imageUrl!!,
            priceCents = item.price!!,
            sizeType = item.sizeType!!,
            sizeInternational = item.sizeInternational,
            sizeNumber = item.sizeNumber,
            itemId = item.id!!,
            description = item.description!!,
            tagProperties = item.tagProperties!!
        )
    }

    override fun onItemClicked(item: Item) {
        activityView.startViewItemFragment(createSpot(item))
    }


}