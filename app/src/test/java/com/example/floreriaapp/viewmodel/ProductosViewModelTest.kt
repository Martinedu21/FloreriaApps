package com.example.floreriaapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.floreriaapp.Flor
import com.example.floreriaapp.repository.CarritoRepository
import com.example.floreriaapp.repository.ProductosRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

class ProductosViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var productosRepository: ProductosRepository

    @Mock
    private lateinit var carritoRepository: CarritoRepository

    @Mock
    private lateinit var floresObserver: Observer<List<Flor>>

    @Mock
    private lateinit var errorObserver: Observer<String>

    @Mock
    private lateinit var mensajeObserver: Observer<String>

    private lateinit var viewModel: ProductosViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = ProductosViewModel(productosRepository, carritoRepository)
        viewModel.flores.observeForever(floresObserver)
        viewModel.error.observeForever(errorObserver)
        viewModel.mensaje.observeForever(mensajeObserver)
    }

    @Test
    fun `cargarFlores exito actualiza livedata flores`() {
        // Given
        val listaFlores = listOf(
            Flor(1, "Rosa", "Roja", 5000, "rosa"),
            Flor(2, "Tulipan", "Amarillo", 3000, "tulipan")
        )

        // Mockeamos el comportamiento del repositorio para devolver la lista
        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[0] as (List<Flor>?, Throwable?) -> Unit
            callback(listaFlores, null)
            null
        }.`when`(productosRepository).obtenerFlores(any())

        // When
        viewModel.cargarFlores()

        // Then
        verify(floresObserver).onChanged(listaFlores)
        verify(mensajeObserver).onChanged("Productos cargados")
    }

    @Test
    fun `cargarFlores error actualiza livedata error`() {
        // Given
        val errorMessage = "Error de red"
        val exception = Exception(errorMessage)

        Mockito.doAnswer { invocation ->
            val callback = invocation.arguments[0] as (List<Flor>?, Throwable?) -> Unit
            callback(null, exception)
            null
        }.`when`(productosRepository).obtenerFlores(any())

        // When
        viewModel.cargarFlores()

        // Then
        verify(errorObserver).onChanged(errorMessage)
    }

    @Test
    fun `agregarAlCarrito llama al repositorio y actualiza mensaje`() {
        // Given
        val flor = Flor(1, "Rosa", "Roja", 5000, "rosa").apply {
            cantidadSeleccionada = 2
        }

        // When
        viewModel.agregarAlCarrito(flor)

        // Then
        verify(carritoRepository).agregarAlCarrito(
            eq("Rosa"),
            eq(5000.0),
            eq("rosa"),
            eq(2)
        )
        verify(mensajeObserver).onChanged("2 x Rosa agregado al carrito")
    }
}
