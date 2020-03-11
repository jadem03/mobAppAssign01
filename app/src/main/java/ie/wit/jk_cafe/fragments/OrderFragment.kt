package ie.wit.jk_cafe.fragments

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ValueEventListener
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.main.MainActivity
import ie.wit.jk_cafe.models.OrderModel
import kotlinx.android.synthetic.main.fragment_order.view.*
import kotlinx.android.synthetic.main.fragment_order.view.americano_quantity
import kotlinx.android.synthetic.main.fragment_order.view.coffeeCup
import kotlinx.android.synthetic.main.fragment_order.view.collectTime
import kotlinx.android.synthetic.main.fragment_order.view.total
import kotlinx.android.synthetic.main.fragment_order.view.where
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

class OrderFragment : Fragment(), AnkoLogger {

    lateinit var app: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app=activity?.application as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val orderFragment = inflater.inflate(R.layout.fragment_order, container, false)
        activity?.title = getString(R.string.action_order)
        setQuantity(orderFragment)
        setOrderTime(orderFragment)
        setButtonListener(orderFragment)
        return orderFragment
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OrderFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun setQuantity(layout: View)
    {
        layout.americano_quantity.minValue = 1
        layout.americano_quantity.maxValue = 5
        var orderTotal = layout.americano_quantity.minValue * 2.5
        layout.total.setText("€"+"$orderTotal"+"0")
        layout.americano_quantity.setOnValueChangedListener { picker, oldVal, newVal ->
            orderTotal = newVal * 2.5
            layout.total.setText("€"+"$orderTotal"+"0")
        }
    }

    private fun setOrderTime(layout: View)
    {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        layout.collectTime.setOnClickListener()
        {
            val clock = TimePickerDialog(activity,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    if(hourOfDay>hour)
                    {
                        var time = String.format("$hourOfDay:%02d", minute)
                        layout.collectTime.setText(time)
                    }
                    else
                    {
                        Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
                        var time = String.format("$hour:$minute")
                        layout.collectTime.setText(time) }
                }, hour, minute, true)
            clock.show()
        }
    }

    private fun setButtonListener(layout:View) {
        layout.orderBtn.setOnClickListener {
            val total = ("€"+layout.americano_quantity.value * 2.5+"0")
            val quantity = layout.americano_quantity.value
            val where = if (layout.where.checkedRadioButtonId == R.id.sitIn) "Sit In" else "Take Away"
            var coffeeCup = if (layout.coffeeCup.checkedRadioButtonId == R.id.small) "Small" else "Large"
            val collectTime = layout.collectTime.text.toString()
            writeNewOrder(OrderModel(total = total, quantity = quantity, where = where,
                coffeeCup = coffeeCup, collectTime = collectTime, email = app.auth.currentUser!!.email)
            )
            var fr = getFragmentManager()?.beginTransaction()
            fr?.replace(R.id.homeFrame, ReceiptsFragment())
            fr?.commit()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        app.database.child("user-orders")
            .child(app.auth.currentUser!!.uid)
    }

    private fun writeNewOrder(order:OrderModel){
        val uid = app.auth.currentUser!!.uid
        val key = app.database.child("orders").push().key
        order.uid = key
        val orderValues = order.toMap()
        val childUpdates = HashMap<String, Any>()
        childUpdates["/orders/$key"]=orderValues
        childUpdates["/user-orders/$uid/$key"]=orderValues
        app.database.updateChildren(childUpdates)
    }

}
