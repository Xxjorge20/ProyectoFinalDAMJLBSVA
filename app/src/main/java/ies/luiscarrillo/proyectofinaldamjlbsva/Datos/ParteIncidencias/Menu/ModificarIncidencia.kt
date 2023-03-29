package ies.luiscarrillodesotomayor.gestionincidencias.Menu

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage
import ies.luiscarrillodesotomayor.gestionincidencias.Datos.Incidencia
import ies.luiscarrillodesotomayor.gestionincidencias.databinding.FragmentModificarIncidenciaBinding
import java.io.ByteArrayOutputStream

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
    enum class acaba{
        SI,NO
    }

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
        val nombre = arguments?.getString("nombre")
        val descripcion = arguments?.getString("descripcion")
        val fecha = arguments?.getString("fecha")
        val prioridad = arguments?.getString("prioridad")
        val acabada = arguments?.getString("acabada")



        // Mostrar los datos en el log
        Log.i("ModificarIncidencia", "Nombre: $nombre")
        Log.i("ModificarIncidencia", "Descripcion: $descripcion")
        Log.i("ModificarIncidencia", "Fecha: $fecha")
        Log.i("ModificarIncidencia", "Prioridad: $prioridad")

        // Limpiar los campos
        resetear()
        binding.TBTituloIncidenciaModificar.setText(nombre)
        binding.TBTituloIncidenciaModificar.isEnabled = false // Desactivar el campo
        binding.TBDescripcionIncidencia2Modificar.setText(descripcion)

        // cargo la lista de prioridades
        val listaPrioridades = Incidencia.Prioridad.values()
        val listaPrioridadesString = ArrayList<String>()
        for (prioridad in listaPrioridades) {
            listaPrioridadesString.add(prioridad.toString())
        }
        binding.listPrioridadModificar.adapter = android.widget.ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            // recorro el array de prioridades y la añado a la lista
            listaPrioridadesString

        )

        // establezco la prioridad seleccionada
        binding.listPrioridadModificar.setSelection(listaPrioridadesString.indexOf(prioridad))

        // evento click del boton
        binding.BModificarIncidencia.setOnClickListener(){
            val nombre = binding.TBTituloIncidenciaModificar.text.toString()
            val descripcion = binding.TBDescripcionIncidencia2Modificar.text.toString()
            val prioridad = binding.listPrioridadModificar.selectedItem.toString()

        }









        // Inflate the layout for this fragment
        return binding.root
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