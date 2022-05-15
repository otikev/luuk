package com.elmenture.luuk.ui.main.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.databinding.FragmentSearchBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.Item
import models.Spot

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SearchItemsFragment : BaseFragment(), SearchAdapter.CartActionListener {
    lateinit var binding: FragmentSearchBinding
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private var itemList: ArrayList<Item> = ArrayList()
    var adapter = SearchAdapter(itemList, this)

    private lateinit var viewModel: SearchViewModel


    companion object {
        fun newInstance() = SearchItemsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpEventListeners()
        observeLiveData()
    }

    private fun initView() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        binding.rvSearch.layoutManager = LinearLayoutManager(context)
        binding.rvSearch.adapter = adapter
    }

    private fun setUpEventListeners() {
        binding.svItems.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty())
                    viewModel.fetchSearchItems(query);
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {
        viewModel.searchItemLiveData.observe(viewLifecycleOwner) {
            itemList.clear()
            itemList.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemClicked(item: Item) {
        activityView.startViewItemFragment(createSpot(item))
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


}