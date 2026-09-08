package com.aprendiz.educontrol.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aprendiz.educontrol.data.entity.NotaEntity

@Dao
interface NotaDao {
    @Query("SELECT * FROM notas WHERE materiaId = :materiaId")
    suspend fun getNotasByMateria(materiaId: Long): List<NotaEntity>

    @Insert
    suspend fun insertNota(nota: NotaEntity): Long

    @Update
    suspend fun updateNota(nota: NotaEntity)

    @Delete
    suspend fun deleteNota(nota: NotaEntity)

    @Query("SELECT SUM(porcentaje) FROM notas WHERE materiaId = :materiaId")
    suspend fun getSumPercentageByMateria(materiaId: Long): Double?
}
