package ie.wit.jk_cafe.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.util.*
import java.util.Calendar
import android.content.Intent

class OrderFragment : Fragment(), AnkoLogger {

    lateinit var app: MainActivity
    var order = OrderModel()
    
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
        //makes the time 15 minutes ahead
        calendar.add(Calendar.MINUTE, 15)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        layout.collectTime.setOnClickListener()
        {
            val clock = TimePickerDialog(activity,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    if(!(hourOfDay <= 7 || hourOfDay >= 18
                                || hourOfDay < hour))
                    {
                        val time = String.format("$hourOfDay:%02d", minute)
                        layout.collectTime.setText(time)
                    }
                    else
                    {
                        val dialogBox = AlertDialog.Builder(activity)
                        dialogBox.setMessage("Our open times are 07am - 06pm Monday to Sunday." +
                                "Please select another time")
                        dialogBox.setNeutralButton("OK") {dialog, which ->
                            dialog.cancel() }
                        val myDialog = dialogBox.create()
                        myDialog.show()
                    }
                }, hour, minute, true)
            clock.show()
        }
    }

    private fun setButtonListener(layout:View) {
        layout.orderBtn.setOnClickListener {
            
            order.total = ("€"+layout.americano_quantity.value * 2.5+"0")
            order.quantity = layout.americano_quantity.value
            order.editText = layout.editText.text.toString()
            order.where = if (layout.where.checkedRadioButtonId == R.id.sitIn) "Sit In" else "Take Away"
            order.coffeeCup = if (layout.coffeeCup.checkedRadioButtonId == R.id.small) "Small" else "Large"
            order.collectTime = layout.collectTime.text.toString()

            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.homeFrame, ViewOrderFragment.newInstance(order))
                .addToBackStack(null)
                .commit()
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

}
