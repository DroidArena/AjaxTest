package ua.medstar.idis.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.medstar.idis.data.db.model.FreeNumber

@Dao
interface FreeNumbersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(number: FreeNumber): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleRecords(numbers: List<FreeNumber>)

    @Query("SELECT * FROM free_numbers")
    suspend fun fetchAllData(): List<FreeNumber>

    @Query("SELECT COUNT(id) FROM free_numbers")
    suspend fun getCount(): Int

    @Query("SELECT * FROM free_numbers LIMIT 1")
    suspend fun getValue(): FreeNumber?

    @Delete
    suspend fun deleteRecord(number: FreeNumber)

    @Query("DELETE FROM free_numbers")
    fun dropTable()
}