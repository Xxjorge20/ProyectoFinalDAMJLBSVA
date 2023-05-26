package InicioSesion

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu.MenuLateral
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityInicioBinding
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityMainBinding
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityRegistroBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isPasswordVisible = false


   /**
    * La función inicializa Firebase y establece detectores de clics para los botones "Iniciar sesión"
    * y "Olvido de contraseña" en la actividad principal de una aplicación de Android.
    * 
    * @param savedInstanceState Un objeto Bundle que contiene el estado guardado previamente de la
    * actividad. Este parámetro generalmente se usa para restaurar el estado de la actividad después de
    * que se haya destruido y recreado, como durante un cambio de configuración (por ejemplo, rotación
    * de pantalla).
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.sesion.setOnClickListener{
            // Al pulsar sobre el botón INICIAR SESION, comprobamos autentificacion
            //pasandole a Firebase el correo y la contraseña
            login { userName ->





                val user = FirebaseAuth.getInstance().currentUser
                if (user != null && user.isEmailVerified)
                {

                    val intent = Intent(this, MenuLateral::class.java)
                    intent.putExtra("correo", binding.correo.text.toString())
                    intent.putExtra("nombre", userName)
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        this,
                        "Debes verificar tu correo electrónico antes de iniciar sesión.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
        binding.botonOlvidado.setOnClickListener{
            startActivity(Intent(this,OlvidoPasswordActivity::class.java))
        }


       binding.verContrasena.setOnClickListener{
           togglePasswordVisibility()
           true

       }



    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible

        if (isPasswordVisible) {
            binding.EscribirContrasena.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.verContrasena.setImageResource(R.drawable.eyepassword)
        } else {
            binding.EscribirContrasena.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.verContrasena.setImageResource(R.drawable.eyepassword)
        }
    }


   /**
    * La función comprueba si el usuario ha iniciado sesión. Si es así, se llama a la función
    * onUserLoaded, que se pasa como parámetro. Si no, se muestra un mensaje de error.
    *
    */
   private fun login(onUserLoaded: (userName: String) -> Unit) {
       // Si el correo y el password no son campos vacíos:
       if (binding.correo.text.isNotEmpty() && binding.EscribirContrasena.text.isNotEmpty()) {
           FirebaseAuth.getInstance().signInWithEmailAndPassword(
               binding.correo.text.toString(),
               binding.EscribirContrasena.text.toString()
           )
               .addOnCompleteListener { task ->
                   // Si la autenticación tuvo éxito:
                   if (task.isSuccessful) {
                       var nombre: String
                       var apellidos: String
                       var userName: String

                       val auth = FirebaseAuth.getInstance()
                       val db = FirebaseFirestore.getInstance()
                       auth.currentUser?.let {
                           db.collection("usuarios")
                               .whereEqualTo("email", auth.currentUser?.email)
                               .get()
                               .addOnSuccessListener { documents ->
                                   if (!documents.isEmpty) {
                                       val document = documents.documents[0]
                                       nombre = document.getString("Nombre") ?: ""
                                       apellidos = document.getString("Apellidos") ?: ""
                                       userName = nombre + " " + apellidos
                                       onUserLoaded(userName) // Llama a la función lambda con el nombre de usuario
                                   }
                                   finish()
                               }
                               .addOnFailureListener { exception ->
                                   Log.d("Usuario", "Error al obtener el usuario", exception)
                               }
                       }



                   } else {
                       Toast.makeText(this, "Correo o contraseña incorrecta", Toast.LENGTH_SHORT).show()
                   }
               }
       } else {
           Toast.makeText(this, "Algún campo está vacío", Toast.LENGTH_SHORT).show()
       }
   }




    /**
    * La función anula el comportamiento predeterminado del botón Atrás en una actividad de Android y
    * muestra un cuadro de diálogo de confirmación antes de cerrar la actividad.
    */
    override fun onBackPressed() {
        // Aquí puedes realizar las acciones que deseas cuando se presiona el botón de retroceso
        // Por ejemplo, puedes cerrar la actividad actual o mostrar un diálogo de confirmación antes de cerrarla

        // Si deseas mostrar un diálogo de confirmación antes de cerrar la actividad, puedes utilizar AlertDialog:
        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage("¿Estás seguro de que deseas salir?")
            .setPositiveButton("Sí") { _: DialogInterface, _: Int ->
                // Acciones a realizar al aceptar la confirmación
                finishAffinity()
            }
            .setNegativeButton("No", null)
            .show()
    }

}