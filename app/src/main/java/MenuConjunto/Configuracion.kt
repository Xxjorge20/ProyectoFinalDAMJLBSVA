package MenuConjunto

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import com.google.firebase.auth.FirebaseAuth
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu.MenuLateral
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.FragmentConfiguracionBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Configuracion.newInstance] factory method to
 * create an instance of this fragment.
 */
class Configuracion : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentConfiguracionBinding? = null

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

        activity?.setTitle("Configuracion")
        // Inflate the layout for this fragment
        binding= FragmentConfiguracionBinding.inflate(inflater, container, false)
        binding!!.switch1.setOnClickListener {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            if (binding!!.switch1.isChecked) {
                // Activar modo oscuro
                if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            } else {
                // Activar modo claro
                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
            recreate(requireActivity() as MenuLateral)
        }


        binding!!.root.isFocusableInTouchMode = true
        binding!!.root.requestFocus()
        binding!!.root.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                var usuario = FirebaseAuth.getInstance().currentUser
                var correo = usuario?.email.toString()
                var nameUser = correo.split("@")[0]

                // Aquí puedes realizar las acciones que deseas cuando se presiona el botón de retroceso
                // Por ejemplo, puedes cerrar el Fragment actual o mostrar un diálogo de confirmación antes de cerrarlo



                // Si deseas cerrar el Fragment actual, puedes utilizar fragmentManager
                fragmentManager?.beginTransaction()?.remove(this)?.commit()

                val CasaFragment = Intent(activity, MenuLateral::class.java)
                CasaFragment.putExtra("correo", correo)
                CasaFragment.putExtra("nombre", nameUser)
                startActivity(CasaFragment)



                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }



        return binding!!.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Configuracion.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Configuracion().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}