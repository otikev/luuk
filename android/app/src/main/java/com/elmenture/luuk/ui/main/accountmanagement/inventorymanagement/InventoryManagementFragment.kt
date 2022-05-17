package com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.databinding.FragmentInventoryManagementBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.Item


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class InventoryManagementFragment : BaseFragment(), Type.ProfileSettings, InventoryAdapter.CartActionListener {
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    lateinit var binding: FragmentInventoryManagementBinding
    lateinit var viewModel: InventoryManagementViewModel
    var itemList = arrayListOf<Item>()
    lateinit var adapter: InventoryAdapter


    companion object {
        fun newInstance() = InventoryManagementFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInventoryManagementBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpEventListeners()
        observeLivedata()
        viewModel.fetchItems()
    }

    private fun observeLivedata() {
        viewModel.fetchItemsApiState.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isSuccessful) {
                    itemList.clear()
                    itemList.addAll(it.data as ArrayList<Item>)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }


    private fun initView() {
        viewModel = ViewModelProvider(this).get(InventoryManagementViewModel::class.java)
        adapter = InventoryAdapter(itemList, this)
        binding.rvInventoryItems.layoutManager = LinearLayoutManager(context)
        binding.rvInventoryItems.adapter = adapter

    }

    private fun setUpEventListeners() {
        binding.toolBar.setHelperTextClickListener { activityView.startCreateItemFragment() }
        binding.toolBar.setNavClickListener() { requireActivity().onBackPressed() }
    }

    override fun onEditClicked(item: Item) {
        activityView.startCreateItemFragment(item)
    }

    override fun onDiscardClicked(item: Item) {
    }
}