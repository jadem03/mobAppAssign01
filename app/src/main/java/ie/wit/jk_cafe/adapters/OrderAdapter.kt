package ie.wit.jk_cafe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.models.OrderModel
import kotlinx.android.synthetic.main.card_order.view.*
import kotlinx.android.synthetic.main.card_order.view.coffeeCup

interface DeleteListener
{
    fun onDeleteClick(order:OrderModel)
}

class OrderAdapter constructor(private var orders: List<OrderModel>,
                               private val deleteListener: DeleteListener
)
    : RecyclerView.Adapter<OrderAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_order,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val order = orders[holder.adapterPosition]
        holder.bind(order, deleteListener)
    }

    override fun getItemCount(): Int = orders.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            order: OrderModel,
        deleteListener: DeleteListener
        ) {
            itemView.where.text = order.where
            itemView.coffeeCup.text = order.coffeeCup
            itemView.orderTotal.text = order.total
            itemView.collectTime.text = order.collectTime
            itemView.deleteBtn.setOnClickListener{deleteListener.onDeleteClick(order)}
            itemView.imageIcon.setImageResource(R.mipmap.coffee_cup02)

        }
    }
}