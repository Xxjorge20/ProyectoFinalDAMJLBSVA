package ParteUsuarios.ViewHolder

import ParteUsuarios.Data.Usuarios
import android.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoclara.Data.ItemsUsuarios
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data.DatosIncidencias
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data.Incidencia
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.ViewHolder.ViewHolder
import ies.luiscarrillo.proyectofinaldamjlbsva.R

class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Crear variables para usar en el ViewHolder
    private val imagen = itemView.findViewById<ImageView>(R.id.imagenUsuario)
    private val email = itemView.findViewById<TextView>(R.id.email)
    private val nombre = itemView.findViewById<TextView>(R.id.nombreUsuario)
    private val apellidos = itemView.findViewById<TextView>(R.id.apellidosUsuario)
    private val privilegios = itemView.findViewById<TextView>(R.id.privilegios)
    private val password = itemView.findViewById<TextView>(R.id.password)


    fun bind(itemsUsuarios: ItemsUsuarios){

        email.text = "Email: " + itemsUsuarios.email
        nombre.text = "Nombre: " + itemsUsuarios.Nombre
        apellidos.text = "Apellidos: " + itemsUsuarios.Apellidos
        privilegios.text = "Privilegios: " + itemsUsuarios.Privilegios
        password.text = "Contraseña: " + itemsUsuarios.Password





        // Al presionar sobre el usuario para borrarlo
        itemView.setOnLongClickListener() {
            // Ampliar Usuarios
            Log.d("Borrar Usuario", "Usuario pulsado")





            true
        }

        itemView.setOnClickListener() {
            // Ampliar Usuario
            Log.d("Ampliar Usuario", "Usuario Pulsado")


            true
        }





    }
}