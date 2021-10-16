package com.elewa.nagwaandroidtask.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.elewa.nagwaandroidtask.data.model.ItemModel
import com.elewa.nagwaandroidtask.getOrAwaitValue
import com.elewa.nagwaandroidtask.util.Constants.DATABASE_NAME
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ItemDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var dao: ItemDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.itemDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertItemList() = runBlockingTest {

        var i = 0
        var itemList = ArrayList<ItemModel>()
        repeat(5) {
            i++
            itemList.add(ItemModel(i,"VIDEO","url","Item"+i,0))
        }
        dao.insert(itemList)

        val allItems = dao.get("VIDEO").first()

        assertThat(itemList.size).isEqualTo(allItems.size)
    }


    @Test
    fun deleteItem() = runBlockingTest {
        val item = ItemModel(1,"VIDEO","url","Item1",0)
        dao.insertItem(item)
        dao.delete(item)

        val allItems = dao.get("VIDEO").first()

        assertThat(allItems).doesNotContain(item)
    }

    @Test
    fun updateItem() = runBlockingTest {
        val item = ItemModel(1,"VIDEO","url","Item1",0)
        dao.insertItem(item)
        dao.update(1,100)

        val allItems = dao.get("VIDEO").first()

        assertThat(allItems[0].status).isEqualTo(100)
    }

    @Test
    fun clear() = runBlockingTest {
        val item = ItemModel(1,"VIDEO","url","Item1",0)
        dao.insertItem(item)
        val item2 = ItemModel(2,"VIDEO","url","Item1",0)
        dao.insertItem(item2)
        dao.clear()

        val allItems = dao.get("VIDEO").first()

        assertThat(allItems.size).isEqualTo(0)
    }
}