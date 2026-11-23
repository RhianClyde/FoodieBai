package com.example.foodorderingsystem.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingsystem.MainActivity
import com.example.foodorderingsystem.R
import com.example.foodorderingsystem.adapters.CartAdapter
import com.example.foodorderingsystem.data.CartManager
import com.example.foodorderingsystem.data.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class CartFragment : Fragment() {

    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvDeliveryFee: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var tvEmptyCart: TextView
    private lateinit var btnCheckout: Button
    private lateinit var cardSummary: View
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        rvCartItems = view.findViewById(R.id.rv_cart_items)
        tvSubtotal = view.findViewById(R.id.tv_subtotal)
        tvDeliveryFee = view.findViewById(R.id.tv_delivery_fee)
        tvTotalPrice = view.findViewById(R.id.tv_total_price)
        tvEmptyCart = view.findViewById(R.id.tv_empty_cart)
        btnCheckout = view.findViewById(R.id.btn_checkout)
        cardSummary = view.findViewById(R.id.card_summary)

        adapter = CartAdapter(CartManager.getCartItems()) {
            updateCartUI()
        }
        rvCartItems.layoutManager = LinearLayoutManager(context)
        rvCartItems.adapter = adapter

        btnCheckout.setOnClickListener {
            submitSingleOrderToFirestore()
        }

        updateCartUI()

        return view
    }

    private fun submitSingleOrderToFirestore() {
        // 1. Validate that customer is signed in
        val firebaseAuthUser = FirebaseAuth.getInstance().currentUser
        val appUser = UserManager.currentUser

        if (!UserManager.isLoggedIn() && firebaseAuthUser == null) {
            Toast.makeText(requireContext(), "Please sign in to place an order.", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = firebaseAuthUser?.uid ?: appUser?.username ?: ""
        val customerName = appUser?.username ?: firebaseAuthUser?.displayName ?: "Customer"
        val customerEmail = appUser?.email ?: firebaseAuthUser?.email ?: "customer@foodiebai.com"

        if (userId.isEmpty() || customerName.isEmpty() || customerEmail.isEmpty()) {
            Toast.makeText(requireContext(), "Customer information is incomplete.", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Validate that food items are selected
        val cartItems = CartManager.getCartItems()
        if (cartItems.isEmpty()) {
            Toast.makeText(requireContext(), "Your cart is empty! Please select a food item.", Toast.LENGTH_SHORT).show()
            return
        }

        val firstItem = cartItems.first()
        val foodId = cartItems.joinToString(",") { it.foodItem.id }
        val foodName = cartItems.joinToString(", ") { "${it.quantity}x ${it.foodItem.title}" }

        if (foodId.isEmpty() || foodName.isEmpty()) {
            Toast.makeText(requireContext(), "Food item details are invalid.", Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Validate quantity >= 1
        val totalQuantity = cartItems.sumOf { it.quantity }
        if (totalQuantity <= 0) {
            Toast.makeText(requireContext(), "Quantity must be at least 1.", Toast.LENGTH_SHORT).show()
            return
        }

        // 4. Calculate unitPrice and totalAmount (totalAmount = unitPrice * quantity)
        val unitPrice = firstItem.foodItem.price.toDouble()
        val totalAmount = CartManager.getTotalPrice().toDouble()

        if (unitPrice <= 0.0 || totalAmount <= 0.0) {
            Toast.makeText(requireContext(), "Price and total amount cannot be zero.", Toast.LENGTH_SHORT).show()
            return
        }

        // PREVENT DOUBLE TAPS: Disable Place Order button during submission
        btnCheckout.isEnabled = false
        btnCheckout.text = "Submitting Order..."

        val orderId = "ORD-" + System.currentTimeMillis().toString().takeLast(6)

        // SINGLE FIRESTORE DOCUMENT (orders/{orderId})
        val singleOrderDoc = hashMapOf(
            "orderId" to orderId,
            "userId" to userId,
            "customerName" to customerName,
            "customerEmail" to customerEmail,
            "foodId" to foodId,
            "foodName" to foodName,
            "quantity" to totalQuantity,
            "unitPrice" to unitPrice,
            "totalAmount" to totalAmount,
            "orderedAt" to FieldValue.serverTimestamp(),
            "status" to "Pending"
        )

        val db = FirebaseFirestore.getInstance()

        // ONE set() operation ONLY to orders/{orderId}
        db.collection("orders")
            .document(orderId)
            .set(singleOrderDoc)
            .addOnSuccessListener {
                btnCheckout.isEnabled = true
                btnCheckout.text = "Place Order"
                Toast.makeText(requireContext(), "Order submitted successfully!", Toast.LENGTH_SHORT).show()

                val localOrder = CartManager.placeOrder(orderId)
                if (localOrder != null) {
                    showOrderSuccessDialog(localOrder.orderId, localOrder.totalPrice)
                } else {
                    updateCartUI()
                }
            }
            .addOnFailureListener { e ->
                btnCheckout.isEnabled = true
                btnCheckout.text = "Place Order"
                Toast.makeText(requireContext(), "Failed to submit order: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun showOrderSuccessDialog(orderId: String, totalPrice: Int) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_order_success)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val tvOrderRef = dialog.findViewById<TextView>(R.id.tv_order_ref)
        val tvOrderAmount = dialog.findViewById<TextView>(R.id.tv_order_amount)
        val btnTrackOrder = dialog.findViewById<Button>(R.id.btn_track_order)
        val btnContinueShopping = dialog.findViewById<Button>(R.id.btn_continue_shopping)

        tvOrderRef.text = orderId
        tvOrderAmount.text = "₱$totalPrice"

        btnTrackOrder.setOnClickListener {
            dialog.dismiss()
            (activity as? MainActivity)?.selectBottomTab(R.id.nav_orders)
        }

        btnContinueShopping.setOnClickListener {
            dialog.dismiss()
            (activity as? MainActivity)?.selectBottomTab(R.id.nav_home)
        }

        dialog.show()
        updateCartUI()
    }

    private fun updateCartUI() {
        val items = CartManager.getCartItems()
        if (items.isEmpty()) {
            tvEmptyCart.visibility = View.VISIBLE
            rvCartItems.visibility = View.GONE
            cardSummary.visibility = View.GONE
        } else {
            tvEmptyCart.visibility = View.GONE
            rvCartItems.visibility = View.VISIBLE
            cardSummary.visibility = View.VISIBLE
            adapter.updateData(items)

            val subtotal = CartManager.getSubtotal()
            tvSubtotal.text = "₱$subtotal"
            tvDeliveryFee.text = "₱${CartManager.deliveryFee}"
            tvTotalPrice.text = "₱${CartManager.getTotalPrice()}"
        }
        (activity as? MainActivity)?.updateCartBadge()
    }

    override fun onResume() {
        super.onResume()
        updateCartUI()
    }
}
