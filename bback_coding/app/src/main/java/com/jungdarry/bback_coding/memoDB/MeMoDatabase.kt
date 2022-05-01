package com.jungdarry.bback_coding.memoDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Memo::class], version = 1)
abstract class MeMoDatabase: RoomDatabase() {

    abstract fun memoDao(): MemoDao

    companion object {
        private var INSTANCE: MeMoDatabase? = null

        fun getInstance(context: Context): MeMoDatabase? {
            if (INSTANCE == null) {
                synchronized(MeMoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        MeMoDatabase::class.java, "memo")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }

}