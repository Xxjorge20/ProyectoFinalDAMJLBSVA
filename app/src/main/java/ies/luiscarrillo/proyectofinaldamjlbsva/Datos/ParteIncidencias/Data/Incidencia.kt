package ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

open class Incidencia {

    // Enumeracion de tipo de prioridad
    enum class Prioridad  {
        BAJA, MEDIA, ALTA
    }

    enum class tipoIncidencia {
        Informaticas, Mantenimiento, Electrico,Otros
    }



    // Miembros de la clase Incidencia
     var nombre: String = ""
     var descripcion: String = ""
     var fecha: String = ""
     var acabada: Boolean = false
     var foto: String = ""
     var prioridad: String = ""
     var tipo: String = ""
     private var ID: String = ""


    // Constructor de la clase Incidencia
    constructor(
        nombre: String,
        fecha: String,
        descripcion: String,
        acabada: Boolean,
        prioridad: String,
        tipo :String,
        foto: String
    ) {
        this.nombre = nombre
        this.fecha = fecha
        this.descripcion = descripcion
        this.acabada = acabada
        this.prioridad = prioridad.toString()
        this.tipo = tipo
        this.foto = foto
    }

    // Constructor de la clase Incidencia para Firebase
     constructor(nombre: String, descripcion: String, fecha: String, acabada: Boolean, foto: String,prioridad: String,tipo: String, id: String) {
            this.nombre = nombre
            this.descripcion = descripcion
            this.fecha = fecha
            this.acabada = acabada
            this.foto = foto
            this.prioridad = prioridad
            this.tipo = tipo
            this.ID = id

     }

    // Metodos de la clase Incidencia

    // Metodo para los datos de la incidencia
    override fun  toString(): String {
        return "Incidencia(nombre='$nombre', descripcion='$descripcion', fecha='$fecha', acabada=$acabada, foto='$foto', prioridad='$prioridad')"
    }

    // Insertar Incidencia en la BD
     fun InsertarIncidencia(incidencia: Incidencia) :Boolean{

        // Declaracion de variables
        var insertadoCorrectamente = false
        var auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
         val idDocumento = (auth.currentUser?.uid?.hashCode()!! + System.currentTimeMillis().toInt()).toString()
            incidencia.ID = incidencia.hashCode().toString()

        // Insertar incidencia en la BD
        auth.currentUser?.let {
            // Convierto los datos de la incidencia en un HashMap
            val datos = hashMapOf(
                "nombre" to incidencia.nombre,
                "descripcion" to incidencia.descripcion,
                "prioridad" to incidencia.prioridad,
                "tipo" to incidencia.tipo,
                "fecha" to incidencia.fecha,
                "acabada" to incidencia.acabada,
                "ID" to incidencia.ID,
                "foto" to incidencia.foto
            )

            // Inserto los datos en la BD de Firebase
            db.collection("Incidencias").document(idDocumento).set(datos)
                .addOnSuccessListener {
                    insertadoCorrectamente = true
                    Log.d("Incidencia", "Incidencia insertada correctamente")
                }
                .addOnFailureListener {
                    insertadoCorrectamente = false
                    Log.d("Incidencia", "Error al insertar la incidencia")
                }


        }
        return insertadoCorrectamente
    }

    // Obtener la ID Incidencia
    /*
    fun getIDIncidencia(incidencia : Incidencia): String {
        var auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        var idIncidencia = ""

        auth.currentUser?.let {
            db.collection("Incidencias").document(incidencia.ID).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        idIncidencia = document.getString("ID").toString()
                        Log.d("Incidencia", "ID de la incidencia obtenida correctamente")
                    } else {
                        Log.d("Incidencia", "No existe la incidencia")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Incidencia", "Error al obtener la ID de la incidencia", exception)
                }

        }
        return idIncidencia
    }
    */
    // Borra una incidencia de la BD
    fun BorrarIncidencia(incidencia: String): Boolean {
        var borradoCorrectamente = false
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        auth.currentUser?.let {
            db.collection("Incidencias").whereEqualTo("ID", incidencia).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Eliminar la incidencia en la base de datos
                        db.collection("Incidencias").document(document.id).delete()
                            .addOnSuccessListener {
                                borradoCorrectamente = true
                                Log.d("BorrarIncidencia", "Incidencia borrada correctamente")

                                // Obtener el nombre de la imagen de la incidencia
                                val imageName = document.getString("nombre") + ".jpg"

                                // Obtener la referencia a la imagen en el Storage
                                val storage = FirebaseStorage.getInstance()
                                val storageRef = storage.reference
                                val imageRef = storageRef.child("FotosIncidencia/$imageName")

                                // Eliminar la imagen en el Storage
                                imageRef.delete().addOnSuccessListener {
                                    Log.d("BorrarIncidencia", "Imagen eliminada correctamente")
                                }.addOnFailureListener { exception ->
                                    Log.w("BorrarIncidencia", "Error al eliminar la imagen", exception)
                                }
                            }
                            .addOnFailureListener {
                                borradoCorrectamente = false
                                Log.d("BorrarIncidencia", "Error al borrar la incidencia")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("BorrarIncidencia", "Error al obtener la incidencia", exception)
                }
        }
        return borradoCorrectamente
    }


    // Actualiza una incidencia de la BD
    /*
    fun actualizarIncidencia(id: String, incidencia: Incidencia) :Boolean{
        var actualizadoCorrectamente = false
        var auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        // Obtenemos la incidencia a actualizar
        auth.currentUser?.let {
            val datos = hashMapOf(
                "nombre" to incidencia.nombre,
                "descripcion" to incidencia.descripcion,
                "prioridad" to incidencia.prioridad,
                "fecha" to incidencia.fecha,
                "acabada" to incidencia.acabada,
                "foto" to incidencia.foto
            )
            db.collection("Incidencias").document(id).set(datos)
                .addOnSuccessListener {
                    actualizadoCorrectamente = true
                    Log.d("Incidencia", "Incidencia actualizada correctamente")
                }
                .addOnFailureListener {
                    actualizadoCorrectamente = false
                    Log.d("Incidencia", "Error al actualizar la incidencia")
                }
        }


        return actualizadoCorrectamente
    }
    */
    companion object {
        fun actualizarIncidencia(incidencia: Incidencia) {

            // Actualiza una incidencia de la BD
            var actualizadoCorrectamente = false
            var auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()

            // Obtenemos la incidencia a actualizar
            auth.currentUser?.let {
                val datos = hashMapOf(
                    "nombre" to incidencia.nombre,
                    "descripcion" to incidencia.descripcion,
                    "prioridad" to incidencia.prioridad,
                    "fecha" to incidencia.fecha,
                    "acabada" to incidencia.acabada,
                    "foto" to incidencia.foto
                )
                db.collection("Incidencias").document(incidencia.ID).set(datos)
                    .addOnSuccessListener {
                        actualizadoCorrectamente = true
                        Log.d("Incidencia", "Incidencia actualizada correctamente")
                        Log.d("Incidencia", incidencia.ID)
                        Log.d("Incidencia", incidencia.nombre)
                        Log.d("Incidencia", incidencia.descripcion)
                        Log.d("Incidencia", incidencia.prioridad)
                        Log.d("Incidencia", incidencia.fecha)
                        Log.d("Incidencia", incidencia.acabada.toString())
                        Log.d("Incidencia", incidencia.foto)
                    }
                    .addOnFailureListener {
                        actualizadoCorrectamente = false
                        Log.d("Incidencia", "Error al actualizar la incidencia")
                    }
            }

        }
    }


}