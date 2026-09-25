package com.aprendiz.educontrol.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aprendiz.educontrol.data.entity.ClaseEntity

@Dao
interface ClaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClase(clase: ClaseEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClases(clases: List<ClaseEntity>)

    @Query("SELECT * FROM clases WHERE id = :id LIMIT 1")
    suspend fun getClaseById(id: Long): ClaseEntity?

    @Query("SELECT * FROM clases WHERE teacherId = :teacherId")
    suspend fun getClasesByTeacher(teacherId: Long): List<ClaseEntity>

    @Query("SELECT c.* FROM clases c INNER JOIN inscripciones i ON c.id = i.claseId WHERE i.studentId = :studentId")
    suspend fun getClasesByStudent(studentId: Long): List<ClaseEntity>
}
