package com.jungdarry.bback_coding.memoDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    var id: Int?,

    @ColumnInfo(name = "title") //칼럼명을 변수명과 같이 쓰려면 생략
    var title: String,

    @ColumnInfo(name = "content")
    var content: String,

    @ColumnInfo(name = "ispw")
    var ispw: Boolean,

    @ColumnInfo(name = "pw")
    var pw: Int,

    @ColumnInfo(name = "date")
    var date: String,

//    @ColumnInfo(name = "initial")
//    var initial: Char
) {
    constructor() : this(null, "", "",false,-1,"")
}