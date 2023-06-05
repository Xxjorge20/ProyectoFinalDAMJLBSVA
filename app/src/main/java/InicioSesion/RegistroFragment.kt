package InicioSesion

import MenuConjunto.Notificaciones
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
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu.MenuLateral
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityRegistroBinding

class RegistroFragment : Fragment() {

    private lateinit var binding: ActivityRegistroBinding
    private val db = FirebaseFirestore.getInstance()
    private var usuarioA = FirebaseAuth.getInstance().currentUser
    private var auth = FirebaseAuth.getInstance()
    private var email = usuarioA?.email.toString()
    val usuario = Usuarios("", "", "", "","")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.title = "Nuevo Usuario"

        usuarioA = FirebaseAuth.getInstance().currentUser
        auth = FirebaseAuth.getInstance()
        val usuarioActual = Usuarios("", "", "", "","")

        obtenerUser { usuarios ->

                if (usuarios != null) {
                    usuarioActual.email = usuarios.email
                }
            if (usuarios != null) {
                usuarioActual.privi = usuarios.privi
            }
            if (usuarios != null) {
                usuarioActual.nombre = usuarios.nombre
            }
            if (usuarios != null) {
                usuarioActual.apellidos = usuarios.apellidos
            }
            if (usuarios != null) {
                usuarioActual.password = usuarios.password
            }
                Log.d("Usuario", "Usuario actual: $usuarioActual")

        }



        val listaPrivilegios = Usuarios.privilegios.values()
        val listaPrivilegiosString = ArrayList<String>()
        for (privilegio in listaPrivilegios) {
            listaPrivilegiosString.add(privilegio.toString())
        }
        binding.listaPrivi.adapter = ArrayAdapter(
            requireContext(), ies.luiscarrillo.proyectofinaldamjlbsva.R.layout.list_item_custom,
            listaPrivilegiosString
        )

        binding.listaPrivi.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(requireContext(), "Privilegio seleccionado: ${listaPrivilegios[position]}", Toast.LENGTH_SHORT).show()
            Log.d("Privilegios", listaPrivilegios[position].toString())

            // Limpio el color de fondo de todos los items y lo pongo transparente
            for (i in 0 until parent.childCount) {
                parent.getChildAt(i).setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }


            if (listaPrivilegios[position] == Usuarios.privilegios.admin) {
                usuario.privi = Usuarios.privilegios.admin.toString()

                // Pongo el color de fondo del item seleccionado
                view.setBackgroundColor(resources.getColor(ies.luiscarrillo.proyectofinaldamjlbsva.R.color.purple_500))

            } else if (listaPrivilegios[position] == Usuarios.privilegios.gestor) {
                usuario.privi = Usuarios.privilegios.gestor.toString()

                // Pongo el color de fondo del item seleccionado
                view.setBackgroundColor(resources.getColor(ies.luiscarrillo.proyectofinaldamjlbsva.R.color.purple_500))

            } else if (listaPrivilegios[position] == Usuarios.privilegios.user) {
                usuario.privi = Usuarios.privilegios.user.toString()

                // Pongo el color de fondo del item seleccionado
                view.setBackgroundColor(resources.getColor(ies.luiscarrillo.proyectofinaldamjlbsva.R.color.purple_500))
            }
        }

        binding.registro.setOnClickListener {

            usuario.nombre = binding.Nombre.text.toString()
            usuario.apellidos = binding.apellidos.text.toString()
            usuario.email = binding.email.text.toString()
            usuario.password = binding.password.text.toString()


            // Obtenemos el usuario actual


            // Comprobamos que los campos no estén vacíos



            val nombre = binding.Nombre.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (nombre.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        val user = authResult.user
                        if (user != null) {

                            // Enviamos el email de verificación

                            /*

                            user.sendEmailVerification()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        requireContext(),
                                        "Email de verificación enviado a ${user.email}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("Registro", "Email de verificación enviado a ${user.email}")
                                }
                                .addOnFailureListener { emailException ->
                                    Toast.makeText(
                                        requireContext(),
                                        "Error al enviar el email de verificación: ${emailException.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("Registro", emailException.toString())
                                }

                            */
                            db.collection("usuarios").document(email)
                                .set(
                                    mapOf(
                                        "Nombre" to usuario.nombre,
                                        "Apellidos" to usuario.apellidos,
                                        "email" to usuario.email,
                                        "Password" to usuario.password,
                                        "Privilegios" to usuario.privi
                                    )
                                )
                                .addOnSuccessListener {
                                    Log.d("Registro", "Usuario registrado correctamente: ${user.email}")
                                    FirebaseAuth.getInstance().signOut()
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(usuarioActual.email, usuarioActual.password)
                                        .addOnSuccessListener { signInResult ->
                                            Log.d("Registro", "Usuario logueado correctamente: ${signInResult.user?.email}")

                                            val intent = Intent(requireContext(), Notificaciones::class.java).apply {
                                                putExtra("RegistroNombre", usuario.nombre)
                                                putExtra("RegistroEmail", usuario.email)
                                            }

                                            startActivity(intent)
                                        }
                                        .addOnFailureListener { signInException ->
                                            Toast.makeText(
                                                requireContext(),
                                                "Error al iniciar sesión: ${signInException.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            Log.d("Registro", signInException.toString())
                                        }
                                }
                                .addOnFailureListener { registerException ->
                                    Toast.makeText(
                                        requireContext(),
                                        "Error en el registro del nuevo usuario: ${registerException.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("Registro", registerException.toString())
                                }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Error en el registro del nuevo usuario",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("Registro", "AuthResult user is null")
                        }
                    }
                    .addOnFailureListener { registerException ->
                        Toast.makeText(
                            requireContext(),
                            "Error en el registro del nuevo usuario: ${registerException.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Registro", registerException.toString())
                    }
            } else {
                Toast.makeText(requireContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show()
            }


        }
    }

    // Funcion para obtener el usuario actual de la base de datos

    fun obtenerUser(callback: (Usuarios?) -> Unit) {
        var usuario = Usuarios("", "", "", "", "")
        usuarioA = FirebaseAuth.getInstance().currentUser
        var correo = usuarioA?.email.toString()


        auth.currentUser.let {
            db.collection("usuarios").document(correo).get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    usuario.nombre = documentSnapshot.get("Nombre").toString()
                    usuario.apellidos = documentSnapshot.get("Apellidos").toString()
                    usuario.email = documentSnapshot.get("email").toString()
                    usuario.password = documentSnapshot.get("Password").toString()
                    usuario.privi = documentSnapshot.get("Privilegios").toString()

                    callback(usuario)
                } else {
                    callback(null)
                }
            }.addOnFailureListener {
                callback(null)
            }
        }


    }



}
