package ies.luiscarrillodesotomayor.gestionincidencias.Menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Adapter.adapterIncidencas
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data.DatosIncidencias
import ies.luiscarrillo.proyectofinaldamjlbsva.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CasaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CasaFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
     var recicler: RecyclerView? = null
     lateinit var listaIncidencias : ArrayList<DatosIncidencias>

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
      var vista: View = inflater.inflate(R.layout.fragment_casa2, container, false)

        listaIncidencias = ArrayList()
        recicler =  vista.findViewById(R.id.recycler)
        recicler?.layoutManager = LinearLayoutManager(context)
        // Cargar datos
        cargarDatos()
        recicler?.adapter = adapterIncidencas(listaIncidencias)

        return vista
    }


     fun cargarDatos() {
        val db = FirebaseFirestore.getInstance()

        // Obtengo los datos de la base de datos
        db.collection("Incidencias")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("AñadiendoIncidencias", "${document.id} => ${document.data}")
                    val incidencia = document.toObject(DatosIncidencias::class.java)
                    listaIncidencias.add(incidencia)
                }
              var adapter = adapterIncidencas(listaIncidencias)
                recicler?.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("AñadiendoIncidencias", "Error al obtener las incidencias", exception)
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Casa2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CasaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}