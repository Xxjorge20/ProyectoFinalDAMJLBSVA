package ParteUsuarios.Adapter

import ParteUsuarios.Data.Usuarios
import ParteUsuarios.ViewHolder.UsersViewHolder
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoclara.Data.ItemsUsuarios
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.ViewHolder.ViewHolder
import ies.luiscarrillo.proyectofinaldamjlbsva.R

class UsuarioAdapter(private var users: ArrayList<ItemsUsuarios>) : RecyclerView.Adapter<UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_mostrar_usuarios, parent,false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = users[position]
        holder.bind(item)


        holder.itemView.setOnLongClickListener() {
            DialogoBorrarUser(item, holder)
        }

    }

    override fun getItemCount(): Int {
        return users.size
    }

    /*
    fun setData(data: List<ItemsUsuarios>) {
        users = data
        notifyDataSetChanged()
    }
    */


    fun DialogoBorrarUser(usuarios: ItemsUsuarios, holder: UsersViewHolder): Boolean {
        var respuesta = false
        val builder = AlertDialog.Builder(holder.itemView.context)
        builder.setTitle("¿Desea borrar el Usuari@?")
        builder.setMessage(usuarios.email + " - " + usuarios.Nombre)

        builder.setPositiveButton("Confirmar") { dialog, which ->

            respuesta = true
            Usuarios(
                usuarios.Nombre,
                usuarios.email,
                usuarios.Apellidos
                ,usuarios.Privilegios,
                usuarios.Password
            ).BorrarUsuario(usuarios.email) // Llamo a la funcion de borrar incidencia

            val position = users.indexOf(usuarios)
            users.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, users.size)
            notifyDataSetChanged()
            Toast.makeText(
                holder.itemView.context,
                "Usuario borrado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            return@setPositiveButton
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->

            respuesta = false
            Log.d("Borrar Usuario", "Usuario no borrado")
            return@setNegativeButton
        }

        // Creo el dialogo
        val dialog = builder.create()
        // Muestro el dialogo
        dialog.show()
        return respuesta
    }




}



