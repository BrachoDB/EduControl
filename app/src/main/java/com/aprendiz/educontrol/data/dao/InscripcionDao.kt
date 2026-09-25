package com.aprendiz.educontrol.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aprendiz.educontrol.data.entity.InscripcionEntity
import com.aprendiz.educontrol.data.entity.UserEntity

@Dao
interface InscripcionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInscripcion(inscripcion: InscripcionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInscripciones(inscripciones: List<InscripcionEntity>)

    @Query("SELECT u.* FROM users u INNER JOIN inscripciones i ON u.id = i.studentId WHERE i.claseId = :claseId")
    suspend fun getStudentsByClase(claseId: Long): List<UserEntity>

    @Query("SELECT COUNT(*) FROM inscripciones WHERE claseId = :claseId")
    suspend fun getStudentCountByClase(claseId: Long): Int
}
