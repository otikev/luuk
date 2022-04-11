package com.elmenture.luuk.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.elmenture.luuk.R
import com.elmenture.luuk.base.BaseFragment
import com.elmenture.luuk.databinding.FragmentHomeBinding
import com.elmenture.luuk.ui.main.MainActivityView
import models.Item
import models.Spot
import utils.MiscUtils
import views.cardstackview.*


class HomeFragment : BaseFragment(), CardStackListener {

    private var _binding: FragmentHomeBinding? = null
    private val activityView: MainActivityView by lazy { requireActivity() as MainActivityView }
    private val binding get() = _binding!!
    private val cardStackView by lazy { binding.cardStackView }
    private var spots: ArrayList<Spot> = arrayListOf()
    private val adapter = CardStackAdapter(spots)
    private val manager by lazy { CardStackLayoutManager(activity, this) }
    private lateinit var homeViewModel: HomeViewModel
    private val itemList by lazy { homeViewModel.itemsLiveData.value }

    companion object {
        fun newInstance() = HomeFragment()
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
        homeViewModel.fetchItems()
    }

    private fun observeViewModelLiveData() {
        homeViewModel.itemsLiveData.observe(viewLifecycleOwner) {
                adapter.updateContent(createSpots(it))
        }
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if (manager.topPosition == adapter.itemCount - 5) {
            paginate()
            homeViewModel.fetchItems()
        }

        when (direction) {
            Direction.Left -> homeViewModel.updateSwipesData(dislike = adapter.getItem(manager.topPosition))
            Direction.Right -> homeViewModel.updateSwipesData(like = adapter.getItem(manager.topPosition))
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_price)
        Log.d("CardStackView", "onCardAppeared: ($position) ${textView.text}")

        val priceCents = adapter.getSpots()[position].priceCents
        val size = adapter.getSpots()[position].sizeInternational

        binding.tvPrice.text = MiscUtils.getSpannedText(
            getString(
                R.string.contrast_text,
                "KES",
                MiscUtils.getFormattedAmount((priceCents / 100).toDouble())
            )
        )
        binding.tvSize.text =
            MiscUtils.getSpannedText(getString(R.string.contrast_text, "Size", size))
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_price)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun initView() {
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

    private fun paginate() {
        val old = adapter.getSpots()
        val new = old.plus(createSpots(itemList))
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun reload() {
        val old = adapter.getSpots()
        val new = createSpots(itemList)
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addFirst(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                add(manager.topPosition, createSpot())
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addLast(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            addAll(List(size) { createSpot() })
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeFirst(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(manager.topPosition)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeLast(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(this.size - 1)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun replace() {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            removeAt(manager.topPosition)
            add(manager.topPosition, createSpot())
        }
        adapter.setSpots(new)
        adapter.notifyItemChanged(manager.topPosition)
    }

    private fun swap() {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            val first = removeAt(manager.topPosition)
            val last = removeAt(this.size - 1)
            add(manager.topPosition, last)
            add(first)
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun createSpot(): Spot {
        val spots = ArrayList<Spot>()
        return spots[0]
    }

    private fun createSpots(itemList: ArrayList<Item>?): List<Spot> {
        val spots = ArrayList<Spot>()
        itemList?.let {
            for (item in it) {
                spots.add(
                    Spot(
                        url = item.imageUrl!!,
                        priceCents = item.price!!,
                        sizeInternational = item.sizeInternational!!,
                        sizeNumber = item.sizeNumber,
                        itemId = item.id!!,
                        description = item.description!!
                    )
                )
            }
        }
        return spots
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