package com.example.bestiario_espaol.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CriaturaDAO {

    @Query("SELECT * FROM criaturas")
    fun getAllCriaturas(): Flow<List<Criatura>>//Flow permite obtener los cambios inmediatamente


    @Insert
    suspend fun insertCriatura(criatura: Criatura): Long

    @Delete
    suspend fun deleteCriatura(criatura: Criatura)

    //suspend se utiliza para que no haya problemas con los hilos y se bloquee la app
}