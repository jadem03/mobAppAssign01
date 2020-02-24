package ie.wit.jk_cafe.fragments



import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import ie.wit.jk_cafe.R
import kotlinx.android.synthetic.main.fragment_home.view.orderCoffeeBtn



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
        setButtonListener(homeFragment)
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

    fun setButtonListener(layout: View) {
        layout.orderCoffeeBtn.setOnClickListener {

            //Following code received from stack over flow thread:
            //https://stackoverflow.com/questions/52318195/how-to-change-fragment-kotlin
            var fr = getFragmentManager()?.beginTransaction()
            fr?.replace(R.id.homeFrame, OrderFragment())
            fr?.commit()
        }
    }
}
