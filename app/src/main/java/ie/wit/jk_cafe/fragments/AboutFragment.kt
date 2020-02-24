package ie.wit.jk_cafe.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ie.wit.jk_cafe.R

class AboutFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val aboutFragment = inflater.inflate(R.layout.fragment_about, container, false)
        activity?.title = getString(R.string.action_about)

        return aboutFragment
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AboutFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}