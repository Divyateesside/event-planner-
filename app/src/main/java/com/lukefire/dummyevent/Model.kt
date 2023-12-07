package com.lukefire.dummyevent

class Model  //constructer
{
    //getter and setter
    var name: String? = null

    //////////////
    var description: String? = null
    var event_url: String? = null

    //////////////
    var logo_url: String? = null

    //////
    var registration_Link: String? = null
        set(Registration_Link) {
            logo_url = Registration_Link
        }
}