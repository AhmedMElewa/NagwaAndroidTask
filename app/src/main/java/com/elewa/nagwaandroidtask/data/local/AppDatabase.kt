package com.elewa.nagwaandroidtask.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elewa.nagwaandroidtask.data.model.ItemModel
import com.elewa.nagwaandroidtask.util.Constants.DATABASE_NAME

@Database(entities = [ItemModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val itemDao: ItemDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}