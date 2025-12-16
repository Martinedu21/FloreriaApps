package com.example.floreriaapp.ui.theme.carrito

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.floreriaapp.model.FlorEnCarrito
import com.example.floreriaapp.repository.CarritoRepository

class CarritoViewModel(private val repository: CarritoRepository) : ViewModel() {

    private val _carrito = MutableLiveData<List<FlorEnCarrito>>()
    val carrito: LiveData<List<FlorEnCarrito>> = _carrito

    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    fun cargarCarrito() {
        val items = repository.obtenerCarrito()
        _carrito.value = items
        calcularTotal(items)
    }

    fun vaciarCarrito() {
        repository.vaciarCarrito()
        cargarCarrito() // Actualiza la lista (ahora vacía)
    }
    
    fun eliminarCantidad(id: Int, cantidad: Int) {
        repository.eliminarCantidad(id, cantidad)
        cargarCarrito() // Actualiza la lista y el total
    }

    private fun calcularTotal(items: List<FlorEnCarrito>) {
        val suma = items.sumOf { it.precio * it.cantidad }
        _total.value = suma
    }
}

// Factory para inyectar el repositorio en el ViewModel
class CarritoViewModelFactory(private val repository: CarritoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarritoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
