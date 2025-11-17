package com.example.bestiario_espaol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enlazar con el layout correspondiente
        setContentView(R.layout.activity_firstscreen)

        // Buscar el boton a través de su id
        val btnStart = findViewById<Button>(R.id.btnStart)

        // Definir que hace el botón al ser clicado por el usuario
        btnStart.setOnClickListener {
            // Crea el intent para abrir la segunda pantalla
            val intent = Intent(this, ListActivity::class.java)

            // Ejecutar el intent
            startActivity(intent)
        }
    }
}