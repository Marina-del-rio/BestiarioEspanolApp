package com.example.bestiario_espaol

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bestiario_espaol.data.Criatura
import com.example.bestiario_espaol.data.CriaturaDatabase
import com.example.bestiario_espaol.databinding.ActivityListBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding // Instancia de ViewBinding para acceder a las vistas del layout de forma segura.
    private lateinit var adapter: CriaturaAdapter//Adaptador que gestiona como se muestran los datos en el recyclerview
    private val db by lazy { CriaturaDatabase.getDatabase(applicationContext) }// Instancia de la base de datos Room. Se inicializa solo cuando se accede a ella por primera vez (lazy).
    private var lastAddedCriatura: Criatura? = null // Variable para guardar temporalmente la última criatura añadida

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)  // Establece el layout como el contenido de la actividad.
        setContentView(binding.root)

        // Llama a los métodos de configuración
        setupRecyclerView()
        observeCriaturas()
        setupFAB()
    }
        //Métodos de configuracion:
    private fun setupRecyclerView() {
        // Inicializa el adaptador con una lista vacía.
        //listener que se ejecutará cuando se haga clic en un elemento de la lista.
        adapter = CriaturaAdapter(emptyList()) { criatura ->
            Snackbar.make(binding.root, "Has pulsado en ${criatura.nombre}", Snackbar.LENGTH_SHORT).show()
        }
        binding.recyclerViewCriaturas.layoutManager = LinearLayoutManager(this) // Asigna un LayoutManager, que se encarga de posicionar los elementos
        binding.recyclerViewCriaturas.adapter = adapter

        // Configuración para deslizar elementos(se borra de la liasta y de la bbdd al deslizar)
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false // No es necesario implementar esto
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val criaturaParaBorrar = adapter.getCriaturaAt(position)

                lifecycleScope.launch {
                    db.criaturaDao().deleteCriatura(criaturaParaBorrar)
                }
                // Muestra un Snackbar que informa que el elemento fue eliminado + opción de deshacer
                Snackbar.make(binding.root, "${criaturaParaBorrar.nombre} eliminado", Snackbar.LENGTH_LONG)
                    .setAction("DESHACER") {
                        lifecycleScope.launch {
                            db.criaturaDao().insertCriatura(criaturaParaBorrar)
                        }
                    }.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewCriaturas)

    }

    //Observa los cambios en la tabla de criaturas.
    //Cuando hay cambios, actualiza la lista en la interfaz.
    private fun observeCriaturas() {
        lifecycleScope.launch {
            db.criaturaDao().getAllCriaturas().collect { listaDeCriaturas ->
                adapter.updateData(listaDeCriaturas)
            }
        }
    }

    // Configura el botón flotante: al pulsarlo, se muestra
     //un diálogo que permite añadir una nueva criatura.
    private fun setupFAB() {
        binding.fabAddCriaturas.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_criatura, null)
            val etNombre = dialogView.findViewById<EditText>(R.id.etNombre)
            val etTipo = dialogView.findViewById<EditText>(R.id.etTipo)
            val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcion)
            val etComAutonoma = dialogView.findViewById<EditText>(R.id.etComAutonoma)

            AlertDialog.Builder(this)
                .setTitle("Añadir nueva criatura")
                .setView(dialogView)
                .setPositiveButton("Añadir") { _, _ ->
                    val nombre = etNombre.text.toString()
                    val tipo = etTipo.text.toString()
                    val descripcion = etDescripcion.text.toString()
                    val comAutonoma = etComAutonoma.text.toString()

                    // Solo añade la criatura si el nombre no está vacío.
                    if (nombre.isNotBlank()) {
                        val nuevaCriatura = Criatura(nombre = nombre, tipo = tipo, descripcion = descripcion, comAutonoma = comAutonoma)
                        lifecycleScope.launch {
                            val idInsertado = db.criaturaDao().insertCriatura(nuevaCriatura)
                            // Guarda la criatura recién creada para la opción deshacer.
                            lastAddedCriatura = nuevaCriatura.copy(id = idInsertado.toInt())
                            // Muestra un Snackbar que informa que el elemento fue añadido + opción de deshacer
                            Snackbar.make(binding.root, "Criatura añadida", Snackbar.LENGTH_LONG)
                                .setAction("DESHACER") {
                                    lastAddedCriatura?.let { criaturaParaBorrar ->
                                        lifecycleScope.launch {
                                            db.criaturaDao().deleteCriatura(criaturaParaBorrar)
                                        }
                                    }
                                }.show()
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }
    }
}