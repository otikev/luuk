package com.elmenture.luuk.ui.main.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DefaultItemAnimator
import com.amazonaws.util.StringUtils.upperCase
import com.elmenture.luuk.R
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.base.Type
import com.elmenture.luuk.base.repositories.LocalRepository
import com.elmenture.luuk.data.ItemCart
import com.elmenture.luuk.data.ItemQueue
import com.elmenture.luuk.databinding.FragmentHomeBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.Item
import models.Spot
import utils.MiscUtils
import views.cardstackview.*


class HomeFragment : BaseFragment(), Type.Home, CardStackListener {

    private var _binding: FragmentHomeBinding? = null
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private val binding get() = _binding!!
    private val cardStackView by lazy { binding.cardStackView }
    private val adapter = CardStackAdapter()
    private val manager by lazy { CardStackLayoutManager(activity, this) }
    private lateinit var homeViewModel: HomeViewModel

    companion object {
        fun newInstance() = HomeFragment()
    }

    fun showAllItemsInQueue(): Boolean {
        val sharedPref =
            requireContext().getSharedPreferences("LUUK_PREFERENCES", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("show_all_items", true)
    }

    fun saveQueueSetting(showAllItems: Boolean) {
        val sharedPref =
            requireContext().getSharedPreferences("LUUK_PREFERENCES", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("show_all_items", showAllItems)
        editor.apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeViewModelLiveData()
        setupCardStackView()
        setEventListeners()
        if (ItemQueue.count() < 5) {
            homeViewModel.fetchItems(showAllItemsInQueue())
        }
    }

    override fun onResume() {
        super.onResume()
        //Always show a new item when the app resumes
        if (!ItemQueue.isEmpty()) {
            ItemQueue.removeTopItem()
            adapter.notifyDataSetChanged()
        }
    }

    private fun setEventListeners() {
        binding.rgOptions.setOnCheckedChangeListener { _, checkedId ->
            homeViewModel.clearItems()
            when (checkedId) {
                R.id.rb_my_sizes -> {
                    ItemQueue.clear()
                    adapter.notifyDataSetChanged()
                    saveQueueSetting(false)
                    homeViewModel.fetchItems(false)
                }
                R.id.rb_all -> {
                    ItemQueue.clear()
                    adapter.notifyDataSetChanged()
                    saveQueueSetting(true)
                    homeViewModel.fetchItems(true)
                }
            }
        }

        val comingSoonAction = View.OnClickListener {
            Toast.makeText(requireContext(), "Feature Coming Soon", Toast.LENGTH_SHORT).show()
        }
        binding.btn1.setOnClickListener(comingSoonAction)
        binding.btn2.setOnClickListener(comingSoonAction)
        binding.btn3.setOnClickListener(comingSoonAction)
        binding.btn4.setOnClickListener(comingSoonAction)
        binding.btn5.setOnClickListener(comingSoonAction)
    }


    private fun observeViewModelLiveData() {
        homeViewModel.itemsLiveData.observe(viewLifecycleOwner) {

            val cart = LocalRepository.swipeRecords.likes.value
            val unfilteredList = createSpots(it)
            if (cart != null) {
                ItemQueue.addItemsAndFilterItemsInCart(unfilteredList, cart.toList())
            } else {
                ItemQueue.addItems(unfilteredList)
            }
            adapter.notifyDataSetChanged()
            if (ItemQueue.isEmpty()) {
                binding.itemInfo.visibility = GONE
                binding.cardStackView.visibility = INVISIBLE
                if (homeViewModel.initialized()) {
                    showDialog("There are no matches for this selection")
                }
            } else {
                binding.itemInfo.visibility = VISIBLE
                binding.cardStackView.visibility = VISIBLE
            }
        }
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction) {
        val spot = ItemQueue.getTopItem()
        ItemQueue.removeTopItem()
        adapter.notifyDataSetChanged()

        if (ItemQueue.isEmpty()) {
            binding.itemInfo.visibility = GONE
            binding.cardStackView.visibility = INVISIBLE
        } else {
            binding.itemInfo.visibility = VISIBLE
            binding.cardStackView.visibility = VISIBLE
        }

        when (direction) {
            Direction.Left -> {
                homeViewModel.updateSwipesData(
                    dislike = spot
                )
            }
            Direction.Right -> {
                homeViewModel.updateSwipesData(
                    like = spot
                )
                ItemCart.addItem(spot)
            }
        }

        logUtils.d("onCardSwiped, items remaining : ${adapter.itemCount}")
        if (adapter.itemCount < 3) {
            homeViewModel.fetchItems(showAllItemsInQueue())
        }
    }

    override fun onCardRewound() {
        logUtils.d("onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        logUtils.d("onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_price)

        val item = adapter.getSpots()[position]
        val priceCents = item.priceCents
        binding.tvPrice.text = MiscUtils.getSpannedText(
            getString(
                R.string.contrast_text,
                "KES",
                MiscUtils.getFormattedAmount((priceCents / 100).toDouble())
            )
        )

        if (item.sizeType.equals("INT", true)) {
            binding.tvSize.text = MiscUtils.getSpannedText(
                getString(
                    R.string.contrast_text,
                    "Size",
                    upperCase(item.sizeInternational)
                )
            )
        } else {
            binding.tvSize.text = MiscUtils.getSpannedText(
                getString(
                    R.string.contrast_text,
                    "Size",
                    item.sizeNumber.toString() + "(" + item.sizeType + ")"
                )
            )
        }
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_price)
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun initView() {
        if (showAllItemsInQueue()) {
            binding.rbAll.isChecked = true
        } else {
            binding.rbMySizes.isChecked = true
        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java);
        adapter.setItemClickListener { activityView.startViewItemFragment(adapter.getItem(manager.topPosition)) }
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun createSpots(itemList: ArrayList<Item>?): List<Spot> {
        val spots = ArrayList<Spot>()
        itemList?.let {
            for (item in it) {
                spots.add(createSpot(item))
            }
        }
        return spots
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResumeFromBackstack() {
        super.onResumeFromBackstack()
        activityView.resetBottomNavigation()
    }

}

