package com.elmenture.luuk.ui.main.search.canned

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.databinding.FragmentCannedResultsBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.elmenture.luuk.ui.main.search.SearchViewModel
import models.Item
import models.Spot
import utils.CannedSearch


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CannedItemsFragment : BaseFragment(), Type.Search,
    CannedItemsAdapter.CartActionListener {
    lateinit var binding: FragmentCannedResultsBinding
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private var itemList: ArrayList<Item> = ArrayList()
    lateinit var query: CannedSearch
    lateinit var adapter: CannedItemsAdapter
    private lateinit var viewModel: SearchViewModel

    companion object {
        fun newInstance(query: CannedSearch): CannedItemsFragment {
            val frag = CannedItemsFragment()
            frag.query = query
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCannedResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
        setEventListeners()
        viewModel.fetchCannedItems(query)
    }

    private fun setEventListeners() {
        binding.toolBar.setNavClickListener { requireActivity().onBackPressed() }
    }

    private fun initView() {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSearch.layoutManager = gridLayoutManager
        adapter = CannedItemsAdapter(itemList, this)
        binding.rvSearch.adapter = adapter
        binding.toolBar.setTitle(query.friendlyString())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {
        viewModel.cannedItemsLiveData.observe(viewLifecycleOwner) {
            if(it.isEmpty()){
                binding.rvSearch.visibility=GONE
                binding.txtNoResults.visibility=VISIBLE
            }else{
                binding.rvSearch.visibility=VISIBLE
                binding.txtNoResults.visibility=GONE
                itemList.clear()
                itemList.addAll(it)
                adapter.notifyDataSetChanged()
            }
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