package ie.wit.jk_cafe.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.main.MainActivity
import ie.wit.jk_cafe.models.OrderModel
import kotlinx.android.synthetic.main.fragment_view_order.view.*
import kotlinx.android.synthetic.main.fragment_view_order.view.viewCupSize
import org.jetbrains.anko.AnkoLogger
import java.util.HashMap

class ViewOrderFragment : Fragment(), AnkoLogger {

    lateinit var app: MainActivity
    lateinit var viewOrderFragment: View
    private var viewOrder: OrderModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainActivity

        arguments?.let {
            viewOrder = it.getParcelable("view_order")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOrderFragment = inflater.inflate(R.layout.fragment_view_order, container, false)

        viewOrderFragment.viewCoffee.text = viewOrder!!.coffee
        viewOrderFragment.viewCupSize.text = viewOrder!!.coffeeCup
        viewOrderFragment.viewQuantity.text = viewOrder!!.quantity.toString()
        viewOrderFragment.viewWhere.text = viewOrder!!.where
        viewOrderFragment.viewTime.text = viewOrder!!.collectTime
        viewOrderFragment.viewTotal.text = viewOrder!!.total

        editOrder(viewOrderFragment)
        setButtonListener(viewOrderFragment)
        return viewOrderFragment
    }

    companion object {
        @JvmStatic
        fun newInstance(order: OrderModel) =
            ViewOrderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("view_order", order)
                }
            }
    }

    private fun editOrder(layout: View)
    {
        layout.editOrderBtn.setOnClickListener{
            fragmentManager?.popBackStack()
        }
    }

    private fun setButtonListener(layout:View) {
        val coffee = viewOrder!!.coffee
        val total = viewOrder!!.total
        val quantity = viewOrder!!.quantity
        val where = viewOrder!!.where
        val coffeeCup = viewOrder!!.coffeeCup
        val request = viewOrder!!.request
        val collectTime = viewOrder!!.collectTime
        val points = 1

        layout.payBtn.setOnClickListener {


            if (layout.cardNum.text.isNotEmpty() || layout.cvsNum.text.isNotEmpty() || layout.cardName.text.isNotEmpty()) {
                writeNewOrder(
                    OrderModel(
                        coffee = coffee, total = total, quantity = quantity, where = where,
                        coffeeCup = coffeeCup, request = request, collectTime = collectTime))

                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.homeFrame, ReceiptsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            } else {
                layout.asterisk01.visibility = View.VISIBLE
                layout.asterisk02.visibility = View.VISIBLE
                layout.asterisk03.visibility = View.VISIBLE
            }
        }
    }


    private fun writeNewOrder(order:OrderModel){
        val uid = app.currentUser!!.uid
        val key = app.database.child("orders").push().key
        order.uid = key
        val orderValues = order.toMap()
        val childUpdates = HashMap<String, Any>()
        childUpdates["/orders/$key"]=orderValues
        childUpdates["/user-orders/$uid/$key"]=orderValues
        app.database.updateChildren(childUpdates)
    }
}