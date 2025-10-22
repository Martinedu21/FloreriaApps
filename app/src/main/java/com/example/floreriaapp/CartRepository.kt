package com.example.floreriaapp

// Objeto Singleton para gestionar el estado del carrito y las compras.
object CartRepository {
    // La lista de flores que representa el carrito actual. Es mutable para poder agregar y quitar items.
    val carrito = mutableListOf<Flor>()

    // Guarda la lista de la última compra para mostrarla en el recibo.
    var lastPurchase = listOf<Flor>()
}
