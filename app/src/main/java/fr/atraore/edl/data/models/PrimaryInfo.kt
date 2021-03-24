package fr.atraore.edl.data.models


abstract class PrimaryInfo {
    var enableInfo: Boolean = false
    abstract fun primaryInfo() : String
}