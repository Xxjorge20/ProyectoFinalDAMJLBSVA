package ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ies.luiscarrillo.proyectofinaldamjlbsva.R
import ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data.DatosIncidencias

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    // Declaro las variables que voy a usar en el ViewHolder
    val imagen = itemView.findViewById<ImageView>(R.id.imagen)
    val titulo = itemView.findViewById<TextView>(R.id.titulo)
    val descripcion = itemView.findViewById<TextView>(R.id.descripcion)
    val fecha = itemView.findViewById<TextView>(R.id.fecha)
    val estado = itemView.findViewById<TextView>(R.id.estado)
    val acabada = itemView.findViewById<TextView>(R.id.Acabada)



    fun bind(datosIncidencias: DatosIncidencias) {

        // Asigno los valores a las variables
        titulo.text = datosIncidencias.nombre
        descripcion.text = "Problema: \n" + datosIncidencias.descripcion
        fecha.text = "Fecha: " + datosIncidencias.fecha
        // tamaño de la foto de la incidencia para que se vea bien
        imagen.layoutParams.height = 800
        imagen.layoutParams.width = 800

        // Dependiendo de si esta acabada la incidencia o no lo mostrare de un color u otro
        if (datosIncidencias.acabada) {
            acabada.text = "Incidencia: Acabada"
            acabada.setTextColor(android.graphics.Color.GREEN)
        } else {
            acabada.text = "Incidencia: Pendiente"
            acabada.setTextColor(android.graphics.Color.RED)
        }

        // Dependiendo de la prioridad de la incidencia lo mostrare de un color u otro
        when (datosIncidencias.prioridad) {
            "ALTA" -> {
                estado.text = "Prioridad: Alta"
                estado.setTextColor(android.graphics.Color.RED)
            }
            "MEDIA" -> {
                estado.text = "Prioridad: Media"
                estado.setTextColor(android.graphics.Color.YELLOW)
            }
            "BAJA" -> {
                estado.text = "Prioridad: Baja"
                estado.setTextColor(android.graphics.Color.GREEN)
            }
        }

    }


}

