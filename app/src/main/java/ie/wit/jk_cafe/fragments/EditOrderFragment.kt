package ie.wit.jk_cafe.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.main.MainActivity
import ie.wit.jk_cafe.models.OrderModel
import kotlinx.android.synthetic.main.fragment_edit_order.*
import kotlinx.android.synthetic.main.fragment_edit_order.view.*
import kotlinx.android.synthetic.main.fragment_edit_order.view.edit_where
import kotlinx.android.synthetic.main.fragment_order.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*
import android.widget.RadioGroup
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_order.view.coffeeCup

class EditOrderFragment : Fragment(), AnkoLogger {

    lateinit var app: MainActivity
    lateinit var editOrderFragment: View
    var editOrder: OrderModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainActivity

        arguments?.let {
            editOrder = it.getParcelable("edit_order")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editOrderFragment = inflater.inflate(R.layout.fragment_edit_order, container, false)
        activity?.title = getString(R.string.action_edit)

        editOrderFragment.edit_americano_quantity.minValue = 1
        editOrderFragment.edit_americano_quantity.maxValue = 5
        var orderTotal = edit_americano_quantity.minValue * 2.5
        editOrderFragment.total.setText("€"+"$orderTotal"+"0")
        editOrderFragment.edit_americano_quantity.setOnValueChangedListener { picker, oldVal, newVal ->
            val orderTotal = newVal * 2.5
            editOrderFragment.edit_total.setText("€"+"$orderTotal"+"0")
        }

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        editOrderFragment.edit_collectTime.setOnClickListener()
        {
            val clock = TimePickerDialog(activity,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    if(hourOfDay>hour)
                    {
                        var time = String.format("$hourOfDay:%02d", minute)
                        editOrderFragment.edit_collectTime.setText(time)
                    }
                    else
                    {
                        Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
                        var time = String.format("$hour:$minute")
                        editOrderFragment.edit_collectTime.setText(time) }
                }, hour, minute, true)
            clock.show()
        }

        editOrderFragment.edit_americano_quantity.value = editOrder!!.quantity
        editOrderFragment.edit_collectTime.setText(editOrder!!.collectTime)
        

        editOrderFragment.updateBtn.setOnClickListener{
            updateOrderData()
            updateOrder(editOrder!!.uid, editOrder!!)
            updateUserOrder(app.auth.currentUser!!.uid,
                editOrder!!.uid, editOrder!!)
        }
        return editOrderFragment
    }

    companion object {
        @JvmStatic
        fun newInstance(order: OrderModel) =
            EditOrderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("edit_order",order) }
            }
    }

    fun updateOrderData(){

        editOrder!!.collectTime = edit_collectTime.text.toString()
        editOrder!!.quantity = editOrderFragment.edit_americano_quantity.value
        editOrder!!.total = ("€"+editOrderFragment.edit_americano_quantity.value * 2.5+"0")
        editOrder!!.where = if (editOrderFragment.edit_where.checkedRadioButtonId == R.id.sitIn) "Sit In" else "Take Away"
        editOrder!!.coffeeCup = if (editOrderFragment.edit_coffeeCup.checkedRadioButtonId == R.id.small) "Small" else "Large"
    }

    fun updateUserOrder(userId: String, uid: String?, order: OrderModel) {
        app.database.child("user-orders").child(userId).child(uid!!.toString())
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(order)
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.homeFrame, ReceiptsFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Order error : ${error.message}")
                    }
                })
    }

    fun updateOrder(uid: String?,order:OrderModel) {
        app.database.child("orders").child(uid!!.toString())
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(order)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Order error : ${error.message}")
                    }
                })
    }
}
