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

        obtenerUser { usuarioActual ->
            if (usuarioActual != null) {
                auth.currentUser?.let {
                    db.collection("usuarios").whereEqualTo("email", usuario).get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                // Obtener el usuario de la BD
                                val email = document.get("email").toString()
                                val password = document.get("Password").toString()
                                // Cierre de sesion con el usuario que ha iniciado sesion
                                auth.signOut()
                                // Inicio de sesion con el usuario que se quiere borrar
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            db.collection("usuarios").document(document.id).delete()
                                                .addOnSuccessListener {
                                                    // Borrar usuarios de la Authenticacion
                                                    auth.currentUser!!.delete()
                                                    borradoCorrectamente = true
                                                    // Cierre de sesion con el usuario que se ha borrado
                                                    auth.signOut()
                                                    // Inicio de sesion con el usuario que ha iniciado sesion
                                                    auth.signInWithEmailAndPassword(
                                                        usuarioActual.email,
                                                        usuarioActual.password
                                                    )
                                                        .addOnCompleteListener { task ->
                                                            if (task.isSuccessful) {
                                                                Log.d(
                                                                    "Usuario",
                                                                    "Usuario logeado correctamente"
                                                                )
                                                            } else {
                                                                Log.d(
                                                                    "Usuario",
                                                                    "Error al logear el usuario"
                                                                )
                                                            }
                                                        }

                                                    Log.d(
                                                        "Usuario",
                                                        "Usuario borrada correctamente"
                                                    )

                                                }
                                                .addOnFailureListener {
                                                    borradoCorrectamente = false
                                                    Log.d("Usuario", "Error al borrar el usuario")
                                                }
                                        } else {
                                            Log.d("Usuario", "Error al logear el usuario")
                                        }
                                    }
                                    }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("Usuario", "Error al obtener el usuario", exception)
                        }
                }

            } else {
                Log.d("Usuario", "Error al obtener el usuario")
            }
        }





        return borradoCorrectamente
    }



    fun obtenerUser(callback: (Usuarios?) -> Unit) {
        var usuarioLogin = FirebaseAuth.getInstance().currentUser
        var correo = usuarioLogin?.email.toString()
        var  auth = FirebaseAuth.getInstance()
        var db = FirebaseFirestore.getInstance()
        var usuario = Usuarios("","","","","")



        auth.currentUser.let {
            db.collection("usuarios").document(correo).get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    usuario.nombre = documentSnapshot.get("Nombre").toString()
                    usuario.apellidos = documentSnapshot.get("Apellidos").toString()
                    usuario.email = documentSnapshot.get("email").toString()
                    usuario.password = documentSnapshot.get("Password").toString()
                    usuario.privi = documentSnapshot.get("Privilegios").toString()

                    callback(usuario)
                } else {
                    callback(null)
                }
            }.addOnFailureListener {
                callback(null)
            }
        }


    }


}