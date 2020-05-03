package ie.wit.jk_cafe.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.main.MainActivity
import kotlinx.android.synthetic.main.home_fragment.view.*
import java.time.LocalTime

class FragmentHome : Fragment(){

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
        val home = inflater.inflate(R.layout.home_fragment, container, false)
        activity?.title = getString(R.string.action_order)

        buttonListener(home)
        return home
        }

    private fun buttonListener(layout:View) {
        val currentTime = LocalTime.now()
        val openTime = LocalTime.of(7, 0, 10)
        val closeTime = LocalTime.of(18, 0, 10)

            layout.orderCoffeeBtn.setOnClickListener {
            if (currentTime > openTime && currentTime < closeTime) {

                var fr = fragmentManager?.beginTransaction()
                fr?.replace(R.id.homeFrame, OrderFragment())
                fr?.commit()
            }
            else
            {
                val dialogBox = AlertDialog.Builder(activity)
                dialogBox.setTitle("Sorry!")
                dialogBox.setMessage("We're Closed!" +
                        "Our open times are 07am - 06pm Monday - Sunday.")

                dialogBox.setNeutralButton("OK") {dialog, which ->
                    dialog.cancel()
                }
                val myDialog = dialogBox.create()
                myDialog.show()

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentHome().apply {
                arguments = Bundle().apply {
                }
            }
    }
}