package InicioSesion

import ParteUsuarios.Data.Usuarios
import ParteUsuarios.InicioActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    var usuario = Usuarios("","","","")
    lateinit var binding: ActivityRegistroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Nuevo Usuario" // Cambia el titulo de  la pantalla

        // Cargar la lista de privilegios
        val listaPrivilegios = Usuarios.privilegios.values()
        val listaPrivilegiosString = ArrayList<String>()
        for (privilegio in listaPrivilegios) {
            listaPrivilegiosString.add(privilegio.toString())
        }
        binding.listaPrivi.adapter = android.widget.ArrayAdapter(
            this, android.R.layout.simple_list_item_1,
            // Recorro el array de prioridades y la añado a la lista
            listaPrivilegiosString

        )

        // Evento click en la lista de privilegios
        binding.listaPrivi.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, "Privilegio seleccionado: ${listaPrivilegios[position]}", Toast.LENGTH_SHORT).show()
            Log.d("Privilegios", listaPrivilegios[position].toString())

            // Guardo los privilegios seleccionada
            if (listaPrivilegios[position] == Usuarios.privilegios.admin) {
                usuario.privi = Usuarios.privilegios.admin.toString()
            } else if (listaPrivilegios[position] ==  Usuarios.privilegios.gestor) {
                usuario.privi =  Usuarios.privilegios.gestor.toString()
            } else if (listaPrivilegios[position] ==  Usuarios.privilegios.user) {
                usuario.privi =  Usuarios.privilegios.user.toString()
            }


        }


        binding.registro.setOnClickListener {
            // Comprobamos que ningún campo esté vacio:
            if (binding.email.text.isNotEmpty() && binding.password.text.isNotEmpty()
                && binding.Nombre.text.isNotEmpty() && binding.apellidos.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.email.text.toString(), binding.password.text.toString()).addOnCompleteListener{

                    if (it.isSuccessful){ // Si se han registrado los datos
                        db.collection("usuarios").document(binding.email.text.toString())
                            .set(mapOf(
                                "nombre" to binding.Nombre.text.toString(),
                                "apellidos" to binding.apellidos.text.toString(),
                                "email" to binding.email.text.toString(),
                                "privilegios" to usuario.privi,
                            )
                            )



                        // Accedemos a la pantalla InicioActivity parae dar la bienvenida al usuario
                        val intent = Intent(this, InicioActivity::class.java).apply {
                            putExtra("nombreusuario", binding.Nombre.text.toString())
                        }
                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this,"Error en el registro del nuevo usuario", Toast.LENGTH_SHORT).show()
                    }

                }



            }
            else {
                Toast.makeText(this, "Algun campo está vacio", Toast.LENGTH_SHORT).show()
            }
        }


    }

}
