package ie.wit.jk_cafe.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ie.wit.jk_cafe.R
import kotlinx.android.synthetic.main.activity_make_order.*
import kotlinx.android.synthetic.main.activity_make_order.view.*
import java.util.*

class Order : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_order)

        americano_quantity.minValue = 0
        americano_quantity.maxValue = 5

        americano_quantity.setOnValueChangedListener { amerPicker, amerOldVal, amerNewVal ->
            val total = amerNewVal * 2.5
            orderTotal.setText("€" + "$total" + "0")
        }

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        collectTime.setOnClickListener()
        {
            val clock = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    var time = String.format("$hourOfDay:%02d", minute)
                    collectTime.setText(time)
                },
                hour,
                minute,
                true
            )

            clock.show()
        }

        orderBtn.setOnClickListener()
        {


        }
    }
}