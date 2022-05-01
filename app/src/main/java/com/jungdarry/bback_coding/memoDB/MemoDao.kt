package com.jungdarry.bback_coding.memoDB

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoDao {

    @Query("SELECT * FROM memo ORDER BY date DESC") // ACS DESC
    fun getAll(): LiveData<List<Memo>>

    @Query("SELECT * FROM memo WHERE title LIKE '%' || :strfind || '%'") // title에 대한 오름차순으로가져오기
    fun getFilterd(strfind : String?) :LiveData<List<Memo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Memo)

    @Delete
    fun delete(contact: Memo)

}