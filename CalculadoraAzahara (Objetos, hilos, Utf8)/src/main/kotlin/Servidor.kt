import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.atomic.AtomicInteger

fun main(args: Array<String>) {
    println("Hello World soy Un Sevidor")


    val puerto : Int = 5000;
    var servidor : ServerSocket?
    var cliente : Socket?
    var numeroCliente : AtomicInteger =AtomicInteger(0);



    try {
        servidor= ServerSocket(puerto)
        do {
            cliente = servidor.accept()
            numeroCliente.addAndGet(1)

            println("Me enetra el Cliente: $numeroCliente y lo paso a un hilo")

            var  hilo = Hilo(numeroCliente.toInt(), cliente)
            hilo.start()

        }while (true)

    }catch (e: Exception){
        e.printStackTrace()
    }
}
