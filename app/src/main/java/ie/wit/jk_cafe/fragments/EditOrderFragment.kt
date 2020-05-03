package ie.wit.jk_cafe.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.main.MainActivity
import ie.wit.jk_cafe.models.OrderModel
import kotlinx.android.synthetic.main.fragment_edit_order.view.*
import kotlinx.android.synthetic.main.fragment_edit_order.view.edit_where
import kotlinx.android.synthetic.main.fragment_order.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

class EditOrderFragment : Fragment(), AnkoLogger {

    lateinit var app: MainActivity
    var editOrder: OrderModel? = null

    val coffeeType = arrayOf("Americano", "Latte",
        "Cappacino", "Flat White", "Hot Chocolate", "Tea")

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
        val editOrderFragment = inflater.inflate(R.layout.fragment_edit_order, container, false)
        activity?.title = getString(R.string.action_edit)

        editOrderFragment.updateBtn.setOnClickListener{
            updateOrderData(editOrderFragment)
            updateOrder(editOrder!!.uid, editOrder!!)
            updateUserOrder(app.currentUser!!.uid,
                editOrder!!.uid, editOrder!!)
        }

        editOrderFragment.edit_quantity.value = editOrder!!.quantity
        editOrderFragment.edit_editText.setText(editOrder!!.editText)
        editOrderFragment.edit_collectTime.setText(editOrder!!.collectTime)

        setQuantity(editOrderFragment)
        setOrderTime(editOrderFragment)
        setCoffeeType(editOrderFragment)

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

    private fun setOrderTime(layout: View) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        layout.edit_collectTime.setOnClickListener()
        {
            val clock = TimePickerDialog(activity,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    if (hourOfDay > hour) {
                        var time = String.format("$hourOfDay:%02d", minute)
                        layout.edit_collectTime.setText(time)
                    } else {
                        Toast.makeText(activity, R.string.error, Toast.LENGTH_SHORT).show()
                        var time = String.format("$hour:$minute")
                        layout.edit_collectTime.setText(time)
                    }
                }, hour, minute, true)
            clock.show()
        }
    }

    private fun setCoffeeType(layout: View){
        val spinner = layout.spinner
        spinner?.adapter = activity?.applicationContext?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, coffeeType) } as SpinnerAdapter
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("error")
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val coffee = parent?.getItemAtPosition(position).toString()
                println(coffee)
                editOrder!!.coffee = coffee
            }
        }
    }

    private fun setQuantity(layout: View)
    {
        layout.edit_quantity.minValue = 1
        layout.edit_quantity.maxValue = 5
        var orderTotal = layout.edit_quantity.minValue * 2.5
        layout.edit_total.setText("€"+"$orderTotal"+"0")
        layout.edit_quantity.setOnValueChangedListener { picker, oldVal, newVal ->
            orderTotal = newVal * 2.5
            layout.edit_total.text = "€"+"$orderTotal"+"0"
        }
    }

    fun updateOrderData(layout: View){

        editOrder!!.collectTime = layout.edit_collectTime.text.toString()
        editOrder!!.quantity = layout.edit_quantity.value
        editOrder!!.total = ("€"+layout.edit_quantity.value * 2.5+"0")
        editOrder!!.where = if (layout.edit_where.checkedRadioButtonId == R.id.sitIn) "Sit In" else "Take Away"
        editOrder!!.coffeeCup = if (layout.edit_coffeeCup.checkedRadioButtonId == R.id.small) "Small" else "Large"
    }

    private fun updateUserOrder(userId: String, uid: String?, order: OrderModel) {
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

    private fun updateOrder(uid: String?,order:OrderModel) {
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
