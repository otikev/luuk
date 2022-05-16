package com.elmenture.luuk.ui.main.search

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
import com.elmenture.luuk.databinding.FragmentSearchBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.Item
import models.Spot


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ViewSearchedItemsFragment : BaseFragment(), ViewSearchedItemsAdapter.CartActionListener {
    lateinit var binding: FragmentSearchBinding
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private var itemList: ArrayList<Item> = ArrayList()

    private lateinit var viewModel: SearchViewModel


    companion object {
        fun newInstance(list: List<Item>) :ViewSearchedItemsFragment{
            val frag = ViewSearchedItemsFragment()
            frag.itemList = list as ArrayList<Item>
            return frag
        }
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
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSearch.layoutManager = gridLayoutManager
        val adapter = ViewSearchedItemsAdapter(itemList, this)
        binding.rvSearch.adapter = adapter
    }

    private fun setUpEventListeners() {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {

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

    }


}