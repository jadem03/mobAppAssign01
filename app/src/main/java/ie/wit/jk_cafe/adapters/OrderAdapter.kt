package ie.wit.jk_cafe.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.jk_cafe.R
import ie.wit.jk_cafe.models.OrderModel
import kotlinx.android.synthetic.main.card_order.view.*


interface OrderListener {
    fun onOrderClick(order: OrderModel)
}

class OrderAdapter constructor(var orders: ArrayList<OrderModel>,
                               private val listener: OrderListener)
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
        holder.bind(order, listener)
    }

    override fun getItemCount(): Int
    {
        return orders.size
    }

    fun removeAt(position: Int)
    {
        orders.removeAt(position)
        notifyItemRemoved(position)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            order: OrderModel, listener: OrderListener
        ) {
            itemView.tag = order
            itemView.where.text = order.where
            itemView.orderQuantity.text = order.quantity
            itemView.coffeeCup.text = order.coffeeCup
            itemView.orderTotal.text = order.total
            itemView.collectTime.text = order.collectTime
            itemView.imageIcon.setImageResource(R.mipmap.coffee_cup02)
            itemView.setOnClickListener{listener.onOrderClick(order)}
        }
    }
}