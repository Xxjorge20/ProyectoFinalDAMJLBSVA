package com.example.proyectoclara.Data

import android.provider.ContactsContract.CommonDataKinds.Email

data class ItemsUsuarios(
    // Data class para poder sacar los datos de Firebase
    var email: String = "",
    var Nombre : String = "",
    var Apellidos : String = "",
    var Privilegios : String = ""

)
