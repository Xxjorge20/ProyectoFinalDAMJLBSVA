package InicioSesion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu.MenuLateral
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityInicioBinding
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityMainBinding
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityRegistroBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var binding1 : ActivityInicioBinding
    private lateinit var binding2 : ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding1 = ActivityInicioBinding.inflate(layoutInflater)
        binding2 = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sesion.setOnClickListener{
            // Al pulsar sobre el botón INICIAR SESION, comprobamos autentificacion
            //pasandole a Firebase el correo y la contraseña
            login()
        }
        binding.botonOlvidado.setOnClickListener{
            startActivity(Intent(this,OlvidoPasswordActivity::class.java))
        }
    }

    private fun login() {
        // Si el correo y el password no son campos vacios:
        if (binding.correo.text.isNotEmpty() && binding.EscribirContrasena.text.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                binding.correo.text.toString(),
                binding.EscribirContrasena.text.toString()
            )
                .addOnCompleteListener{
                    // Si la autenticacion tuvo exito:
                    if (it.isSuccessful)
                    {
                        // Obtengo los datos de la base de datos y cambio de activity // CAMBIAR PARA PROBAR DISTINTAS PARTES
                        // MenuLateral -> Parte de Jorge
                        // InicioActivity -> Parte de Sergio
                        val intent = Intent(this, MenuLateral::class.java)

                        intent.putExtra("correo", binding.correo.text.toString())
                        var correo = binding.correo.text.toString()
                       var nombre = correo.split("@")[0]
                        intent.putExtra("nombre", nombre)


                        startActivity(intent)
                    }
                    else {
                        Toast.makeText(this,"Correo o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }

                }
        }
        else {
            Toast.makeText(this,"Algún campo está vacío", Toast.LENGTH_SHORT).show()
        }
    }
}