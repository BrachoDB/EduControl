package com.aprendiz.educontrol.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aprendiz.educontrol.data.dao.*
import com.aprendiz.educontrol.data.entity.*

@Database(
    entities = [
        UserEntity::class,
        MateriaEntity::class,
        NotaEntity::class,
        ClaseEntity::class,
        InscripcionEntity::class,
        ActividadEntity::class,
        EntregaEntity::class,
        CalificacionEntity::class,
        PreguntaQuizEntity::class,
        RespuestaQuizEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun materiaDao(): MateriaDao
    abstract fun notaDao(): NotaDao
    abstract fun claseDao(): ClaseDao
    abstract fun inscripcionDao(): InscripcionDao
    abstract fun actividadDao(): ActividadDao
    abstract fun entregaDao(): EntregaDao
    abstract fun calificacionDao(): CalificacionDao
    abstract fun preguntaQuizDao(): PreguntaQuizDao
    abstract fun respuestaQuizDao(): RespuestaQuizDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "educontrol_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
