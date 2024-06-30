import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.deliveryapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore

class RestaurantMenuFragment : Fragment() {

    private lateinit var rvDishes: RecyclerView
    private lateinit var dishAdapter: DishAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_restaurant_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()

        val ivRestaurantBanner = view.findViewById<ImageView>(R.id.ivRestaurantBanner)
        val tvRestaurantName = view.findViewById<TextView>(R.id.tvRestaurantName)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)
        rvDishes = view.findViewById(R.id.rvDishes)

        // Get shop details from arguments
        val shopName = arguments?.getString("SHOP_NAME") ?: "Unknown Shop"
        val shopImage = arguments?.getString("SHOP_IMAGE")
        val shopId = arguments?.getString("SHOP_ID") ?: ""

        // Set restaurant details
        tvRestaurantName.text = shopName
        shopImage?.let { Glide.with(this).load(it).into(ivRestaurantBanner) }

        // Set up TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Food"))
        tabLayout.addTab(tabLayout.newTab().setText("Items"))
        tabLayout.addTab(tabLayout.newTab().setText("Rating"))

        // Set up RecyclerView
        rvDishes.layoutManager = LinearLayoutManager(context)
        dishAdapter = DishAdapter(emptyList())
        rvDishes.adapter = dishAdapter

        // Fetch dishes for this shop
        fetchDishes(shopId)
    }

    private fun fetchDishes(shopId: String) {
        firestore.collection("shops").document(shopId).collection("dishes")
            .get()
            .addOnSuccessListener { result ->
                val dishes = result.mapNotNull { document ->
                    val name = document.getString("name") ?: return@mapNotNull null
                    val price = document.getLong("price")?.toInt() ?: return@mapNotNull null
                    val ingredients = document.getString("ingredients") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""
                    Dish(name, price, ingredients, imageUrl)
                }
                dishAdapter.updateDishes(dishes)
            }
            .addOnFailureListener { exception ->
                // Handle the error, maybe show a toast or a dialog
            }
    }
}

data class Dish(val name: String, val price: Int, val ingredients: String, val imageUrl: String)

class DishAdapter(private var dishes: List<Dish>) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dish, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        holder.bind(dishes[position])
    }

    override fun getItemCount() = dishes.size

    fun updateDishes(newDishes: List<Dish>) {
        dishes = newDishes
        notifyDataSetChanged()
    }

    class DishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDishName: TextView = itemView.findViewById(R.id.tvDishName)
        private val tvDishPrice: TextView = itemView.findViewById(R.id.tvDishPrice)
        private val tvDishIngredients: TextView = itemView.findViewById(R.id.tvDishIngredients)
        private val ivDish: ImageView = itemView.findViewById(R.id.ivDish)
        private val btnAddToCart: MaterialButton = itemView.findViewById(R.id.btnAddToCart)

        fun bind(dish: Dish) {
            tvDishName.text = dish.name
            tvDishPrice.text = "$${dish.price}"
            tvDishIngredients.text = dish.ingredients
            Glide.with(itemView.context).load(dish.imageUrl).into(ivDish)

            btnAddToCart.setOnClickListener {
                // Handle add to cart action
            }
        }
    }
}