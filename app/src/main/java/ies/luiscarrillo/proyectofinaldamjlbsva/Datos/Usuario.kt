package ies.luiscarrillodesotomayor.gestionincidencias.Datos

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

open class Usuario {

    // Miembros de la clase Usuario
    var nombre: String = ""
    var apellidos: String = ""
    var email: String = ""
    var password: String = ""
    var privi: String = ""
    // Enumeracion de privilegios
    enum class privilegios {admin,gestor,user}

    // Enumeracion de Roles y permisos (PARTE DE SERGIO VILLAR)

    // Constructor de la clase Usuario
    constructor(
        nombre: String,
        apellidos: String,
        email: String,
        password: String,
        privilegios: String
    )
    {
        this.nombre = nombre
        this.apellidos = apellidos
        this.email = email
        this.password = password
    }

    // Constructor de la clase Usuario 2
    constructor(email: String, password: String){
        this.email = email
        this.password = password
    }


    // Metodos de la clase Usuario

    // Obtener los datos del usuario
    override fun toString(): String {
        return "Usuario(nombre='$nombre', apellidos='$apellidos', email='$email', password='$password')"
    }


    // Validar la contraseña del usuario para que sea segura
    fun contraseñaSegura(usuario: Usuario): Boolean {
        val esvalida = false

        if (usuario.password.length >= 8) {
            if (usuario.password.contains("[a-z]".toRegex())) {
                if (usuario.password.contains("[A-Z]".toRegex())) {
                    if (usuario.password.contains("[0-9]".toRegex())) {
                        if (usuario.password.contains("[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]".toRegex())) {
                            return true
                        }
                    }
                }
            }
        }
        return esvalida
    }

    // Validaciones de los datos
    fun validarEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    // Registro de usuario en la base de datos de Firebase
    fun registrarUsuario(usuario: Usuario) : Boolean {

        var registrado = false
        val auth = FirebaseAuth.getInstance()
        val id = Integer.toString(usuario.hashCode())
        auth.createUserWithEmailAndPassword(usuario.email, usuario.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Encriptar contraseña
                    usuario.password = encriptarContraseña(usuario.password)
                    // ACABAR DE REGISTRAR EL USUARIO EN LA BASE DE DATOS DE FIREBASE REALTIME
                    val db = Firebase.firestore  // Instancia de la base de datos
                    // Creamos un mapa con los datos del usuario
                    val datos = hashMapOf(
                        "nombre" to usuario.nombre,
                        "apellidos" to usuario.apellidos,
                        "email" to usuario.email,
                        "password" to usuario.password,
                        "ID" to id
                    )
                    // Añadimos los datos al documento
                    db.collection("Usuarios").document(id).set(datos)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Usuario", "DocumentSnapshot added with ID: ${documentReference}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Usuario", "Error adding document", e)
                        }
                    registrado = true
                } else
                {
                    registrado = false
                }
            }
        return registrado
    }

    // Recordar contraseña
    open fun recordarContraseña(email: String) : Boolean {
        var enviado = false
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener { task ->
                enviado = true
            }
            .addOnFailureListener() {
                enviado = false
            }

        return enviado
    }

    // consultar si el usuario existe en la base de datos de firebase
    fun existeUsuario(): Boolean {
        var existe = false
        val database = Firebase.database
        val myRef = database.getReference("Usuarios")
        myRef.get().addOnSuccessListener {
            if (it.hasChild(email)) {
                existe = true
            }
        }
        return existe
    }

    // Encriptar la contraseña del usuario
    fun encriptarContraseña(password: String): String {
        // Instanciamos un objeto de tipo MessageDigest para poder utilizar el algoritmo de hash SHA-256
        val messageDigest = MessageDigest.getInstance("SHA-256")

        // Convertimos la contraseña a un arreglo de bytes y generamos su hash utilizando el algoritmo SHA-256
        val hash = messageDigest.digest(password.toByteArray(StandardCharsets.UTF_8))

        // Creamos un objeto de tipo StringBuilder para almacenar la cadena hexadecimal resultante
        val hexString = StringBuilder()

        // Iteramos sobre cada byte del hash generado
        for (b in hash) {
            // Convertimos cada byte a una cadena hexadecimal
            val hex = Integer.toHexString(0xff and b.toInt())

            // Si la longitud de la cadena hexadecimal es 1, agregamos un 0 al principio para completar la representación a dos dígitos
            if (hex.length == 1) {
                hexString.append('0')
            }

            // Agregamos la cadena hexadecimal a la cadena final
            hexString.append(hex)
        }
        // Devolvemos la cadena hexadecimal resultante
        return hexString.toString()
    }





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

