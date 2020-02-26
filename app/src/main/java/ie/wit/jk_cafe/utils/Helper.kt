package ie.wit.jk_cafe.utils

import android.app.AlertDialog
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import ie.wit.jk_cafe.R
import kotlinx.android.synthetic.main.loading.view.*

fun createLoader(activity: FragmentActivity):AlertDialog{
    val loaderBuilder = AlertDialog.Builder(activity)
        .setCancelable(true)
        .setView(R.layout.loading)
    val loader = loaderBuilder.create()
    loader.setTitle(R.string.app_name)
    loader.setIcon(R.mipmap.logo_01)

    return loader
}

fun showLoader(loader:AlertDialog, message:String)
{
    if(!loader.isShowing())
    {
        if(message!=null) loader.setTitle(message)
        loader.show()
    }
}

fun hideLoader(loader:AlertDialog)
{
    if(loader.isShowing())
        loader.dismiss()
}

fun serviceUnavailableMessage(activity:FragmentActivity)
{
    Toast.makeText(
        activity, "JK Café service unavailable. Try again later.",
        Toast.LENGTH_LONG
    )
        .show()
}

fun serviceAvailableMessage(activity:FragmentActivity)
{
    Toast.makeText(activity, "JK Café contacted successfully",
        Toast.LENGTH_LONG)
        .show()
}