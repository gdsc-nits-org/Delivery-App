import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deliveryapp.R
import com.example.deliveryapp.RoomDatabase.OrderDao
import com.example.deliveryapp.models.CartItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartAdapter(private var cartItems: MutableList<CartItem>, private val orderDao: OrderDao) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.item_name)
        val imageView = itemView.findViewById<ImageView>(R.id.item_image)
        val quantityTextView: TextView = itemView.findViewById(R.id.item_quantity)
        val priceTextView: TextView = itemView.findViewById(R.id.item_price)
        val incrementer = itemView.findViewById<ImageButton>(R.id.btn_increase)
        val decrementer = itemView.findViewById<ImageButton>(R.id.btn_decrease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        Glide.with(holder.imageView)
            .load(cartItem.imageUrl)
            .into(holder.imageView)
        holder.idTextView.text = "ID: ${cartItem.title}"
        holder.quantityTextView.text = "Quantity: ${cartItem.quantity}"
        holder.priceTextView.text = "₹${(cartItem.price * cartItem.quantity)}"

        // Increment button click handler
        holder.incrementer.setOnClickListener {
            val newQuantity = cartItem.quantity + 1
            CoroutineScope(Dispatchers.IO).launch {
                updateQuantityInDatabase(cartItem, newQuantity)
            }
            cartItems[position].quantity = newQuantity // Update quantity in the list
            notifyItemChanged(position) // Notify adapter of the change
        }

        // Decrement button click handler
        holder.decrementer.setOnClickListener {
            if (cartItem.quantity > 1) { // Prevent quantity from going below 1
                val newQuantity = cartItem.quantity - 1
                CoroutineScope(Dispatchers.IO).launch {
                    updateQuantityInDatabase(cartItem, newQuantity)
                }
                cartItems[position].quantity = newQuantity // Update quantity in the list
                notifyItemChanged(position) // Notify adapter of the change
            } else if (cartItem.quantity == 1) {
                // Delete the order if quantity is 1
                CoroutineScope(Dispatchers.IO).launch {
                    deleteOrder(cartItem, position)
                }
            }
        }
    }

    private suspend fun deleteOrder(cartItem: CartItem, position: Int) {
        orderDao.deleteOrderById(cartItem.id) // Delete from the database
        // Update the list and notify the adapter on the main thread
        withContext(Dispatchers.Main) {
            cartItems.removeAt(position) // Remove the item from the list
            notifyItemRemoved(position) // Notify adapter to remove the item from UI
        }
    }

    private suspend fun updateQuantityInDatabase(cartItem: CartItem, newQuantity: Int) {
        cartItem.quantity = newQuantity
        orderDao.update(cartItem.id, newQuantity)
    }

    override fun getItemCount() = cartItems.size

    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems.toMutableList()
        notifyDataSetChanged()
    }

    fun getTotalPrice(): Int {
        return cartItems.sumOf { it.price * it.quantity }
    }
}
