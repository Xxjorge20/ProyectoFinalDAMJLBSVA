package InicioSesion

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityOlvidoPasswordBinding

class OlvidoPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityOlvidoPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.emailOlvidado.setOnClickListener {
            var comprobacionEmail = binding.textEmail.text.toString()
            if (comprobacionEmail.isNotEmpty()) {
                enviarEmail(comprobacionEmail)
            }

        }


    }

    fun enviarEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "Email enviado.")
                } else {
                    Log.e(ContentValues.TAG, "Fallo al enviar el email.")
                }
            }
    }
}
