package com.example.prak3_sqlitedbwithimage

class Model() {
    var id: Int = 0
        get() = field
        set(value) { field = value }

    var proavatar: ByteArray? = null
        get() = field
        set(value) { field = value }

    var name: String = ""
        get() = field
        set(value) { field = value }
}