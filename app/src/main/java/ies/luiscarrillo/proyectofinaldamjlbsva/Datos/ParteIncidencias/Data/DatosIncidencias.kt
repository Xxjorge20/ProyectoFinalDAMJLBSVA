package ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data

import android.graphics.Bitmap
import android.net.Uri

data class DatosIncidencias (
   var nombre: String = "",
   var fecha: String = "",
   var descripcion: String = "",
   var acabada: Boolean = false,
   var prioridad: String = "",
   var tipo: String = "",
   var ID: String ="",
   var foto: String = ""
)


