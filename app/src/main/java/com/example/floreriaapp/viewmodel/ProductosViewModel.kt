package com.example.floreriaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.floreriaapp.Flor
import com.example.floreriaapp.repository.CarritoRepository
import com.example.floreriaapp.repository.ProductosRepository

class ProductosViewModel(
    private val productosRepository: ProductosRepository,
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    private val _flores = MutableLiveData<List<Flor>>()
    val flores: LiveData<List<Flor>> = _flores

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    fun cargarFlores() {
        productosRepository.obtenerFlores { lista, throwable ->
            if (lista != null) {
                _flores.postValue(lista!!)
                _mensaje.postValue("Productos cargados")
            } else {
                _error.postValue(throwable?.message ?: "Error desconocido")
            }
        }
    }

    fun agregarAlCarrito(flor: Flor) {
        carritoRepository.agregarAlCarrito(
            flor.nombre,
            flor.precio.toDouble(),
            flor.imagenNombre,
            flor.cantidadSeleccionada
        )
        _mensaje.value = "${flor.cantidadSeleccionada} x ${flor.nombre} agregado al carrito"
    }
}

class ProductosViewModelFactory(
    private val productosRepository: ProductosRepository,
    private val carritoRepository: CarritoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductosViewModel(productosRepository, carritoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
