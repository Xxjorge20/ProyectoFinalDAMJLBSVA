package MenuConjunto

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu.MenuLateral
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityMainBinding
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityRegistroBinding

class Notificaciones : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializamos el binding
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_registro)

        // Obtener datos del usuario registrado
        val nombre = intent.getStringExtra("nombre")
        val email = intent.getStringExtra("email")

        // Crear notificación local
        val channelId = "Registro"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logoluiscarrillo)
            .setContentTitle("Registro exitoso")
            .setContentText("Bienvenido, $nombre! Tu correo electrónico es $email.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Canal de registro",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Construccion de notificacion
        notificationManager.notify(1, notificationBuilder.build())
        // Cambio de activity
        val intent = Intent(this, MenuLateral::class.java).apply {
            putExtra("nombreusuario", binding.Nombre.text.toString())
        }
        startActivity(intent)
    }


}