import java.io.DataOutputStream
import java.io.ObjectInputStream
import java.io.PrintStream
import java.net.Socket


class Hilo : Thread {
   var numeroCliente : Int? = null
    var cliente : Socket? = null


 constructor(n : Int , s : Socket){
        this.numeroCliente = n
        this.cliente = s
 }
    override fun run() {

        //leer el paquete del cliente object
        val entrada = ObjectInputStream(cliente?.getInputStream())
        val ob: Paquete = entrada.readObject() as Paquete

        val operacion = ob.operacion
        val nUno = ob.numeroUno
        val nDos = ob.numerodos

        //escribe string
        val respuesta: String
        if (operacion == "*") {
            respuesta = (nUno * nDos).toString()

        } else if (operacion == "+") {
            respuesta = (nUno + nDos).toString()

        } else if (operacion == "-") {
            respuesta = (nUno - nDos).toString()

        } else if (nUno != 0 && nDos != 0 && operacion == "/") {
            respuesta = (nUno / nDos).toString()

        } else if (operacion == "/") {
            respuesta = "las operaciones de dividir no pueden tener 0"
        }else{respuesta = "la operacion pedida no es correcta, las operaciones son *, /, +; -"}


        var  ps =  DataOutputStream(cliente?.getOutputStream())
        ps.writeUTF("Usted es mi cliente: "+numeroCliente +" y Su operacion $nUno $operacion $nDos = $respuesta");

        println("Su operacion $nUno $operacion $nDos = $respuesta")

        cliente?.close()

    }

}