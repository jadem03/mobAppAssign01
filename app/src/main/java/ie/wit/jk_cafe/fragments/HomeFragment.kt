package ie.wit.jk_cafe.fragments



import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import ie.wit.jk_cafe.R
import kotlinx.android.synthetic.main.fragment_home.view.orderCoffeeBtn
import org.jetbrains.anko.toast
import java.time.LocalTime
import java.util.*

class HomeFragment : Fragment() {

    lateinit var ft: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val homeFragment = inflater.inflate(R.layout.fragment_home, container, false)
        activity?.title = getString(R.string.action_home)

        buttonListener(homeFragment)
        return homeFragment
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun buttonListener(layout: View) {
        val currentTime = LocalTime.now()
        val openTime = LocalTime.of(7, 0, 10)
        val closeTime = LocalTime.of(22, 0, 10)

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
}
