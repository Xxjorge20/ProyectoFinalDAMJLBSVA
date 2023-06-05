package ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Adapter

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data.DatosIncidencias
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data.Incidencia
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu.MenuLateral
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.ViewHolder.ViewHolder
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillodesotomayor.gestionincidencias.Menu.ModificarIncidencia


class adapterIncidencas(private var incidencias: ArrayList<DatosIncidencias>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_incidencia, parent, false)
        return ViewHolder(view)
    }

    /**
     * Esta función carga datos e imágenes para una lista de incidentes y los establece en un
     * ViewHolder en un RecyclerView, al mismo tiempo que permite acciones de clic largo para
     * establecer privilegios.
     * 
     * @param holder El objeto ViewHolder que representa la vista del elemento actual en RecyclerView.
     * Contiene referencias a las vistas que deben actualizarse con los datos del elemento actual.
     * @param position El parámetro de posición en el método onBindViewHolder hace referencia a la
     * posición del elemento en RecyclerView que representa ViewHolder. Se utiliza para recuperar los
     * datos correspondientes de la fuente de datos (en este caso, la lista de incidencias) y
     * vincularlos a las vistas en ViewHolder.
     */
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
                holder.imagen.setImageResource(R.drawable.incidencia)
                Log.i("Imagen", "Error al cargar la imagen")
            }
        }


        holder.titulo.text = incidencia.nombre
        holder.descripcion.text = incidencia.descripcion
        holder.tipo.text = incidencia.tipo



        holder.fecha.text = incidencia.fecha
        // Establecimiento del color de los diferentes textos
        holder.titulo.setTextColor(android.graphics.Color.WHITE)
        holder.descripcion.setTextColor(android.graphics.Color.WHITE)
        holder.fecha.setTextColor(android.graphics.Color.WHITE)
        holder.bind(incidencias[position])

        val db = FirebaseFirestore.getInstance()
        val correo = auth.currentUser?.email.toString()

        holder.itemView.setOnLongClickListener(){
            establecerPrivilegios(db, correo, holder, incidencia)
            true
        }


    }

  /**
   * Esta función comprueba los privilegios de un usuario y muestra las opciones correspondientes.
   * 
   * @param db Instancia de FirebaseFirestore utilizada para acceder a la base de datos de Firestore.
   * @param correo correo es un parámetro String que representa la dirección de correo electrónico de
   * un usuario.
   * @param holder ViewHolder es una clase que contiene referencias a las vistas dentro de un elemento
   * RecyclerView. Se utiliza para reciclar de manera eficiente las vistas a medida que el usuario se
   * desplaza por una lista. En esta función, ViewHolder se usa para acceder al contexto de la vista de
   * elementos, que se necesita para mostrar un mensaje Toast.
   * @param incidencia El parámetro "incidencia" es de tipo "DatosIncidencias" y es un objeto que
   * representa una incidencia con sus correspondientes datos como título, descripción, fecha,
   * ubicación, etc.
   */
    private fun establecerPrivilegios(
        db: FirebaseFirestore,
        correo: String,
        holder: ViewHolder,
        incidencia: DatosIncidencias
    ) {
        db.collection("usuarios").document(correo).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val privilegios = documentSnapshot.getString("Privilegios")

                when (privilegios) {
                    "user" -> {
                        // No tiene permisos, mostrar toast

                        Toast.makeText(
                            holder.itemView.context,
                            "No tienes permisos para modificar y borrar incidencias",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    "gestor" -> {
                        // Tiene permisos, mostrar opciones
                        if (!DialogoModificarBorrar(incidencia, holder)) {
                            Log.i("MostarMenu", "Modificando incidencia")
                        } else {
                            Log.i("MostarMenu", "Borrando incidencia")
                        }
                    }

                   "admin" ->{
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
    }

    override fun getItemCount(): Int {
       return incidencias.size
    }

   /**
    * Esta función carga un fragmento para modificar una incidencia cuando se selecciona desde un menú.
    * 
    * @param datosIncidencias Es un objeto de la clase DatosIncidencias que contiene información sobre
    * una incidencia, como su ID, nombre, fecha, descripción, prioridad, si está terminada o no, foto y
    * tipo.
    * @param holder El parámetro "titular" es del tipo ViewHolder, que es una clase que se usa en
    * RecyclerView para mantener y administrar las vistas de un solo elemento de la lista. En esta
    * función, se utiliza para acceder al contexto de la vista del elemento y reemplazar el fragmento
    * actual por uno nuevo.
    */

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
        args.putString("tipo", datosIncidencias.tipo)
       args.putString("lugar", datosIncidencias.lugar)


        fragment.arguments = args
        // Asociar con el menu de la aplicacion de la parte de incidencias
        val fragmentManager: FragmentManager = (holder.itemView.context as MenuLateral).supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragment_container_view, fragment).commit()

    }

   /**
    * La función muestra un cuadro de diálogo con opciones para modificar o eliminar una incidencia y
    * devuelve un valor booleano basado en la selección del usuario.
    * 
    * @param datosIncidencias un objeto de tipo DatosIncidencias que contiene información sobre una
    * incidencia
    * @param holder El parámetro "titular" es una instancia de la clase ViewHolder, que se usa para
    * contener referencias a las vistas que se muestran en un RecyclerView. Se pasa a la función para
    * que la función pueda acceder al contexto de la vista y realizar acciones en las vistas si es
    * necesario.
    * @return un valor booleano, que está determinado por la selección del usuario en el cuadro de
    * diálogo. Si el usuario selecciona "Modificar", la función devuelve verdadero. Si el usuario
    * selecciona "Borrar", la función devuelve falso.
    */

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
    /**
     * La función muestra un diálogo de confirmación para eliminar una incidencia y realiza la
     * eliminación si se confirma.
     * 
     * @param datosIncidencias Un objeto de tipo DatosIncidencias, que contiene información sobre una
     * incidencia.
     * @param holder El parámetro "titular" es una instancia de la clase ViewHolder, que contiene
     * referencias a las vistas que componen un solo elemento en RecyclerView. Se usa en esta función
     * para obtener el contexto de la vista del elemento, que se necesita para mostrar un mensaje Toast
     * y crear un AlertDialog.
     * @return un valor booleano, que está determinado por la respuesta del usuario al cuadro de
     * diálogo de confirmación. Si el usuario confirma la eliminación, la función devuelve verdadero.
     * Si el usuario cancela la eliminación, la función devuelve falso.
     */
    
    fun DialogoBorrarIncidencias(datosIncidencias: DatosIncidencias, holder: ViewHolder): Boolean {
        var respuesta = false
        val builder = AlertDialog.Builder(holder.itemView.context)
        builder.setTitle("¿Desea borrar la incidencia?")
        builder.setMessage(datosIncidencias.ID + " - " + datosIncidencias.nombre)

        builder.setPositiveButton("Confirmar") { dialog, which ->

            respuesta = true
            Incidencia(
                datosIncidencias.nombre,
                datosIncidencias.descripcion,
                datosIncidencias.fecha,
                datosIncidencias.acabada,
                datosIncidencias.foto,
                datosIncidencias.prioridad,
                datosIncidencias.tipo,
                datosIncidencias.lugar,
                datosIncidencias.ID
            ).BorrarIncidencia(datosIncidencias.ID) // Llamo a la funcion de borrar incidencia

            val position = incidencias.indexOf(datosIncidencias)
            incidencias.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, incidencias.size)
            notifyDataSetChanged()
            Toast.makeText(
                holder.itemView.context,
                "Incidencia borrada correctamente",
                Toast.LENGTH_SHORT
            ).show()

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