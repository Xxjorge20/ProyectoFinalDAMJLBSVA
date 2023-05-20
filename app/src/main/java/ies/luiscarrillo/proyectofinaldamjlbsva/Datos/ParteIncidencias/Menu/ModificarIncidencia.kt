package ies.luiscarrillodesotomayor.gestionincidencias.Menu

import InicioSesion.MainActivity
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        prioridad = arguments?.getString("prioridad")
        acabada = arguments?.getString("acabada")
        id = arguments?.getString("id")
        foto = arguments?.getString("foto")
        tipo = arguments?.getString("tipo")

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
            android.R.layout.simple_list_item_1,
            // recorro el array de prioridades y la añado a la lista
            listaPrioridadesString

        )



        // evento click en la lista de prioridades
        binding.listPrioridadModificar.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(requireContext(), "Prioridad seleccionada: ${listaPrioridades[position]}", Toast.LENGTH_SHORT).show()
            Log.d("Prioridad", listaPrioridades[position].toString())

            // guardo la prioridad seleccionada
            if (listaPrioridades[position] == Incidencia.Prioridad.ALTA) {
                prioridad = Incidencia.Prioridad.ALTA.toString()
            } else if (listaPrioridades[position] == Incidencia.Prioridad.MEDIA) {
                prioridad = Incidencia.Prioridad.MEDIA.toString()
            } else if (listaPrioridades[position] == Incidencia.Prioridad.BAJA) {
                prioridad = Incidencia.Prioridad.BAJA.toString()
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
            android.R.layout.simple_list_item_1,
            // recorro el array de prioridades y la añado a la lista
            listaTipo

        )


        // evento click en la lista de prioridades
        binding.listTipoModificar.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(requireContext(), "Tipo seleccionada: ${tipoLista[position]}", Toast.LENGTH_SHORT).show()
            Log.d("Tipo", tipoLista[position].toString())

            // guardo la prioridad seleccionada
            if (tipoLista[position] == Incidencia.tipoIncidencia.Informaticas) {
                tipo = Incidencia.tipoIncidencia.Informaticas.toString()
            } else if (tipoLista[position] == Incidencia.tipoIncidencia.Mantenimiento) {
                tipo = Incidencia.tipoIncidencia.Mantenimiento.toString()
            } else if (tipoLista[position] == Incidencia.tipoIncidencia.Electrico) {
                tipo = Incidencia.tipoIncidencia.Electrico.toString()
            }
            else if (tipoLista[position] == Incidencia.tipoIncidencia.Otros) {
                tipo = Incidencia.tipoIncidencia.Otros.toString()
            }

            ocultarTeclado()
        }




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


        nombre = binding.TBTituloIncidenciaModificar.text.toString()
        descripcion = binding.TBDescripcionIncidencia2Modificar.text.toString()
        var comentarioAdccional = binding.editText.text.toString()

        finalizada = binding.checkBoxSi.isChecked

        var usuario = obtenerUsuarioActual()


        val incidencia = Incidencia(

            binding.TBTituloIncidenciaModificar.text.toString(),
            descripcion.toString()+ "\n" + comentarioAdccional.toString() + " \n Comentario añadido por: " + usuario.toString(),
            fecha.toString(),
            finalizada,
            foto.toString(),
            prioridad.toString(),
            tipo.toString(),
            id.toString()

        )

        // Actualizo la incidencia
        Incidencia.actualizarIncidencia(incidencia)

        // vuelvo a la activity principal
        val email = FirebaseAuth.getInstance().currentUser?.email
        val intent = Intent(requireContext(), MenuLateral::class.java)
        intent.putExtra("nombre", usuario)
        intent.putExtra("correo", email)
        startActivity(intent)


    }

    // Obtener al usuario actual
/**
 * La función obtiene el correo electrónico del usuario actual y devuelve su nombre de usuario
 * dividiendo el correo electrónico en el símbolo "@".
 * 
 * @return La función `obtenerUsuarioActual()` devuelve una cadena que representa la dirección de
 * correo electrónico del usuario actual sin el nombre de dominio.
 */
    private fun obtenerUsuarioActual(): String {
        val usuario = FirebaseAuth.getInstance().currentUser
        var usuarioActual = usuario?.email
        var nombre = usuarioActual?.split("@")
        return nombre?.get(0).toString()
    }


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



}