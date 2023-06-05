package MenuConjunto

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu.MenuLateral
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityMainBinding
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityRegistroBinding
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.MenulateralBinding

class Notificaciones : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializamos el binding
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_registro)



        val nombreRegistrado = intent.getStringExtra("RegistroNombre")
        val correoRegistrado = intent.getStringExtra("RegistroEmail")

        val email = intent.getStringExtra("correo")
        val nombre = intent.getStringExtra("nombre")



        // Crear notificación local
        val channelId = "Registro"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logoluiscarrillo)
            .setContentTitle("Registro exitoso")
            .setContentText("Bienvenido, $nombreRegistrado! Tu correo electrónico es $correoRegistrado.")
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
            putExtra("correo", email)
            putExtra("nombre", nombre)
        }
        startActivity(intent)




    }
    
}