package InicioSesion

import MenuConjunto.Notificaciones
import ParteUsuarios.Data.Usuarios
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
import ies.luiscarrillo.proyectofinaldamjlbsva.R

class RegistroFragment : Fragment() {

    private lateinit var binding: ActivityRegistroBinding
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityRegistroBinding.inflate(inflater, container, false)

        activity?.title = "Nuevo Usuario"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Nuevo Usuario"

        var usuario = Usuarios("", "", "", "","")
        val listaPrivilegios = Usuarios.privilegios.values()
        val listaPrivilegiosString = ArrayList<String>()
        for (privilegio in listaPrivilegios) {
            listaPrivilegiosString.add(privilegio.toString())
        }
        binding.listaPrivi.adapter = ArrayAdapter(
            requireContext(), R.layout.list_item_custom,
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

            usuario.nombre = binding.Nombre.text.toString()
            usuario.apellidos = binding.apellidos.text.toString()
            usuario.email = binding.email.text.toString()
            usuario.password = binding.password.text.toString()


            // Obtenemos el usuario actual

            var usuarioA = FirebaseAuth.getInstance().currentUser
            var correo = usuarioA?.email.toString()
            var nameUser = correo.split("@")[0]
            // Comprobamos que los campos no estén vacíos



            if (usuario.email != null
                && usuario.password != null
                && usuario.nombre != null
                && usuario.apellidos != null) {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        usuario.email, usuario.password).addOnCompleteListener{


                         // VERIFICACION DE CORREO
                        usuarioA?.sendEmailVerification()?.addOnCompleteListener { emailVerificationTask ->
                            if (emailVerificationTask.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Registro exitoso. Se ha enviado un correo de verificación.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                        if (it.isSuccessful){
                            db.collection("usuarios").document(usuario.email)
                                .set(mapOf(
                                    "Nombre" to usuario.nombre,
                                    "Apellidos" to usuario.apellidos,
                                    "email" to usuario.email,
                                    "Password" to usuario.password,
                                    "Privilegios" to usuario.privi
                                ))


                            Log.d("Registro", "Usuario registrado correctamente" + it.result)

                            val intent = Intent(requireContext(), Notificaciones::class.java).apply{
                                putExtra("nombre",nameUser)
                                putExtra("correo",correo)

                                putExtra("RegistroNombre", usuario.nombre)
                                putExtra("RegistroEmail", usuario.email)
                            }

                            startActivity(intent)



                        }
                        else {
                            Toast.makeText(requireContext(),"Error en el registro del nuevo usuario", Toast.LENGTH_SHORT).show()
                            Log.d("Registro", it.exception.toString())
                        }
                }
            }
            else {
                Toast.makeText(requireContext(), "Algun campo está vacio", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
