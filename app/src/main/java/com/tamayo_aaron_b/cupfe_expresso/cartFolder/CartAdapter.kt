package com.tamayo_aaron_b.cupfe_expresso.cartFolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tamayo_aaron_b.cupfe_expresso.AddToCart
import com.tamayo_aaron_b.cupfe_expresso.R
import java.util.Locale

class CartAdapter(
    private val context: Context,
    var cartItems: MutableList<CartItem>,
    private val tvOverallPrice: TextView,
    private val tvNumberCoffee: TextView) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coffeeName: TextView = itemView.findViewById(R.id.tvAddCoffeeNameValue)
        val selectedSize: TextView = itemView.findViewById(R.id.tvAddSizeValue)
        val quantity: TextView = itemView.findViewById(R.id.tvFavQuantityValue)
        val price: TextView = itemView.findViewById(R.id.tvAddPriceValue)
        val imageView: ImageView = itemView.findViewById(R.id.ivImage)
        val userComment: TextView = itemView.findViewById(R.id.tvComment1)
        val tvMinus: TextView = itemView.findViewById(R.id.tvMinus)
        val tvAdd: TextView = itemView.findViewById(R.id.tvAdd)
        val radioCheck1: ImageView = itemView.findViewById(R.id.radioCheck1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.add_to_cart_layout, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.coffeeName.text = item.coffeeName
        holder.selectedSize.text = "Size: ${item.selectedSize}"
        holder.quantity.text = "${item.quantity}"
        holder.price.text = "₱${String.format("%.2f", item.price)}"
        holder.userComment.text = "``${item.userComment}"

        Glide.with(context).load(item.imageUrl).into(holder.imageView)

        // Decrease quantity
        holder.tvMinus.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity--
                holder.quantity.text = "${item.quantity}"
                updateTotalPrice()
            } else {
                cartItems.removeAt(position) // Remove if quantity is 0
                notifyItemRemoved(position)
                updateTotalPrice()
                tvNumberCoffee.text = cartItems.size.toString() // Update count
            }
        }

        // Increase quantity
        holder.tvAdd.setOnClickListener {
            item.quantity++
            holder.quantity.text = "${item.quantity}"
            updateTotalPrice()
        }



        if (item.isSelected) {
            holder.radioCheck1.setBackgroundResource(R.drawable.circle_checked)
        } else {
            holder.radioCheck1.setBackgroundResource(R.drawable.circle_non_check)
        }

        holder.radioCheck1.setOnClickListener {
            item.isSelected = !item.isSelected
            notifyItemChanged(position) // Update UI
            updateTotalPrice()
        }
    }

    override fun getItemCount(): Int = cartItems.size

    private fun updateTotalPrice() {
        var totalPrice = 0.0

        for (item in cartItems) {
            if (item.isSelected) {
                totalPrice += item.quantity * item.price
            }
        }

        tvOverallPrice.text = "₱${String.format(Locale.getDefault(), "%.2f", totalPrice)}"

        // Check if the cart is empty
        if (cartItems.isEmpty()) {
            (context as? AddToCart)?.showEmptyCartDialog()  // Call the dialog if cart is empty
        }
    }

    fun updateCart(newCartItems: MutableList<CartItem>) {
        cartItems.clear()
        cartItems.addAll(newCartItems)
        notifyDataSetChanged()
        updateTotalPrice()
        tvNumberCoffee.text = "CupFe cart (${cartItems.size})"
    }


    fun setAllItemsChecked(isChecked: Boolean) {
        for (item in cartItems) {
            item.isSelected = isChecked
        }
        notifyDataSetChanged() // Refresh the RecyclerView
        updateTotalPrice()
    }

    fun attachSwipeToDelete(recyclerView: RecyclerView, context: Context) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {  // Allows both left and right swipe
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val itemToRemove = cartItems[position]

                // Call the method to remove the item from persistent storage
                removeItemFromCartStorage(itemToRemove, context)

                // Remove item from the list and notify the adapter
                cartItems.removeAt(position)
                notifyItemRemoved(position)
                updateTotalPrice() // Update total price after removal
                tvNumberCoffee.text = "CupFe cart (${cartItems.size})" // ✅ Update coffee count
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    // Method to remove the item from persistent storage (e.g., SharedPreferences or database)
    private fun removeItemFromCartStorage(item: CartItem, context: Context) {
        val sharedPreferences = context.getSharedPreferences("CartPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Remove the item from the shared preferences by using the item's unique identifier.
        // Assuming CartItem has a unique identifier like `coffeeName` or `id`
        val cartJson = sharedPreferences.getString("cart_items", "[]")
        val cartList: MutableList<CartItem> = Gson().fromJson(cartJson, object : TypeToken<MutableList<CartItem>>() {}.type) ?: mutableListOf()

        // Remove the item from the list
        cartList.remove(item)

        // Save the updated list back to SharedPreferences
        val updatedCartJson = Gson().toJson(cartList)
        editor.putString("cart_items", updatedCartJson)
        editor.apply()
    }



}