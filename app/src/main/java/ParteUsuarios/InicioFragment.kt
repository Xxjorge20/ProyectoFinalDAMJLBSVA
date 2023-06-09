package ParteUsuarios

import InicioSesion.MainActivity
import InicioSesion.RegistroFragment
import ParteUsuarios.Adapter.UsuarioAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectoclara.Data.ItemsUsuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu.MenuLateral
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivityInicioBinding
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.FragmentInsertarIncidenciaBinding
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.MenulateralBinding

class InicioFragment : Fragment() {

    private lateinit var binding: ActivityInicioBinding
    private lateinit var bindingMenu : MenulateralBinding
    private lateinit var listaUsuarios: ArrayList<ItemsUsuarios>
    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    var recycler: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflamos el binding
        binding = ActivityInicioBinding.inflate(inflater, container, false)

        binding.root.isFocusableInTouchMode = true
        binding.root.requestFocus()
        binding.root.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                var usuario = FirebaseAuth.getInstance().currentUser
                var correo = usuario?.email.toString()
                var nameUser = correo.split("@")[0]

                // Aquí puedes realizar las acciones que deseas cuando se presiona el botón de retroceso
                // Por ejemplo, puedes cerrar el Fragment actual o mostrar un diálogo de confirmación antes de cerrarlo



                // Si deseas cerrar el Fragment actual, puedes utilizar fragmentManager
                fragmentManager?.beginTransaction()?.remove(this)?.commit()

                val CasaFragment = Intent(activity, MenuLateral::class.java)
                CasaFragment.putExtra("correo", correo)
                CasaFragment.putExtra("nombre", nameUser)
                startActivity(CasaFragment)



                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Realizar la navegación deseada cuando se presione el botón de retroceso
                val homeFragment = Intent(activity, MenuLateral::class.java)
                startActivity(homeFragment)
                requireActivity().finish()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos la lista
        listaUsuarios = ArrayList()
        listaUsuarios.clear()
        activity?.setTitle("Ver Usuarios")

        arguments?.let {
            val correo = it.getString("correo")

            auth.currentUser?.let {
                if (correo != null) {
                    db.collection("usuarios").document(correo).get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                val privilegios = documentSnapshot.getString("privilegios")


                                if (privilegios == "user") {

                                    // Menu
                                    bindingMenu.navView.menu.findItem(R.id.nav_verUsuarios).isVisible = false
                                    bindingMenu.navView.menu.findItem(R.id.nav_insertarUsuario).isVisible = false


                                    binding.mostrarUsuario.isInvisible = true
                                    binding.registro.isInvisible = true
                                    binding.photo.isInvisible = true
                                }
                                if (privilegios == "gestor") {
                                    binding.mostrarUsuario.isInvisible = true
                                }
                            }
                            Log.d("Usuario", "Datos Usuario: ${documentSnapshot.data}")
                        }
                }
            }
        }

        binding.registro.setOnClickListener {
            startActivity(Intent(requireContext(), RegistroFragment::class.java))
        }

        // Boton mostrar todos los usuarios
        binding.mostrarUsuario.setOnClickListener {
            listaUsuarios.clear()
            cargarDatos()
        }

        // Acceder a la galeria
        /*
        binding.photo.setOnClickListener {
            startActivity(Intent(requireContext(), Galeria::class.java))
        }*/
    }

    fun cargarDatos() {
        // Limpiamos la lista de usuarios
        listaUsuarios.clear()

        // Obtengo los datos de la base de datos
        db.collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    Log.d("Añadiendo Usuarios", "${document.id} => ${document.data}")
                    val usuario = document.toObject(ItemsUsuarios::class.java)
                    listaUsuarios.add(usuario)
                    // Muestro el recyclerView
                    binding.mostrarTodos.layoutManager = LinearLayoutManager(requireContext())
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


    }
}