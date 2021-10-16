package com.elewa.nagwaandroidtask.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.elewa.nagwaandroidtask.data.model.ItemModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(itemList: List<ItemModel>)

    @Query("SELECT * FROM item where type=:type")
    fun get(type:String): Flow<List<ItemModel>>

    @Query("update item set status=:status where id=:id")
    suspend fun update(id : Int, status: Int)

    @Delete
    suspend fun delete(item: ItemModel)

    @Query("DELETE FROM item")
    suspend fun clear()
}