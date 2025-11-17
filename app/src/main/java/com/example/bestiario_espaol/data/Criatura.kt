package com.example.bestiario_espaol.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "criaturas")
data class Criatura(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val comAutonoma: String
)