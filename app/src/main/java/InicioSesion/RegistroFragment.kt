package InicioSesion

import ParteUsuarios.Data.Usuarios
import ParteUsuarios.InicioFragment

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityRegistroBinding

class RegistroFragment : Fragment() {

    private lateinit var binding: ActivityRegistroBinding
    private val db = FirebaseFirestore.getInstance()
    private var usuario = Usuarios("", "", "", "")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Nuevo Usuario"

        val listaPrivilegios = Usuarios.privilegios.values()
        val listaPrivilegiosString = ArrayList<String>()
        for (privilegio in listaPrivilegios) {
            listaPrivilegiosString.add(privilegio.toString())
        }
        binding.listaPrivi.adapter = ArrayAdapter(
            requireContext(), R.layout.simple_list_item_1,
            listaPrivilegiosString
        )

        binding.listaPrivi.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(requireContext(), "Privilegio seleccionado: ${listaPrivilegios[position]}", Toast.LENGTH_SHORT).show()
            Log.d("Privilegios", listaPrivilegios[position].toString())

            if (listaPrivilegios[position] == Usuarios.privilegios.admin) {
                usuario.privi = Usuarios.privilegios.admin.toString()
            } else if (listaPrivilegios[position] == Usuarios.privilegios.gestor) {
                usuario.privi = Usuarios.privilegios.gestor.toString()
            } else if (listaPrivilegios[position] == Usuarios.privilegios.user) {
                usuario.privi = Usuarios.privilegios.user.toString()
            }
        }

        binding.registro.setOnClickListener {
            if (binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty()
                && binding.Nombre.text.isNotEmpty() && binding.apellidos.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.email.text.toString(), binding.password.text.toString()).addOnCompleteListener{

                    if (it.isSuccessful){
                        db.collection("usuarios").document(binding.email.text.toString())
                            .set(mapOf(
                                "nombre" to binding.Nombre.text.toString(),
                                "apellidos" to binding.apellidos.text.toString(),
                                "email" to binding.email.text.toString(),
                                "privilegios" to usuario.privi,
                            ))

                        val intent = Intent(requireContext(), InicioFragment::class.java).apply {
                            putExtra("nombreusuario", binding.Nombre.text.toString())
                        }
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(requireContext(),"Error en el registro del nuevo usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                Toast.makeText(requireContext(), "Algun campo está vacio", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
