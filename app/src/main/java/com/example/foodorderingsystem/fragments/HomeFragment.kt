package com.example.foodorderingsystem.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingsystem.MainActivity
import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.adapters.FoodAdapter
import com.example.foodorderingsystem.data.FoodRepository

class HomeFragment : Fragment() {

    private lateinit var rvFoodList: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var llCategories: LinearLayout
    private lateinit var tvItemCountBadge: TextView
    private lateinit var adapter: FoodAdapter

    private var selectedCategory = "All"
    private val categories = listOf("All", "Jollibee", "McDonald's", "KFC", "Chowking", "Mang Inasal", "Starbucks")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        rvFoodList = view.findViewById(R.id.rv_food_list)
        etSearch = view.findViewById(R.id.et_search)
        llCategories = view.findViewById(R.id.ll_categories)
        tvItemCountBadge = view.findViewById(R.id.tv_item_count_badge)

        val tvExploreMenu = view.findViewById<TextView>(R.id.tv_promo_btn)
        tvExploreMenu.setOnClickListener {
            (activity as? MainActivity)?.selectBottomTab(R.id.nav_menu)
        }

        setupCategories()
        setupRecyclerView()
        setupSearch()

        return view
    }

    private fun setupRecyclerView() {
        adapter = FoodAdapter(FoodRepository.getAllFoods()) { foodItem ->
            Toast.makeText(context, "Added ${foodItem.title} to cart!", Toast.LENGTH_SHORT).show()
            (activity as? MainActivity)?.updateCartBadge()
        }
        rvFoodList.layoutManager = GridLayoutManager(context, 2)
        rvFoodList.adapter = adapter
        updateList()
    }

    private fun setupCategories() {
        llCategories.removeAllViews()
        for (category in categories) {
            val chip = TextView(context).apply {
                text = category
                textSize = 14f
                setPadding(36, 18, 36, 18)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 16, 0)
                }
                layoutParams = params
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    selectedCategory = category
                    updateCategoryChips()
                    updateList()
                }
            }
            llCategories.addView(chip)
        }
        updateCategoryChips()
    }

    private fun updateCategoryChips() {
        for (i in 0 until llCategories.childCount) {
            val chip = llCategories.getChildAt(i) as TextView
            val cat = categories[i]
            if (cat == selectedCategory) {
                chip.setBackgroundResource(R.drawable.bg_chip_selected)
                chip.setTextColor(resources.getColor(R.color.white, null))
            } else {
                chip.setBackgroundResource(R.drawable.bg_chip_unselected)
                chip.setTextColor(resources.getColor(R.color.text_dark, null))
            }
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateList() {
        val query = etSearch.text.toString().trim()
        val filtered = if (query.isNotEmpty()) {
            FoodRepository.searchFoods(query)
        } else {
            FoodRepository.getFoodsByCategory(selectedCategory)
        }
        adapter.updateData(filtered)
        tvItemCountBadge.text = "${filtered.size} items"
    }

    override fun onResume() {
        super.onResume()
        updateList()
        (activity as? MainActivity)?.updateCartBadge()
    }
}
