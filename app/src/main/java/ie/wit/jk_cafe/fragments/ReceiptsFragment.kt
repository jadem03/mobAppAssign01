package ie.wit.jk_cafe.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.adapters.OrderAdapter
import ie.wit.jk_cafe.adapters.OrderListener
import ie.wit.jk_cafe.main.MainActivity
import ie.wit.jk_cafe.models.OrderModel
import ie.wit.jk_cafe.utils.SwipeToDeleteCallback
import ie.wit.jk_cafe.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.fragment_receipts.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.security.Timestamp
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class ReceiptsFragment : Fragment(), AnkoLogger, OrderListener {

    lateinit var app: MainActivity
    lateinit var receiptsFragment: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        receiptsFragment = inflater.inflate(R.layout.fragment_receipts, container, false)
        activity?.title = getString(R.string.action_receipts)

        receiptsFragment.recyclerView.setLayoutManager(LinearLayoutManager(activity))

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val adapter = receiptsFragment.recyclerView.adapter as OrderAdapter

                adapter.removeAt(viewHolder.adapterPosition)
                deleteOrder((viewHolder.itemView.tag as OrderModel).uid)
                deleteUserOrder(app.auth.currentUser!!.uid,
                    (viewHolder.itemView.tag as OrderModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(receiptsFragment.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onOrderClick(viewHolder.itemView.tag as OrderModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(receiptsFragment.recyclerView)

        return receiptsFragment
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReceiptsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    fun setSwipeRefresh() {
        receiptsFragment.swiperefresh.setOnRefreshListener {
            receiptsFragment.swiperefresh.isRefreshing = true
            getAllOrders(app.auth.currentUser!!.uid)
        }
    }

    fun checkSwipeRefresh() {
        if (receiptsFragment.swiperefresh.isRefreshing) receiptsFragment.swiperefresh.isRefreshing = false
    }

    fun deleteUserOrder(userId: String, uid:String?) {
        app.database.child("user-orders").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Order error : ${error.message}")
                    }
                })
    }

    fun deleteOrder(uid: String?) {
        app.database.child("orders").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Order error : ${error.message}")
                    }
                })
    }

    override fun onOrderClick(order: OrderModel) {
        
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditOrderFragment.newInstance(order))
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        getAllOrders(app.auth.currentUser!!.uid)
    }

    private fun getAllOrders(userId:String)
    {
        val orderList = ArrayList<OrderModel>()
        app.database.child("user-orders").child(userId)
            .addValueEventListener(object: ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot.children
                    children.forEach{
                        val order = it.getValue(OrderModel::class.java)

                        orderList.add(order!!)

                        receiptsFragment.recyclerView.adapter =
                            OrderAdapter(orderList, this@ReceiptsFragment)
                        receiptsFragment.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()
                        app.database.child("user-orders").child(userId).removeEventListener(this)
                    }
                }
            })
    }
}
