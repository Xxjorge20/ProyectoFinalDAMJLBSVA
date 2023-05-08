package ies.luiscarrillodesotomayor.gestionincidencias.Menu

import InicioSesion.MainActivity
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.type.DateTime
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data.Incidencia
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Menu.MenuLateral
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.FragmentInsertarIncidenciaBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*


class InsertarIncidenciaFragment : Fragment(R.layout.fragment_insertar_incidencia) {

    private var _binding: FragmentInsertarIncidenciaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var data : ByteArray
    lateinit var imagenVarible : Bitmap
    private val galeria = 1
    private val camara = 2
    private val INCIDENCIAS_IMAGENES = "/incidencias"
    private lateinit var rutaFoto : String
    private lateinit var urlDescargarFoto : String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        urlDescargarFoto = ""
        data = ByteArray(0)
        imagenVarible  = Bitmap.createBitmap(100,100, Bitmap.Config.ARGB_8888)
        var incidencia = Incidencia("","","",false,"Baja", urlDescargarFoto)
        _binding = FragmentInsertarIncidenciaBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.TBFechaIncidencia.setText(obtenerFechaHoraActual())
        binding.TBFechaIncidencia.isEnabled = false

        activity?.setTitle("Insertar Incidencias")

        // cargo la lista de prioridades
        val listaPrioridades = Incidencia.Prioridad.values()
        val listaPrioridadesString = ArrayList<String>()
        for (prioridad in listaPrioridades) {
            listaPrioridadesString.add(prioridad.toString())
        }
        binding.listPrioridad.adapter = android.widget.ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            // recorro el array de prioridades y la añado a la lista
            listaPrioridadesString

        )

        // evento click en la lista de prioridades
        binding.listPrioridad.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(requireContext(), "Prioridad seleccionada: ${listaPrioridades[position]}", Toast.LENGTH_SHORT).show()
            Log.d("Prioridad", listaPrioridades[position].toString())

            // guardo la prioridad seleccionada
            if (listaPrioridades[position] == Incidencia.Prioridad.ALTA) {
                incidencia.prioridad = Incidencia.Prioridad.ALTA.toString()
            } else if (listaPrioridades[position] == Incidencia.Prioridad.MEDIA) {
                incidencia.prioridad = Incidencia.Prioridad.MEDIA.toString()
            } else if (listaPrioridades[position] == Incidencia.Prioridad.BAJA) {
                incidencia.prioridad = Incidencia.Prioridad.BAJA.toString()
            }

            ocultarTeclado()
        }



        // Boton para echar una foto
        binding.BCamaraGaleria.setOnClickListener {
            cuadroCamaraGaleria()
        }



        // Boton de insertar incidencia
        binding.BInsertarIncidencia.setOnClickListener(){
            var usuario = FirebaseAuth.getInstance().currentUser
            var correo = usuario?.email.toString()
            var nameUser = correo.split("@")[0]
            // Comprobamos que los campos no estén vacíos
            if (binding.TBTituloIncidencia.text.toString() != "" && binding.TBDescripcionIncidencia.text.toString() != "") {

                insertarIncidencia(nameUser, incidencia, correo)

            }
            else
            {
                // Mostramos un mensaje de error
                Toast.makeText(requireActivity().applicationContext, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
                Log.e("InsertarIncidencia", "Rellene todos los campos")
                ocultarTeclado()
            }
        }
        return view
    }

    private fun insertarIncidencia(
        nameUser: String,
        incidencia: Incidencia,
        correo: String
    ) {
        // Obtener la fecha actual del sistema y guardarla en la incidencia
        //val fechaActual = Calendar.getInstance().time

        // Creamos la incidencia
        val nombre = binding.TBTituloIncidencia.text.toString()
        val fecha = binding.TBFechaIncidencia.text.toString()
        val descripcion =
            binding.TBDescripcionIncidencia.text.toString() + " \n Comentario hecho por: " + nameUser
        val acabada = false
        // val foto = subirFotoFirebase(imagenVarible)
        incidencia.nombre = nombre
        incidencia.fecha = fecha
        incidencia.descripcion = descripcion
        incidencia.acabada = acabada
        incidencia.foto = urlDescargarFoto


        // Subir la foto a Firebase Storage ---> Pasar a funcion
        val auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {
            val storage = FirebaseStorage.getInstance()
            // Ruta donde guardare la imagen en la base de datos
            val storageReference = storage.getReference("FotosIncidencia/")
            // Nombre de la imagen que guardare en la base de datos
            val imageRef = storageReference.child(incidencia.nombre + ".jpg")
            val baos = ByteArrayOutputStream()
            imagenVarible.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val uploadTask = imageRef.putBytes(data)
            uploadTask.addOnFailureListener {
                Toast.makeText(
                    requireActivity().applicationContext,
                    "Error al subir la imagen",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnSuccessListener { taskSnapshot ->

                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    incidencia.foto = it.toString()
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Imagen subida exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i("UrlDescargarFoto", it.toString())
                }.addOnFailureListener {
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Error al obtener la URL de descarga",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        val contador = 0
        // cuando pasen 1 segundos, se inserta la incidencia para que de tiempo a subir la foto a firebase
        Handler(Looper.getMainLooper()).postDelayed({
            if (contador == 0) {
                // Insertamos la incidencia en la base de datos
                if (!incidencia.InsertarIncidencia(incidencia)) {

                    binding.TBTituloIncidencia.setText("")
                    binding.TBFechaIncidencia.setText("")
                    binding.TBDescripcionIncidencia.setText("")
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Incidencia insertada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i("InsertarIncidencia", "Incidencia insertada correctamente")


                    val CasaFragment = Intent(activity, MenuLateral::class.java)
                    CasaFragment.putExtra("foto", imagenVarible)
                    CasaFragment.putExtra("correo", correo)
                    CasaFragment.putExtra("nombre", nameUser)
                    startActivity(CasaFragment)

                } else {
                    // Si da error al insertar la incidencia, mostramos un mensaje de error
                    val CasaFragment = Intent(activity, MenuLateral::class.java)
                    startActivity(CasaFragment)
                    CasaFragment.putExtra("correo", correo)
                    CasaFragment.putExtra("nombre", nameUser)
                    CasaFragment.putExtra("foto", imagenVarible)
                    // Mostramos un mensaje de error
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Error al insertar la incidencia",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("InsertarIncidencia", "Error al insertar la incidencia")
                    ocultarTeclado()
                }
            }
        }, 1000)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





    // Ocultar el teclado
    private fun ocultarTeclado() {
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(requireActivity())
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    private fun cuadroCamaraGaleria() {
        val dialogoGC = AlertDialog.Builder(requireContext())
        dialogoGC.setTitle("¿Que Deseas Hacer?")
        val pictureDialogItems = arrayOf("Seleccionar foto de la galeria", "Tomar foto con la camara")
        dialogoGC.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> fotoGaleria()
                1 -> fotoCamara()
            }
        }
        dialogoGC.show()
    }

    private fun fotoGaleria() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, galeria)
    }

    private fun fotoCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, camara)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == galeria) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        val fotoGaleria = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, contentURI)
                        rutaFoto = guardarImagen(fotoGaleria)
                        Log.i("ImagenGuardada","Imagen guardada en el dispositivo")
                        imagenVarible = fotoGaleria
                        binding.imageVista.setImageBitmap(fotoGaleria)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Log.e("Error", "Error al guardar la imagen")
                    }
                }
            } else if (requestCode == camara) {
                val fotoCamara = data!!.extras!!.get("data") as Bitmap
                imagenVarible = fotoCamara
                binding.imageVista.setImageBitmap(fotoCamara)
                rutaFoto = guardarImagen(fotoCamara)
                Log.i("ImagenGuardada","Imagen guardada en el dispositivo")
                imagenVarible = fotoCamara
            }
        }
    }

    // Funcion para guardar la imagen
    private fun guardarImagen(fotoEscogida: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        fotoEscogida.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + INCIDENCIAS_IMAGENES
        )

        Log.d("Directorio", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        try {
            val fotoArchivo = File(
                wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg")
            )
            fotoArchivo.createNewFile()
            val fo = FileOutputStream(fotoArchivo)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                requireActivity().applicationContext,
                arrayOf(fotoArchivo.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("Archivo Guardado", "Ruta de la foto::--->" + fotoArchivo.absolutePath)

            return fotoArchivo.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }


    // Funcion para obtener la fecha actual
    fun obtenerFechaHoraActual(): String {
        val timeZone = TimeZone.getTimeZone("Europe/Madrid")
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        dateFormat.timeZone = timeZone
        val fechaHoraActual = Date()
        return dateFormat.format(fechaHoraActual)
    }

    fun formatearFecha(fecha: String): String {
        val formatoActual = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val fechaActual = formatoActual.parse(fecha)

        val formatoNuevo = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "ES"))
        return formatoNuevo.format(fechaActual)
    }

}