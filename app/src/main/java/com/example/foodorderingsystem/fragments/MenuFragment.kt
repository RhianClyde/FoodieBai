package com.example.foodorderingsystem.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.adapters.FoodAdapter
import com.example.foodorderingsystem.data.FoodRepository

class MenuFragment : Fragment() {

    private lateinit var rvMenu: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var adapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        rvMenu = view.findViewById(R.id.rv_menu)
        etSearch = view.findViewById(R.id.et_search)

        adapter = FoodAdapter(FoodRepository.getAllFoods()) { foodItem ->
            Toast.makeText(context, "Added ${foodItem.title} to cart!", Toast.LENGTH_SHORT).show()
        }
        rvMenu.layoutManager = GridLayoutManager(context, 2)
        rvMenu.adapter = adapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                adapter.updateData(FoodRepository.searchFoods(query))
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        adapter.updateData(FoodRepository.getAllFoods())
    }
}
