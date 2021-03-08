package com.ajax.ajaxtestassignment.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.medstar.idis.data.db.convertor.*
import ua.medstar.idis.data.db.model.*

@Database(
    entities = [
        Examination::class,
        FreeNumber::class,
        Kit::class,
        SupportedDevice::class,
        Analyse::class,
        DbMedia::class,
        ResultModel::class],
    version = 1
)
@TypeConverters(
    DateConverter::class,
    AnalyseTypeConverter::class,
    StatusTypeConverter::class,
    GenderTypeConverter::class,
    UuidConverter::class
)
abstract class IdisDatabase : RoomDatabase() {
    abstract fun resultsDao(): ResultsDao
    abstract fun examinationDao(): ExaminationDao
    abstract fun analyseDao(): AnalyseDao
    abstract fun mediaDao(): MediaDao
    abstract fun kitDao(): KitDao
    abstract fun freeNumbersDao(): ContactsDao
}