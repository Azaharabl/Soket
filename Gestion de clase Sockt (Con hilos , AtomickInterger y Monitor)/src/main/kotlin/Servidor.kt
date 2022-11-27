
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicInteger


fun main(args: Array<String>) {
    println("Hello World soy Un Srevidor De Gestion de clinetes")

    //lo que necesita el server
    var puerto = 5000;
    var soket = ServerSocket(puerto)
    var cliente: Socket
    var numeroCliente = AtomicInteger(0)

    //var lista = ArrayList<Alumno>() cambiamos por el monitor para proteger secioncritica
    var lista = ListaAlumnosMonitor()


    while (true) {
        try {

            println("Esperamos conexion de cliente...")
            cliente = soket.accept()

            println("Recobida conexion de cliente")
            numeroCliente.addAndGet(1)

            println("Pasamos a un hilo el cliente para delegar")
            var hilo = Hilo(cliente, numeroCliente, lista)
            hilo.start()

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}

