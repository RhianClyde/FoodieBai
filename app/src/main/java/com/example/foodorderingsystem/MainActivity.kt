package com.example.foodorderingsystem

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.foodorderingsystem.data.CartManager
import com.example.foodorderingsystem.fragments.CartFragment
import com.example.foodorderingsystem.fragments.HomeFragment
import com.example.foodorderingsystem.fragments.MenuFragment
import com.example.foodorderingsystem.fragments.OrdersFragment
import com.example.foodorderingsystem.fragments.ProfileFragment
import com.example.foodorderingsystem.fragments.SavedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    private val homeFragment = HomeFragment()
    private val menuFragment = MenuFragment()
    private val savedFragment = SavedFragment()
    private val cartFragment = CartFragment()
    private val ordersFragment = OrdersFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_navigation)

        loadFragment(homeFragment)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> loadFragment(homeFragment)
                R.id.nav_menu -> loadFragment(menuFragment)
                R.id.nav_saved -> loadFragment(savedFragment)
                R.id.nav_cart -> loadFragment(cartFragment)
                R.id.nav_orders -> loadFragment(ordersFragment)
                R.id.nav_profile -> loadFragment(profileFragment)
                else -> false
            }
        }

        updateCartBadge()
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        // UI UPDATE v2: Smooth fragment transition animations
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in_fast, R.anim.fade_out_fast)
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }

    fun selectBottomTab(itemId: Int) {
        bottomNav.selectedItemId = itemId
    }

    fun updateCartBadge() {
        val cartCount = CartManager.getCartCount()
        val badge = bottomNav.getOrCreateBadge(R.id.nav_cart)
        if (cartCount > 0) {
            badge.isVisible = true
            badge.number = cartCount
            badge.backgroundColor = ContextCompat.getColor(this, R.color.primary_green)
        } else {
            badge.isVisible = false
        }
    }
}
