package ie.wit.jk_cafe.models

interface OrderStore {
    fun findAll() : List<OrderModel>
    fun findById(id: Long) : OrderModel?
    fun create(order: OrderModel)
    fun delete(order:OrderModel)
}