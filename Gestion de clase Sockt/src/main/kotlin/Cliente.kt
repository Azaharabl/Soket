import java.io.DataInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.Socket


fun main(args: Array<String>) {

    var hostServidor = InetAddress.getByName("localhost")
    var puertoServidor = 5000
    var socket : Socket

    //datos necesarios para peticion
    var opcion : Int? = null
    var nombre : String? = null
    var nota: Int? = null
    var nombreAntiguo : String? =null


    while(true) {
        //conexion
         socket = Socket(hostServidor,puertoServidor)

        println("Hola Bienvenido al sistema de Gestion Alumnos.")
        do {
            println("Elige que tipo de Petición va a hacer selecionando el numero.")
            println(
                "1. crear\n" +
                        "2. borrar\n" +
                        "3. actualizar \n" +
                        "4. consultar por orden alfabetico\n" +
                        "5. consultar por nota\n" +
                        "6. consultar estadisticas (NºAprobados, NºSuspensos, Nota Media)\n" +
                        "7. salir"
            )


            opcion = readln().trim().toIntOrNull()
            println("HAS ELEGIDO LA OPCION $opcion")
        } while (opcion == null)


        if (opcion != null) {
            if (opcion==7){
                println("Gracias por usar Gestion de alumnos. Fin de programa")
                System.exit(0)
            }
            if (opcion.equals(1)) {
                do {
                    println("Dime el Nombre del alumno")
                    nombre = readln().trim()

                } while (nombre == null)
                do {
                    println("Dime la nota")
                    nota = readln().trim().toIntOrNull()

                } while (nota == null)
            }

            if (opcion.equals(2)) {
                do {
                    println("Dime el Nombre del alumno a borrar")
                    nombre = readln().trim()

                } while (nombre == null)

            }
            if (opcion.equals(3)) {
                do {
                    println("Dime el Nombre nuevo del alumno")
                    nombre = readln().trim()

                } while (nombre == null)
                do {
                    println("Dime la nota nueva")
                    nota = readln().trim().toIntOrNull()

                } while (nota == null)
                do {
                    println("Dime original del alumno a modificar")
                    nombreAntiguo = readln().trim()

                } while (nombreAntiguo == null)
            }
        }
        //si la opcion es de las 3 ultimas creamos un alumno vacion para que no de error
        // nos aseguramos de qe nota no sea nulo
        if (nota == null) nota = -1
        if (nombre == null) nombre = ""

        //creamos el dato de salida
        require(nombre != null)

        var p = Peticion(opcion, Alumno(nombre, nota), nombreAntiguo)

        println("mandada la peticion")
        //creamos la salida y enviamos
        var bufferObjetosSalida = ObjectOutputStream(socket.getOutputStream())
        bufferObjetosSalida.writeObject(p)


        println("recibiendo...")
        //creamos la lectura para cuando nos llegue
        var bufferLectura = DataInputStream(socket.getInputStream())
        var s = bufferLectura.readUTF()
        println("el resultado de tu peticion es :")
        println(s)

    }



}