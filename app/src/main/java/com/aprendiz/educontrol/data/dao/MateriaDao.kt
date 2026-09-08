package com.aprendiz.educontrol.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aprendiz.educontrol.data.entity.MateriaEntity

@Dao
interface MateriaDao {
    @Query("SELECT * FROM materias WHERE userId = :userId")
    suspend fun getMateriasByUser(userId: Long): List<MateriaEntity>

    @Query("SELECT * FROM materias WHERE id = :id LIMIT 1")
    suspend fun getMateriaById(id: Long): MateriaEntity?

    @Insert
    suspend fun insertMateria(materia: MateriaEntity): Long

    @Update
    suspend fun updateMateria(materia: MateriaEntity)

    @Delete
    suspend fun deleteMateria(materia: MateriaEntity)
}
