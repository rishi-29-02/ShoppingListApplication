package com.androidnerds.shoppinglist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.androidnerds.shoppinglist.data.db.entities.ShoppingItem

@Database(
    entities = [ShoppingItem::class],
    version = 1
)
abstract class ShoppingDataBase: RoomDatabase() {

    abstract fun getShoppingDao() : ShoppingDao

    companion object {

        @Volatile
        private var instance : ShoppingDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDataBase((context)).also { instance = it }
        }

        private fun createDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ShoppingDataBase::class.java,
                "ShoppingDB.db"
            ).build()
    }
}