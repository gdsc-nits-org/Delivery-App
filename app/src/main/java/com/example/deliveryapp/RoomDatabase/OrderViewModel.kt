import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.deliveryapp.RoomDatabase.ADatabase
import com.example.deliveryapp.RoomDatabase.OrderRepository
import com.example.deliveryapp.RoomDatabase.Orders
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: OrderRepository
    val allOrders: LiveData<List<Orders>>

    init {
        val orderDao = ADatabase.getDatabase(application).orderDao()
        repository = OrderRepository(orderDao)
        allOrders = repository.allOrders
    }

    fun insertOrder(order: Orders) = viewModelScope.launch {
        repository.insertOrder(order)
    }

    fun deleteOrderById(id: Long) = viewModelScope.launch {
        repository.deleteOrder(id)
    }

}
