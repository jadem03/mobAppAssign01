package ie.wit.jk_cafe.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.main.MainActivity
import ie.wit.jk_cafe.models.OrderModel
import kotlinx.android.synthetic.main.fragment_order.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import java.util.regex.Pattern

class ViewOrderFragment : Fragment(), AnkoLogger {

    lateinit var app: MainActivity
    var displayOrder: OrderModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainActivity
        arguments?.let {
            displayOrder = it.getParcelable("displayOrder")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val viewOrderFragment = inflater.inflate(R.layout.fragment_view_order, container, false)
        return viewOrderFragment
    }


    companion object {
        @JvmStatic
        fun newInstance(order: OrderModel) =
            ViewOrderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("displayOrder", order)
                }
            }
    }
}