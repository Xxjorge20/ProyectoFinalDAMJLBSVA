package ParteUsuarios.Data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

open class Usuarios {

    // Enumeracion de privilegios
    enum class privilegios {admin,gestor,user}


    var email : String = ""
    var nombre : String = ""
    var apellidos : String = ""
    var privi : String = ""
    var password : String = ""


    constructor(
        email: String,
        nombre: String,
        apellidos: String,
        privilegios: String,
        password: String

    )

    // Borra un usuario de la BD
    fun BorrarUsuario(usuario: String) :Boolean
    {

        var borradoCorrectamente = false
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        auth.currentUser?.let {
            db.collection("usuarios").whereEqualTo("email", usuario).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("usuarios").document(document.id).delete()
                            .addOnSuccessListener {
                                // Borrar usuarios de la Authenticacion
                                auth.currentUser?.delete()
                                borradoCorrectamente = true
                                Log.d("Usuario", "Usuario borrada correctamente")

                            }
                            .addOnFailureListener {
                                borradoCorrectamente = false
                                Log.d("Usuario", "Error al borrar el usuario")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Usuario", "Error al obtener el usuario", exception)
                }
        }
        return borradoCorrectamente
    }


}