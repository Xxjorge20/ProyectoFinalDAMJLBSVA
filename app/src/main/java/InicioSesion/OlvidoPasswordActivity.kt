package InicioSesion

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityOlvidoPasswordBinding

class OlvidoPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityOlvidoPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.botonEmailEnviar.setOnClickListener {

                var email = binding.emailOlvidado.text.toString()

                // Validar que el email y la contraseña no estén vacíos
                if (email.isEmpty())
                {
                    // Si están vacíos, mostrar mensaje de error
                    Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show()
                    Log.d("RecuperarPassword", "Por favor, rellene todos los campos")
                }
                else
                {

                    if (!recordarContraseña(email))
                    {
                        //Si el usuario se ha registrado correctamente, mostrar mensaje de éxito
                        Toast.makeText(this, "Se ha enviado un correo para recuperar la contraseña", Toast.LENGTH_SHORT).show()
                        Log.d("RecuperarPassword", "Se ha enviado un correo para recuperar la contraseña")
                        //Volver a la pantalla de login
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else
                    {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        //Si no se ha podido registrar el usuario, mostrar mensaje de error
                        Toast.makeText(this, "No se ha podido enviar el correo para recuperar la contraseña", Toast.LENGTH_SHORT).show()
                        Log.d("RecuperarPassword", "No se ha podido enviar el correo para recuperar la contraseña")
                    }

                }
            }
        }


    fun recordarContraseña(email: String) : Boolean {
        var enviado = false
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener { task ->
                enviado = true
            }
            .addOnFailureListener() {
                enviado = false
            }

        return enviado
    }


}









