package ParteUsuarios.Adapter

import ParteUsuarios.ViewHolder.UsersViewHolder
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoclara.Data.ItemsUsuarios
import ies.luiscarrillo.proyectofinaldamjlbsva.R

class UsuarioAdapter(private var users: List<ItemsUsuarios>) : RecyclerView.Adapter<UsersViewHolder>() {



    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
     val view = LayoutInflater.from(parent.context)
         .inflate(R.layout.activity_mostrar_usuarios, parent,false)
        return UsersViewHolder(view)

    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val item = users[position]
        holder.bind(item)
    }

    // Retorna el tamaño del listado
    override fun getItemCount(): Int {
        return users.size
    }


}



