import java.io.DataOutputStream
import java.io.ObjectInputStream
import java.net.Socket
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors

class Hilo : Thread {

    var cliente : Socket
    var numeroCliente : AtomicInteger
    var lista : ListaAlumnosMonitor
    var resultado = "Tu consula no se hizo adecuadamente"

    constructor(cliente: Socket, numeroCliente: AtomicInteger, lista: ListaAlumnosMonitor) {
        this.cliente = cliente
        this.numeroCliente = numeroCliente
        this.lista = lista
    }

    override fun run() {
        println("Hilo creado y lanzado: ")
        println("Hilo: Esperando paquete...")
        //leer paqete
        var ob = ObjectInputStream(cliente.getInputStream())
        var p = ob.readObject() as Peticion
        println("Hilo : paquete llegado.")

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
        println("Hilo: El resultado es : " + resultado)

        val ps = DataOutputStream(cliente.getOutputStream())
        ps.writeUTF("Hilo: Esta es mi peticion número: " + numeroCliente + "\n" + resultado);

        //fin de cliente
        println("Hilo : ya he terminado con la peticion $numeroCliente")
        println("Hilo: La bbdd esta : " + lista.leer().toString())


    }

    fun peticionConsultarEstadisticas(l: ListaAlumnosMonitor): String {
        println("Entrado en Estadisticas")
        //(NºAprobados, NºSuspensos, Nota Media).
        //primero quitamos todas las notas que no exixtan que son las que tienen un -1

        //protegido
        var lista = l.leer()

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

    fun peticionConsultarPorOrdenNota(l: ListaAlumnosMonitor): String {
        println("Entrado en consultar nota")

        //protegido
        var lista = l.leer()

        var listaOrdenada = lista.stream().sorted { o1, o2 -> o1.nota-o2.nota }.collect(Collectors.toList())
        return "tu lista de alumnos ordenados por nota es : \n" + listaOrdenada.toString()

    }

    fun peticionConsultarPorOrdenAlfabetico(l: ListaAlumnosMonitor): String {

        println("Entrado en consultar alfabeticamente")

        //protegido
        var lista = l.leer()

        lista.sortBy { a -> a.nombre }
        var listaStream = lista.stream().map{ a -> a.toString() }.collect(Collectors.toList())
        return "tu lista de alumnos ordenados por nombre es : \n" + listaStream
    }

    fun peticionBorrar(lista: ListaAlumnosMonitor, p: Peticion): String {
        println("Entrado en borrar")
        var resultado = ""
        try {
            require(p.alumno.nombre!=null)
            //protegido
            resultado = lista.borrar(p.alumno.nombre!!)

        }catch (e: Exception){
            println(e.message)
        }
        return resultado
    }

    fun  peticionCrear(lista: ListaAlumnosMonitor, p: Peticion): String{
        println("Entrado en crear")
        //protegido
       return lista.añadir(p)

    }

    fun peticionActualizar(lista: ListaAlumnosMonitor, p: Peticion):String {
        println("Entrado en actualizar")
        //protegido
        return  lista.modificar(p)

    }

}