package ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.CuadroDialogo

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment


class CuadroDialogo: DialogFragment() {
/*
    fun DialogoModificarBorrar(datosIncidencias: DatosIncidencias,holder: ViewHolder): Boolean {
        var respuesta = false
        val builder = AlertDialog.Builder(holder.itemView.context)
        builder.setTitle("¿Que Desea Hacer?")
        builder.setMessage(datosIncidencias.ID + " - " + datosIncidencias.nombre)

        builder.setPositiveButton("Modificar") { dialog, which ->
            // Do something when user press the positive button

            // Modificar incidencia

            CargarModificarFragment(datosIncidencias, holder)

            respuesta = true
            return@setPositiveButton
        }

        builder.setNegativeButton("Borrar") { dialog, which ->
            // Do something when user press the negative button
            respuesta = false
            DialogoBorrarIncidencias(datosIncidencias, holder)
            return@setNegativeButton
        }

        // Create the dialog
        val dialog = builder.create()

        // Show the dialog
        dialog.show()

        return respuesta
    }
    // Funcion para mostrar un dialogo de confirmacion de borrado de la incidencia
    fun DialogoBorrarIncidencias(datosIncidencias: DatosIncidencias, holder: ViewHolder, incidencias: ArrayList<DatosIncidencias>): Boolean {
        var respuesta = false
        val builder = AlertDialog.Builder(holder.itemView.context)
        builder.setTitle("¿Desea borrar la incidencia?")
        builder.setMessage(datosIncidencias.ID + " - " + datosIncidencias.nombre)

        builder.setPositiveButton("Confirmar") { dialog, which ->
            // Do something when user press the positive button
            respuesta = true
            Incidencia(
                datosIncidencias.nombre,
                datosIncidencias.fecha,
                datosIncidencias.descripcion,
                datosIncidencias.acabada,
                datosIncidencias.foto,
                datosIncidencias.prioridad,
                datosIncidencias.ID
            ).BorrarIncidencia(datosIncidencias.ID)

            val position = incidencias.indexOf(datosIncidencias)
            incidencias.removeAt(position)
            /*
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, incidencias.size)
            notifyDataSetChanged()*/

            Log.d("Borrar Incidencia", "Incidencia borrada correctamente")


            return@setPositiveButton
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            // Do something when user press the negative button
            respuesta = false
            Log.d("Borrar Incidencia", "incidencia no borrada")
            return@setNegativeButton
        }

        // Create the dialog
        val dialog = builder.create()

        // Show the dialog
        dialog.show()

        return respuesta
    }

    /**
     * Esta función carga un fragmento para modificar una incidencia con los datos dados.
     * 
     * @param datosIncidencias Es un objeto de tipo DatosIncidencias que contiene información sobre una
     * incidencia, como su ID, nombre, fecha, descripción, prioridad y si se ha completado o no.
     * @param holder ViewHolder es una clase que contiene las vistas de un solo elemento en un
     * RecyclerView. Se utiliza para mostrar de manera eficiente una gran cantidad de elementos en una
     * lista o cuadrícula. En este código, el ViewHolder se usa para obtener el contexto de la vista
     * del elemento, que luego se usa para obtener el FragmentManager para
     */
    fun CargarModificarFragment(datosIncidencias: DatosIncidencias, holder: ViewHolder) {

        // Llevar al de modificar incidencia
        val fragment = ModificarIncidencia()
        val args  = Bundle()
        args.putString("ID", datosIncidencias.ID)
        args.putString("nombre", datosIncidencias.nombre)
        args.putString("fecha", datosIncidencias.fecha)
        args.putString("descripcion", datosIncidencias.descripcion)
        args.putString("prioridad", datosIncidencias.prioridad)
        args.putBoolean("acabada", datosIncidencias.acabada)

        fragment.arguments = args

        val fragmentManager: FragmentManager = (holder.itemView.context as MainActivity).supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragment_container_view, fragment).commit()



    }

*/
}


