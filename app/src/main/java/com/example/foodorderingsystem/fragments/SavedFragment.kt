package com.example.foodorderingsystem.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.adapters.FoodAdapter
import com.example.foodorderingsystem.data.FoodRepository

class SavedFragment : Fragment() {

    private lateinit var rvSaved: RecyclerView
    private lateinit var tvEmptySaved: TextView
    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved, container, false)

        rvSaved = view.findViewById(R.id.rv_saved)
        tvEmptySaved = view.findViewById(R.id.tv_empty_saved)

        adapter = FoodAdapter(emptyList()) { foodItem ->
            Toast.makeText(context, "Added ${foodItem.title} to cart!", Toast.LENGTH_SHORT).show()
        }
        rvSaved.layoutManager = GridLayoutManager(context, 2)
        rvSaved.adapter = adapter

        updateSavedList()

        return view
    }

    private fun updateSavedList() {
        val savedFoods = FoodRepository.getFavoriteFoods()
        if (savedFoods.isEmpty()) {
            tvEmptySaved.visibility = View.VISIBLE
            rvSaved.visibility = View.GONE
        } else {
            tvEmptySaved.visibility = View.GONE
            rvSaved.visibility = View.VISIBLE
            adapter.updateData(savedFoods)
        }
    }

    override fun onResume() {
        super.onResume()
        updateSavedList()
    }
}
