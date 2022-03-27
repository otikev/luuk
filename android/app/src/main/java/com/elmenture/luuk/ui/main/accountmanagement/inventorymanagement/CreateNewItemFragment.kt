package com.elmenture.luuk.ui.main.accountmanagement.inventorymanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elmenture.luuk.databinding.FragmentCreateNewItemBinding
import com.elmenture.luuk.ui.main.MainActivityView
import com.kokonetworks.kokosasa.base.BaseFragment
import models.Item

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateNewItemFragment : BaseFragment() {
    lateinit var binding: FragmentCreateNewItemBinding
    lateinit var activityView: MainActivityView
    lateinit var createNewItemViewModel: CreateNewItemViewModel
    val spinnerArray = ArrayList<String>()

    companion object {
        fun newInstance() = CreateNewItemFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewItemBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setUpEventListeners()
        observeLivedata()
    }

    private fun observeLivedata() {
        createNewItemViewModel.createNewItemApiState.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Item Created Successfully", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "Failed To Create Item ", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

    }


    private fun initView() {
        activityView = requireActivity() as MainActivityView
        createNewItemViewModel = ViewModelProvider(this).get(CreateNewItemViewModel::class.java);
    }

    private fun setUpEventListeners() {
        binding.btnAccept.setOnClickListener {
            try {
                createNewItemViewModel.createNewItem(getItemDetails())
            } catch (ex: NumberFormatException) {
                Toast.makeText(
                    requireContext(),
                    "Please set all the required fields",
                    Toast.LENGTH_LONG
                ).show()

            }

        }

        binding.toolBar.setNavClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun getItemDetails(): Item {
        return Item().apply {
            sizeInternational = binding.etSizeInternational.text.toString()
            sizeNumber = binding.etSizeNumber.text.toString().toInt()
            description = binding.etDescription.text.toString()
            imageUrl = binding.etImageUrl.text.toString()
            price = binding.etItemPrice.text.toString().toLong()
        }
    }
}