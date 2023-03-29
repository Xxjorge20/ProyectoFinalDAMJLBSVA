package ies.luiscarrillodesotomayor.gestionincidencias.Datos

import android.graphics.Bitmap
import android.net.Uri

data class DatosIncidencias (
   var nombre: String = "",
   var fecha: String = "",
   var descripcion: String = "",
   var acabada: Boolean = false,
   var prioridad: String = "",
   var ID: String ="",
   var foto: String = ""
)


