package ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu

import InicioSesion.MainActivity
import InicioSesion.RegistroFragment
import MenuConjunto.AcercaDe
import MenuConjunto.Configuracion
import ParteUsuarios.InicioFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat.START
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.MenulateralBinding
import ies.luiscarrillodesotomayor.gestionincidencias.Menu.CasaFragment
import ies.luiscarrillodesotomayor.gestionincidencias.Menu.InsertarIncidenciaFragment


class MenuLateral : AppCompatActivity() {

    private lateinit var binding: MenulateralBinding
    private lateinit var toggle: ActionBarDrawerToggle



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MenulateralBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.open_drawer, R.string.close_drawer)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // cargo el header del menu lateral
        val header = binding.navView.getHeaderView(0)
        val email = header.findViewById<TextView>(R.id.TBCorreoUserName)
        val nombre = header.findViewById<TextView>(R.id.TBUserName)



        obtenerUsername { username ->
            // Hacer algo con el valor de username aquí
            Log.d("Usuario", "Username obtenido: $username")

            if (username != null) {
                nombre.text = username
            }
            else
            {
                nombre.text = "Usuario"
            }

        }

        email.text = obtenerCorreo()
        permisos()


        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, CasaFragment()).commit()
                    binding.drawer.closeDrawers()
                }
                /*
                R.id.nav_modificarIncidencia -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, ModificarIncidencia()).commit()
                    binding.drawer.closeDrawers()
                }
                */

                R.id.nav_insertarIncidencia -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, InsertarIncidenciaFragment()).commit()
                    binding.drawer.closeDrawers()
                }
                R.id.nav_salir -> {
                    cerrarSesion()
                    // vuelvo a la actividad de login
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    // Cierro la actividad
                    finish()
                }

                R.id.nav_insertarUsuario -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, RegistroFragment()).commit()
                    binding.drawer.closeDrawers()
                }

                R.id.nav_verUsuarios -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, InicioFragment()).commit()
                    binding.drawer.closeDrawers()
                }

                R.id.nav_configuracion -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, Configuracion()).commit()
                    binding.drawer.closeDrawers()
                }

                R.id.nav_acercaDe -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, AcercaDe()).commit()
                    binding.drawer.closeDrawers()
                }

            }

            binding.drawer.closeDrawer(START)
            true
        }
        }

/**
 * Esta función maneja la selección de elementos del menú y alterna el menú si es necesario.
 * 
 * @param item `item` es un parámetro de tipo `MenuItem` que representa el elemento de menú que
 * seleccionó el usuario. El método `onOptionsItemSelected` se llama cuando se selecciona un elemento
 * del menú, y este parámetro le permite acceder a información sobre el elemento seleccionado, como su
 * ID, título y otros
 * @return Si el objeto `toggle` maneja el `MenuItem` seleccionado, se devuelve `true`. De lo
 * contrario, se devuelve el valor de retorno del método `super`.
 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Funcion para cerrar Sesión de firebase
    fun cerrarSesion() {
        // Cierro la sesión de firebase
        FirebaseAuth.getInstance().signOut()
        Log.d("CierreSesion", "Sesión cerrada")
    }

   /**
    * La función verifica los privilegios del usuario y oculta ciertos elementos del menú según su
    * nivel de acceso.
    */
    fun permisos(){

        val auth = FirebaseAuth.getInstance()
        val correo = auth.currentUser?.email.toString()
        var db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(correo).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val privilegios = documentSnapshot.getString("Privilegios")


                    if (privilegios == "user") {

                        // Menu
                        binding.navView.menu.findItem(R.id.nav_verUsuarios).isVisible = false
                        binding.navView.menu.findItem(R.id.nav_insertarUsuario).isVisible = false

                    }
                    if (privilegios == "gestor") {
                        binding.navView.menu.findItem(R.id.nav_verUsuarios).isVisible = false
                        binding.navView.menu.findItem(R.id.nav_insertarUsuario).isVisible = false
                    }
                }
                Log.d("Usuario", "Datos Usuario: ${documentSnapshot.data}")
            }
    }


    fun obtenerUsername(callback: (String) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val correo = auth.currentUser?.email.toString()
        val db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(correo).get()
            .addOnSuccessListener { documentSnapshot ->
                var username = ""
                if (documentSnapshot.exists()) {
                    val nombre = documentSnapshot.getString("Nombre")
                    val apellido = documentSnapshot.getString("Apellidos")
                    username = "$nombre $apellido"
                    Log.d("Usuario", "Datos Usuario: $username")
                }
                Log.d("Usuario", "Datos Usuario: ${documentSnapshot.data}")
                callback(username) // Llamar a la devolución de llamada con el valor de username
            }
    }


    fun obtenerCorreo() : String {
        var correo = ""
        val auth = FirebaseAuth.getInstance()
        correo = auth.currentUser?.email.toString()
        return correo
    }


}