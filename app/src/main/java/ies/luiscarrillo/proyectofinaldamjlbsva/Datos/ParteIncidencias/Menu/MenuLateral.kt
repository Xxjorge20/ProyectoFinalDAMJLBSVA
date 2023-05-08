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
import androidx.core.view.GravityCompat.*
import androidx.core.view.isInvisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.MenulateralBinding
import ies.luiscarrillodesotomayor.gestionincidencias.Menu.*



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
        email.text = intent.getStringExtra("correo")
        nombre.text = intent.getStringExtra("nombre")
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

    fun permisos(){

        val auth = FirebaseAuth.getInstance()
        val correo = auth.currentUser?.email.toString()
        var db = FirebaseFirestore.getInstance()

        db.collection("usuarios").document(correo).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val privilegios = documentSnapshot.getString("privilegios")


                    if (privilegios == "user") {

                        // Menu
                        binding.navView.menu.findItem(R.id.nav_verUsuarios).isVisible = false
                        binding.navView.menu.findItem(R.id.nav_insertarUsuario).isVisible = false

                    }
                    if (privilegios == "gestor") {
                        //binding.mostrarUsuario.isInvisible = true
                    }
                }
                Log.d("Usuario", "Datos Usuario: ${documentSnapshot.data}")
            }
    }



}