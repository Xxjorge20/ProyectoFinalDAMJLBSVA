package ParteUsuarios

import InicioSesion.MainActivity
import InicioSesion.RegistroActivity
import ParteUsuarios.Adapter.UsuarioAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoclara.Data.ItemsUsuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityInicioBinding

class InicioActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInicioBinding
    private lateinit var adapterUsuarios : Adapter
    private lateinit var listaUsuarios : ArrayList<ItemsUsuarios>
    var recycler : RecyclerView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializamos la lista
        listaUsuarios = ArrayList()
        // Inflamos el binding
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.registro.setOnClickListener{
            startActivity(Intent(this, RegistroActivity::class.java))
        }


        // Boton mostrar todos los usuarios
        binding.mostrarUsuario.setOnClickListener{
            cargarDatos()
        }

        // Acceder a la galeria
        binding.photo.setOnClickListener{
           // startActivity(Intent(this,Galeria::class.java))
        }




    }


    fun cargarDatos() {
        // Limpiamos la lista de usuarios
        listaUsuarios.clear()
        val db = FirebaseFirestore.getInstance()
        // Obtengo los datos de la base de datos
        db.collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Añadiendo Usuarios", "${document.id} => ${document.data}")
                    val usuario = document.toObject(ItemsUsuarios::class.java)
                    listaUsuarios.add(usuario)
                    // Muestro el recyclerView
                    binding.mostrarTodos.layoutManager = LinearLayoutManager(this)
                    binding.mostrarTodos.adapter = UsuarioAdapter(listaUsuarios)
                    // Vuelvo a actualizar el adapter para el borrado
                    var adapter = UsuarioAdapter(listaUsuarios)
                    recycler?.adapter = adapter
                    val position = listaUsuarios.indexOf(ItemsUsuarios())
                    adapter.notifyItemRemoved(position)
                    adapter.notifyDataSetChanged()


                }


            }
            .addOnFailureListener { exception ->
                Log.w("Añadiendo Usuarios", "Error al obtener los usuarios.", exception)
            }


        // Boton para cerrar la sesion de usuario
        binding.cerrarSesion.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            // Volvemos a nuestro Main Activity
            startActivity(Intent(this, MainActivity::class.java))
        }


    }



}