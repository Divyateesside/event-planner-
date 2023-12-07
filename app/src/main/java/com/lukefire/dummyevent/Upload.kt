package com.lukefire.dummyevent

class Upload {
    var name: String? = null
        private set
    var imageUrl: String? = null

    constructor()
    constructor(name: String, event_url: String?) {
        var name = name
        if (name.trim { it <= ' ' } == "") {
            name = "No Name"
        }
        this.name = name
        imageUrl = event_url
    }

    fun setNmae(name: String?) {
        this.name = name
    }
}