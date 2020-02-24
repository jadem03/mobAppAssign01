package ie.wit.jk_cafe.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.adapters.DeleteListener
import ie.wit.jk_cafe.adapters.OrderAdapter
import ie.wit.jk_cafe.main.MainActivity
import ie.wit.jk_cafe.models.OrderModel
import kotlinx.android.synthetic.main.fragment_receipts.*
import kotlinx.android.synthetic.main.fragment_receipts.view.recyclerView

class ReceiptsFragment : Fragment(), DeleteListener {

    lateinit var app: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val receiptsFragment = inflater.inflate(R.layout.fragment_receipts, container, false)
        receiptsFragment.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        receiptsFragment.recyclerView.adapter = OrderAdapter(app.ordersStore.findAll(), this)
        return receiptsFragment
    }

    override fun onDeleteClick(order: OrderModel) {
        app.ordersStore.delete(order)
        app.ordersStore.findAll()
        var fr = getFragmentManager()?.beginTransaction()
        fr?.replace(R.id.homeFrame, ReceiptsFragment())
        fr?.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReceiptsFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

}
