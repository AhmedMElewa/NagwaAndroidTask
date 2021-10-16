package com.elewa.nagwaandroidtask.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Item")
data class ItemModel(
    @PrimaryKey
    val id: Int,
    val type: String,
    val url: String,
    val name: String,
    var status: Int = 0
)
