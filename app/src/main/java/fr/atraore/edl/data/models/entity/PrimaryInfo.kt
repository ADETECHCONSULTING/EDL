package fr.atraore.edl.data.models.entity


abstract class PrimaryInfo {
    var enableInfo: Boolean = false
    abstract fun primaryInfo() : String
    abstract fun civiInfo() : String
}