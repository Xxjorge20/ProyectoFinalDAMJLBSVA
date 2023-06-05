package ies.luiscarrillodesotomayor.gestionincidencias.Menu

import InicioSesion.MainActivity
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.type.DateTime
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data.Incidencia
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu.MenuLateral
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.FragmentModificarIncidenciaBinding
import java.io.ByteArrayOutputStream
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [ModificarIncidencia.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModificarIncidencia : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit  var binding: FragmentModificarIncidenciaBinding
    var nombre: String? = null
    var descripcion: String? = null
    var fecha: String? = null
    var prioridad: String? = null
    var acabada: String? = null
    var id: String? = null
    var foto: String? = null
    var finalizada: Boolean = false
    var tipo : String? = null
    var lugar : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModificarIncidenciaBinding.inflate(inflater, container, false)

        // Obtencion de los datos a traves de otra activity
        nombre = arguments?.getString("nombre")
        descripcion = arguments?.getString("descripcion")
        fecha = arguments?.getString("fecha")
        acabada = arguments?.getString("acabada")
        foto = arguments?.getString("foto")
        prioridad = arguments?.getString("prioridad")
        tipo = arguments?.getString("tipo")
        lugar = arguments?.getString("lugar")
        id = arguments?.getString("id")



        /*
        datosIncidencias.nombre,
        datosIncidencias.fecha,
        datosIncidencias.descripcion,
        datosIncidencias.acabada,
        datosIncidencias.foto,
        datosIncidencias.prioridad,
        datosIncidencias.ID
        * */


        // Mostrar los datos en el log
        Log.i("ModificarIncidencia", "Nombre: $nombre")
        Log.i("ModificarIncidencia", "Descripcion: $descripcion")
        Log.i("ModificarIncidencia", "Fecha: $fecha")
        Log.i("ModificarIncidencia", "Prioridad: $prioridad")
        Log.i("ModificarIncidencia", "Acabada: $acabada")
        Log.i("ModificarIncidencia", "ID: $id")
        Log.i("ModificarIncidencia", "Foto: $foto")
        Log.i("ModificarIncidencia", "Tipo: $tipo")
        Log.i("ModificarIncidencia", "Lugar: $lugar")

        // Limpiar los campos
        resetear()
        binding.TBTituloIncidenciaModificar.setText(nombre)
        binding.TBTituloIncidenciaModificar.isEnabled = false // Desactivar el campo
        binding.TBDescripcionIncidencia2Modificar.setText(descripcion)
        binding.TBDescripcionIncidencia2Modificar.isEnabled = false // Desactivar el campo



        // cargo la lista de prioridades
        val listaPrioridades = Incidencia.Prioridad.values()
        val listaPrioridadesString = ArrayList<String>()
        for (prioridad in listaPrioridades) {
            listaPrioridadesString.add(prioridad.toString())
        }

        // Establezco la prioridad de la incidencia en el spinner de prioridades de la incidencia a modificar
        if (prioridad == Incidencia.Prioridad.ALTA.toString()) {
            binding.listPrioridadModificar.setSelection(0)
        } else if (prioridad == Incidencia.Prioridad.MEDIA.toString()) {
            binding.listPrioridadModificar.setSelection(1)
        } else if (prioridad == Incidencia.Prioridad.BAJA.toString()) {
            binding.listPrioridadModificar.setSelection(2)
        }

        binding.listPrioridadModificar.adapter = android.widget.ArrayAdapter(
            requireContext(),
            R.layout.list_item_custom,
            // recorro el array de prioridades y la añado a la lista
            listaPrioridadesString

        )



        // evento click en la lista de prioridades
        binding.listPrioridadModificar.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(requireContext(), "Prioridad seleccionada: ${listaPrioridades[position]}", Toast.LENGTH_SHORT).show()
            Log.d("Prioridad", listaPrioridades[position].toString())


            // Limpio el color de fondo de todos los items y lo pongo transparente
            for (i in 0 until parent.count) {
                val item = parent.getChildAt(i)
                item.setBackgroundColor(Color.TRANSPARENT)
            }



            // guardo la prioridad seleccionada
            if (listaPrioridades[position] == Incidencia.Prioridad.ALTA) {
                prioridad = Incidencia.Prioridad.ALTA.toString()

                // Establezco el color de fondo solo al item seleccionado
                val item = parent.getChildAt(position)
                item.setBackgroundColor(Color.MAGENTA)

            } else if (listaPrioridades[position] == Incidencia.Prioridad.MEDIA) {
                prioridad = Incidencia.Prioridad.MEDIA.toString()

                // Establezco el color de fondo solo al item seleccionado
                val item = parent.getChildAt(position)
                item.setBackgroundColor(Color.MAGENTA)

            } else if (listaPrioridades[position] == Incidencia.Prioridad.BAJA) {
                prioridad = Incidencia.Prioridad.BAJA.toString()

                // Establezco el color de fondo solo al item seleccionado
                val item = parent.getChildAt(position)
                item.setBackgroundColor(Color.MAGENTA)

            }

            ocultarTeclado()
        }


        // evento click del boton
        binding.BModificarIncidencia.setOnClickListener(){
            actualizarIncidencia()

        }


        // cargo la lista de prioridades
        val tipoLista = Incidencia.tipoIncidencia.values()
        val listaTipo = java.util.ArrayList<String>()
        for (tipo in tipoLista) {
            listaTipo.add(tipo.toString())
        }

        binding.listTipoModificar.adapter = android.widget.ArrayAdapter(
            requireContext(),
            R.layout.list_item_custom,
            // recorro el array de prioridades y la añado a la lista
            listaTipo

        )


        // evento click en la lista de prioridades
        binding.listTipoModificar.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(requireContext(), "Tipo seleccionada: ${tipoLista[position]}", Toast.LENGTH_SHORT).show()
            Log.d("Tipo", tipoLista[position].toString())

            // Limpio el color de fondo de todos los items y lo pongo transparente
            for (i in 0 until parent.count) {
                val item = parent.getChildAt(i)
                item.setBackgroundColor(Color.TRANSPARENT)
            }

            // guardo la prioridad seleccionada
            if (tipoLista[position] == Incidencia.tipoIncidencia.Informaticas) {
                tipo = Incidencia.tipoIncidencia.Informaticas.toString()

                // Establezco el color de fondo solo al item seleccionado
                val item = parent.getChildAt(position)
                item.setBackgroundColor(Color.MAGENTA)

            } else if (tipoLista[position] == Incidencia.tipoIncidencia.Mantenimiento) {
                tipo = Incidencia.tipoIncidencia.Mantenimiento.toString()

                // Establezco el color de fondo solo al item seleccionado
                val item = parent.getChildAt(position)
                item.setBackgroundColor(Color.MAGENTA)

            } else if (tipoLista[position] == Incidencia.tipoIncidencia.Electrico) {
                tipo = Incidencia.tipoIncidencia.Electrico.toString()

                // Establezco el color de fondo solo al item seleccionado
                val item = parent.getChildAt(position)
                item.setBackgroundColor(Color.MAGENTA)

            }
            else if (tipoLista[position] == Incidencia.tipoIncidencia.Otros) {
                tipo = Incidencia.tipoIncidencia.Otros.toString()

                // Establezco el color de fondo solo al item seleccionado
                val item = parent.getChildAt(position)
                item.setBackgroundColor(Color.MAGENTA)

            }

            ocultarTeclado()
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Realizar la navegación deseada cuando se presione el botón de retroceso
                val homeFragment = Intent(activity, MenuLateral::class.java)
                startActivity(homeFragment)
                requireActivity().finish()
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }
    // Ocultar el teclado
    private  fun ocultarTeclado() {
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(requireActivity())
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    /**
     * Esta función actualiza una incidencia con nueva información y vuelve a la actividad principal.
     */
    private fun actualizarIncidencia() {

            try
            {

                nombre = binding.TBTituloIncidenciaModificar.text.toString()
                descripcion = binding.TBDescripcionIncidencia2Modificar.text.toString()
                var comentarioAdccional = binding.editText.text.toString()


                finalizada = binding.checkBoxSi.isChecked

                obtenerUsername { username ->
                    var usuario = username

                    if (usuario == "") {
                        usuario = "Anónimo"
                    }

                    val incidencia = Incidencia(

                        binding.TBTituloIncidenciaModificar.text.toString(),
                        descripcion.toString() + "\n" + comentarioAdccional.toString() + " \n Comentario añadido por: " + usuario.toString(),
                        fecha.toString(),
                        finalizada,
                        foto.toString(),
                        prioridad.toString(),
                        tipo.toString(),
                        lugar.toString(),
                        id.toString()

                    )

                    // Actualizo la incidencia
                    Incidencia.actualizarIncidencia(incidencia)

                    val intent = Intent(requireContext(), MenuLateral::class.java)
                    startActivity(intent)
                }

            }catch (e: Exception){
                Log.e("ModificarIncidencia", "Error al actualizar la incidencia: ${e.message}")
                Toast.makeText(requireContext(), "Error al actualizar la incidencia", Toast.LENGTH_SHORT).show()

            }


        }






    // Obtener al usuario actual
/**
 * La función obtiene el correo electrónico del usuario actual y devuelve su nombre de usuario
 * dividiendo el correo electrónico en el símbolo "@".
 *
 * @return La función `obtenerUsuarioActual()` devuelve una cadena que representa la dirección de
 * correo electrónico del usuario actual sin el nombre de dominio.
 */




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ModificarIncidencia.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ModificarIncidencia().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun resetear(){
        binding.TBTituloIncidenciaModificar.setText("")
        binding.TBDescripcionIncidencia2Modificar.setText("")

        binding.TBTituloIncidenciaModificar.setTextColor(android.graphics.Color.WHITE)
        binding.TBDescripcionIncidencia2Modificar.setTextColor(android.graphics.Color.WHITE)
    }


    fun obtenerUsername(callback: (String) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val correo = auth.currentUser?.email.toString()
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(correo).get()
            .addOnSuccessListener { documentSnapshot ->
                var username = ""
                if (documentSnapshot.exists()) {
                    val nombre = documentSnapshot.getString("Nombre")
                    val apellido = documentSnapshot.getString("Apellidos")
                    username = "$nombre $apellido"
                    Log.d("Usuario", "Datos Usuario: $username")
                }
                Log.d("Usuario", "Datos Usuario: ${documentSnapshot.data}")
                callback(username) // Llamar a la devolución de llamada con el valor de username
            }
    }


}