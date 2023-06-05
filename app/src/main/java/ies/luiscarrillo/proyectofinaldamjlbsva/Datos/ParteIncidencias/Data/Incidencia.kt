package ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

open class Incidencia {

    // Enumeracion de tipo de prioridad
    enum class Prioridad {
        BAJA, MEDIA, ALTA
    }

    /* La clase define un tipo de enumeración llamado "tipoIncidencia" con cuatro valores posibles. */
    enum class tipoIncidencia {
        Informaticas, Mantenimiento, Electrico, Otros
    }


    // Miembros de la clase Incidencia
    var nombre: String = ""
    var descripcion: String = ""
    var fecha: String = ""
    var acabada: Boolean = false
    var foto: String = ""
    var prioridad: String = ""
    var tipo: String = ""
    var lugar: String = ""
    private var ID: String = ""


    // Constructor de la clase Incidencia
    constructor(
        nombre: String,
        fecha: String,
        descripcion: String,
        acabada: Boolean,
        prioridad: String,
        tipo: String,
        lugar: String,
        foto: String
    ) {
        this.nombre = nombre
        this.fecha = fecha
        this.descripcion = descripcion
        this.acabada = acabada
        this.prioridad = prioridad.toString()
        this.tipo = tipo
        this.lugar = lugar
        this.foto = foto
    }

    // Constructor de la clase Incidencia para Firebase
    constructor(
        nombre: String,
        descripcion: String,
        fecha: String,
        acabada: Boolean,
        foto: String,
        prioridad: String,
        tipo: String,
        lugar: String,
        id: String
    ) {
        this.nombre = nombre
        this.descripcion = descripcion
        this.fecha = fecha
        this.acabada = acabada
        this.foto = foto
        this.prioridad = prioridad
        this.tipo = tipo
        this.lugar = lugar
        this.ID = id

    }

    // Metodos de la clase Incidencia

    // Metodo para los datos de la incidencia
    override fun toString(): String {
        return "Incidencia(nombre='$nombre', descripcion='$descripcion', fecha='$fecha', acabada=$acabada, foto='$foto', prioridad='$prioridad')"
    }

    /**
     * Esta función inserta una incidencia en una base de datos de Firebase.
     * 
     * @param incidencia un objeto de tipo Incidencia, que contiene información sobre un incidente como
     * su nombre, descripción, prioridad, tipo, fecha, si se ha completado o no, DNI y foto.
     * @return un valor booleano que indica si la incidencia se insertó correctamente en la base de
     * datos o no.
     */

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
                "fecha" to incidencia.fecha,
                "acabada" to incidencia.acabada,
                "foto" to incidencia.foto,
                "prioridad" to incidencia.prioridad,
                "tipo" to incidencia.tipo,
                "lugar" to incidencia.lugar,
                "id" to incidencia.ID,
            )

            // Inserto los datos en la BD de Firebase
            db.collection("Incidencias").document(incidencia.ID).set(datos)
                .addOnSuccessListener {
                    insertadoCorrectamente = true
                    Log.d("Incidencia", "Incidencia insertada correctamente")
                    Log.d("IncidenciaID", "ID: ${incidencia.ID}")
                }
                .addOnFailureListener {
                    insertadoCorrectamente = false
                    Log.d("Incidencia", "Error al insertar la incidencia")
                }


        }
        return insertadoCorrectamente
    }



    /**
     * La función elimina una incidencia y su imagen correspondiente de Firebase Firestore y Storage,
     * respectivamente.
     * 
     * @param incidencia El parámetro "incidencia" es una cadena que representa el ID del incidente a
     * eliminar de la base de datos y su imagen correspondiente del Firebase Storage.
     * @return Un valor booleano que indica si la incidencia se eliminó correctamente o no.
     */
    fun BorrarIncidencia(incidencia: String): Boolean {
        var borradoCorrectamente = false
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        auth.currentUser?.let {
            db.collection("Incidencias").whereEqualTo("id", incidencia).get()
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


    companion object {
        /* La función `actualizarIncidencia` es una función de objeto complementaria de la clase `Incidencia`
        en Kotlin. Toma como parámetro un objeto de tipo `Incidencia` y actualiza el documento
        correspondiente en la base de datos de Firebase Firestore con los nuevos datos. */
        fun actualizarIncidencia(incidencia: Incidencia) {

            // Actualiza una incidencia de la BD
            var auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()

            // Obtenemos la incidencia a actualizar
            auth.currentUser?.let {
                val datos = hashMapOf(
                    "nombre" to incidencia.nombre,
                    "descripcion" to incidencia.descripcion,
                    "fecha" to incidencia.fecha,
                    "acabada" to incidencia.acabada,
                    "foto" to incidencia.foto,
                    "prioridad" to incidencia.prioridad,
                    "tipo" to incidencia.tipo,
                    "lugar" to incidencia.lugar,
                    "id" to incidencia.ID,

                )
                db.collection("Incidencias").document(incidencia.ID).set(datos)
                    .addOnSuccessListener {

                        Log.d("Incidencia", "Incidencia actualizada correctamente")
                        Log.d("Incidencia ID", incidencia.ID)
                        Log.d("Incidencia NOMBRE", incidencia.nombre)
                        Log.d("Incidencia DESCRIPCION", incidencia.descripcion)
                        Log.d("Incidencia PRIORIDAD", incidencia.prioridad)
                        Log.d("Incidencia FECHA", incidencia.fecha)
                        Log.d("Incidencia ACABADA", incidencia.acabada.toString())
                        Log.d("Incidencia TIPO", incidencia.tipo)
                        Log.d("Incidencia FOTO", incidencia.foto)
                        Log.d("Incidencia LUGAR", incidencia.lugar)
                    }
                    .addOnFailureListener {

                        Log.d("Incidencia", "Error al actualizar la incidencia")
                    }
            }

        }
    }


}