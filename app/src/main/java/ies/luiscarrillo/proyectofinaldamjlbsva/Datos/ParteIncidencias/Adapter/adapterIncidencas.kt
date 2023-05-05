package ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Adapter

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.ViewHolder.ViewHolder
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data.DatosIncidencias
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data.Incidencia
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu.MenuLateral
import ies.luiscarrillodesotomayor.gestionincidencias.Menu.ModificarIncidencia


class adapterIncidencas(private var incidencias: ArrayList<DatosIncidencias>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_incidencia, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Datos de las incidencias
        var incidencia : DatosIncidencias = incidencias[position]
        val auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {

            val storage = FirebaseStorage.getInstance()
            // Referencia a la imagen de la incidencia en el Storage
            val storageReference = storage.getReference("FotosIncidencia/")
            // Descarga de la imagen de la incidencia en el Storage
            val imageRef = storageReference.child(incidencias[position].nombre + ".jpg")
            imageRef.getBytes(1024 * 1024).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                holder.imagen.setImageBitmap(bitmap)
                Log.i("Imagen", "Imagen cargada")
            }.addOnFailureListener {
                // Handle any errors
                Log.i("Imagen", "Error al cargar la imagen")
            }
        }


        holder.titulo.text = incidencia.nombre
        holder.descripcion.text = incidencia.descripcion
        holder.fecha.text = incidencia.fecha
        // Establecimiento del color de los diferentes textos
        holder.titulo.setTextColor(android.graphics.Color.WHITE)
        holder.descripcion.setTextColor(android.graphics.Color.WHITE)
        holder.fecha.setTextColor(android.graphics.Color.WHITE)
        holder.bind(incidencias[position])

        val db = FirebaseFirestore.getInstance()
        val correo = auth.currentUser?.email.toString()

        holder.itemView.setOnLongClickListener(){
            db.collection("usuarios").document(correo).get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val privilegios = documentSnapshot.getString("privilegios")

                    when (privilegios) {
                        "user" -> {
                            // No tiene permisos, mostrar toast

                            Toast.makeText(holder.itemView.context, "No tienes permisos para modificar y borrar incidencias", Toast.LENGTH_SHORT).show()
                        }
                        "gestor", "admin" -> {
                            // Tiene permisos, mostrar opciones
                            if (!DialogoModificarBorrar(incidencia, holder)) {
                                Log.i("MostarMenu", "Modificando incidencia")
                            } else {
                                Log.i("MostarMenu", "Borrando incidencia")
                            }
                        }
                    }
                }
            }
            true
        }


    }

    override fun getItemCount(): Int {
       return incidencias.size
    }

    // Funcion para modificar una incidencia cuando la pulsamos en el menu
    fun CargarModificarFragment(datosIncidencias: DatosIncidencias, holder: ViewHolder) {

        // Llevar datos a la actividad de modificar incidencia para que los muestre
        val fragment = ModificarIncidencia()
        val args  = Bundle()
        args.putString("ID", datosIncidencias.ID)
        args.putString("nombre", datosIncidencias.nombre)
        args.putString("fecha", datosIncidencias.fecha)
        args.putString("descripcion", datosIncidencias.descripcion)
        args.putString("prioridad", datosIncidencias.prioridad)
        args.putBoolean("acabada", datosIncidencias.acabada)
        args.putString("foto", datosIncidencias.foto)

        /*
                datosIncidencias.nombre,
                datosIncidencias.fecha,
                datosIncidencias.descripcion,
                datosIncidencias.acabada,
                datosIncidencias.foto,
                datosIncidencias.prioridad,
                datosIncidencias.ID
        * */
        fragment.arguments = args
        // Asociar con el menu de la aplicacion de la parte de incidencias
        val fragmentManager: FragmentManager = (holder.itemView.context as MenuLateral).supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragment_container_view, fragment).commit()

    }

    // Funcion para mostrar un dialogo de opciones de la incidencia
    fun DialogoModificarBorrar(datosIncidencias: DatosIncidencias, holder: ViewHolder): Boolean {
        var respuesta = false
        val builder = AlertDialog.Builder(holder.itemView.context)
        builder.setTitle("¿Que Desea Hacer?")
        builder.setMessage(datosIncidencias.ID + " - " + datosIncidencias.nombre)

        builder.setPositiveButton("Modificar") { dialog, which ->

            // Se llama a la funcion para cargar el fragment de modificar incidencia
            CargarModificarFragment(datosIncidencias, holder)
            Log.i("MostarMenu", "Modificando incidencia")
            respuesta = true
            return@setPositiveButton
        }

        builder.setNegativeButton("Borrar") { dialog, which ->

            // Se llama a la funcion para borrar la incidencia
            respuesta = false
            DialogoBorrarIncidencias(datosIncidencias, holder)
            Log.i("MostarMenu", "Borrando incidencia")
            return@setNegativeButton
        }

        // Creo el dialogo
        val dialog = builder.create()
        // Muestro el dialogo
        dialog.show()

        return respuesta
    }
    // Funcion para mostrar un dialogo de confirmacion de borrado de la incidencia
    fun DialogoBorrarIncidencias(datosIncidencias: DatosIncidencias, holder: ViewHolder): Boolean {
        var respuesta = false
        val builder = AlertDialog.Builder(holder.itemView.context)
        builder.setTitle("¿Desea borrar la incidencia?")
        builder.setMessage(datosIncidencias.ID + " - " + datosIncidencias.nombre)

        builder.setPositiveButton("Confirmar") { dialog, which ->

            respuesta = true
            // Creo un objeto de la clase Incidencia para poder borrar la incidencia
            Incidencia(
                datosIncidencias.nombre,
                datosIncidencias.fecha,
                datosIncidencias.descripcion,
                datosIncidencias.acabada,
                datosIncidencias.foto,
                datosIncidencias.prioridad,
                datosIncidencias.ID
            ).BorrarIncidencia(datosIncidencias.ID) // Llamo a la funcion de borrar incidencia

            // Borro la incidencia de la lista y actualizo el recycler view
            val position = incidencias.indexOf(datosIncidencias)
            incidencias.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, incidencias.size)
            notifyDataSetChanged()

            Log.d("Borrar Incidencia", "Incidencia borrada correctamente")
            return@setPositiveButton
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->

            respuesta = false
            Log.d("Borrar Incidencia", "incidencia no borrada")
            return@setNegativeButton
        }

        // Creo el dialogo
        val dialog = builder.create()
        // Muestro el dialogo
        dialog.show()
        return respuesta
    }








}