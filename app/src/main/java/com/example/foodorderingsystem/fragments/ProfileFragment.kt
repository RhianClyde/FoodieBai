package com.example.foodorderingsystem.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.WelcomeActivity
import com.example.foodorderingsystem.data.CartManager
import com.example.foodorderingsystem.data.FoodRepository
import com.example.foodorderingsystem.data.UserManager

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvUsername = view.findViewById<TextView>(R.id.tv_username)
        val tvEmail = view.findViewById<TextView>(R.id.tv_email)
        val tvAddress = view.findViewById<TextView>(R.id.tv_address)
        val tvPhone = view.findViewById<TextView>(R.id.tv_phone)
        val tvProfileAvatar = view.findViewById<TextView>(R.id.tv_profile_avatar)
        val tvStatOrders = view.findViewById<TextView>(R.id.tv_stat_orders)
        val tvStatFavorites = view.findViewById<TextView>(R.id.tv_stat_favorites)
        val btnLogout = view.findViewById<Button>(R.id.btn_logout)

        val user = UserManager.currentUser
        if (user != null) {
            tvUsername.text = user.username
            tvEmail.text = user.email
            tvAddress.text = user.address
            tvPhone.text = user.phone
            tvProfileAvatar.text = user.username.firstOrNull()?.toString()?.uppercase() ?: "F"
        }

        tvStatOrders.text = CartManager.getOrders().size.toString()
        tvStatFavorites.text = FoodRepository.getFavoriteFoods().size.toString()

        btnLogout.setOnClickListener {
            UserManager.logout()
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}
