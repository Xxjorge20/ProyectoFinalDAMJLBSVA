package ies.luiscarrillo.proyectofinaldamjlbsva.Datos.ParteIncidencias.Data

/**
 * Esta es una clase de datos para almacenar información sobre incidentes, incluidos el nombre, la
 * fecha, la descripción, el estado de finalización, la prioridad, el tipo, la identificación y la
 * fotografía.
 * @property {String} nombre - Una propiedad de cadena que representa el nombre del incidente.
 * @property {String} fecha - fecha es una propiedad de tipo String que representa la fecha del
 * incidente.
 * @property {String} descripcion - "descripcion" es una propiedad de la clase de datos
 * "DatosIncidencias". Es una variable de cadena que almacena una descripción del incidente.
 * @property {Boolean} acabada - La propiedad "acabada" es una variable booleana que indica si la
 * incidencia se ha completado o no. Si el valor es verdadero, significa que la incidencia se ha
 * completado, y si el valor es falso, significa que la incidencia aún está pendiente.
 * @property {String} prioridad - La propiedad "prioridad" es un String que representa el nivel de
 * prioridad de un incidente. Podría ser "alta" (alto), "media" (medio) o "baja" (bajo), por ejemplo.
 * Esta propiedad se puede utilizar para ordenar y priorizar incidentes en función de su urgencia o
 * importancia.
 * @property {String} tipo - La propiedad "tipo" en la clase de datos "DatosIncidencias" representa el
 * tipo de incidente. Podría ser una categoría o una clasificación del incidente, como "problema
 * técnico", "violación de seguridad", "reclamo del cliente", etc.
 * @property {String} ID - La propiedad ID es un identificador único para cada instancia de la clase
 * DatosIncidencias. Se puede utilizar para distinguir entre diferentes objetos y recuperar objetos
 * específicos de una colección o base de datos.
 * @property {String} foto - La propiedad "foto" es una cadena que representa la ruta o URL de una foto
 * relacionada con el incidente. Podría usarse para almacenar una referencia a un archivo de imagen o
 * un enlace a una foto en línea.
 */
data class DatosIncidencias (
   var nombre: String = "",
   var descripcion: String = "",
   var fecha: String = "",
   var acabada: Boolean = false,
   var foto: String = "",
   var prioridad: String = "",
   var tipo: String = "",
   var lugar: String = "",
   var ID: String ="",

)


