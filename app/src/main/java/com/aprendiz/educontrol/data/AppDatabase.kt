package com.aprendiz.educontrol.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aprendiz.educontrol.data.dao.MateriaDao
import com.aprendiz.educontrol.data.dao.NotaDao
import com.aprendiz.educontrol.data.dao.UserDao
import com.aprendiz.educontrol.data.entity.MateriaEntity
import com.aprendiz.educontrol.data.entity.NotaEntity
import com.aprendiz.educontrol.data.entity.UserEntity

@Database(
    entities = [UserEntity::class, MateriaEntity::class, NotaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun materiaDao(): MateriaDao
    abstract fun notaDao(): NotaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "educontrol_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
