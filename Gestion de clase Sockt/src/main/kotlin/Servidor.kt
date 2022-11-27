
import java.io.DataOutputStream
import java.io.ObjectInputStream
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicInteger
import java.util.logging.Logger
import java.util.stream.Collectors


fun main(args: Array<String>) {
    println("Hello World soy Un Srevidor De Gestion de clinetes")

    //lo que necesita el server
    var puerto = 5000;
    var soket = ServerSocket(puerto)
    var cliente: Socket
    var numeroCliente = AtomicInteger(0)

    var resultado = "Tu consula no se hizo adecuadamente"
    var lista = ArrayList<Alumno>()

    while (true) {
        try {

            println("Esperamos conexion de cliente...")
            //leer la entrada
            cliente = soket.accept()
            println("Recobida conexion de cliente")
            numeroCliente.addAndGet(1)

            //pasar a un hilo
            println("Esperando paquete...")
            //leer paqete
            var ob = ObjectInputStream(cliente.getInputStream())
            var p = ob.readObject() as Peticion
            println("paquete llegado.")

            //procesar
            var peticion = p.tipoPeticion
            if (peticion.equals(1)) resultado = peticionCrear(lista, p)
            if (peticion.equals(2)) resultado = peticionBorrar(lista, p)
            if (peticion.equals(3)) resultado = peticionActualizar(lista, p)
            if (peticion.equals(4)) resultado = peticionConsultarPorOrdenAlfabetico(lista)
            if (peticion.equals(5)) resultado = peticionConsultarPorOrdenNota(lista)
            if (peticion.equals(6)) resultado = peticionConsultarEstadisticas(lista)


            //enviar resultado como una string
            //escribir(como soker formato utf)
            println("el resultado es : " + resultado)

            val ps = DataOutputStream(cliente.getOutputStream())
            ps.writeUTF("Esta es mi peticion número: " + numeroCliente + "\n" + resultado);

            //fin de cliente
            println("ya he terminado con la peticion $numeroCliente")
            println("La bbdd esta : " + lista.toString())
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}

fun peticionConsultarEstadisticas(lista: ArrayList<Alumno>): String {
    println("Entrado en Estadisticas")
    //(NºAprobados, NºSuspensos, Nota Media).
    //primero quitamos todas las notas que no exixtan que son las que tienen un -1
    var listaOK = lista.filter { x-> x.nota!= -1 }
    var aprobados = listaOK.filter { x -> x.nota>=5 }.count()
    var suspensos = listaOK.filter { x -> x.nota<5}.count()
    var notaSuma : Int? = 0
    var count = listaOK.count()
    listaOK.forEach{ x -> notaSuma = notaSuma?.plus((x.nota)) }
    var notaMedia = notaSuma?.div(count)

    return "!Has pedido las estadisticas de los alumnos¡ \n " +
            "El numero de alumnos es : $count .\n" +
            "El número de aprobados : $aprobados .\n"+
            "El número de suspensos : $suspensos.\n " +
            "La nota media es : $notaMedia . "



}

fun peticionConsultarPorOrdenNota(lista: ArrayList<Alumno>): String {
    println("Entrado en consultar nota")
    var listaOrdenada = lista.stream().sorted { o1, o2 -> o1.nota-o2.nota }.collect(Collectors.toList())
    return "tu lista de alumnos ordenados por nota es : \n" + listaOrdenada.toString()

}

fun peticionConsultarPorOrdenAlfabetico(lista: ArrayList<Alumno>): String {

    println("Entrado en consultar alfabeticamente")
    lista.sortBy { a -> a.nombre }
    var listaStream = lista.stream().map{ a -> a.toString() }.collect(Collectors.toList())
      return "tu lista de alumnos ordenados por nombre es : \n" + listaStream
}

fun peticionBorrar(lista: ArrayList<Alumno>, p: Peticion): String {
    println("Entrado en borrar")
    //ver que no exixte por nombre
    var isAlumno = lista.stream().filter{x -> x.nombre.equals(p.alumno.nombre, true)}.collect(Collectors.toList())
    if (isAlumno.count()==0) {
        return "tu alumno NO sido borrado correctamente por que NO está su nombre en la bbdd"

    }else{
        lista.remove(isAlumno.get(0))
        return "tu alumno ha sido Borrado correctamente"
    }
}

fun  peticionCrear(lista: ArrayList<Alumno>, p: Peticion): String{
    println("Entrado en crear")
    //ver que no exixte por nombre
    var isAlumno = lista.stream().filter{x -> x.nombre.equals(p.alumno.nombre, true)}
    if (isAlumno.count()==0L) {
        lista.add(p.alumno)
        return "tu alumno ha sido introducido correctamente"
        //si no exixte guardar y dar mensaje de ok
    }else{
        return "tu alumno NO sido introducido correctamente por que ya está su nombre ne la bbdd"
        //si exixte mandar mensaje de erro
    }

}

fun peticionActualizar(lista: ArrayList<Alumno>, p: Peticion):String {
    println("Entrado en actualizar")

    var isAlumno = lista.stream().filter{x -> x.nombre.equals(p.nombreAntiguo, true)}
        .collect(Collectors.toList())
    println(isAlumno)
    if (isAlumno.count()==0) {
        return "tu alumno NO sido Actualizado correctamente por no está su nombre ne la bbdd"

    }else{
        var a : Alumno = isAlumno.get(0)
        lista.remove(a)
        lista.add(p.alumno)
        return "tu alumno ha sido Actualizado correctamente"
    }
}

