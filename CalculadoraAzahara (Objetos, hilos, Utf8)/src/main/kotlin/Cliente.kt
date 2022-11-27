import java.io.DataInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.Socket


fun main(args: Array<String>) {
   var direccion: InetAddress    //local hosts en este caso, sino seria la ip a conectar
   var servidor: Socket        //a crear con la dirección y el puerto
   var puerto : Int = 5000;		//puerto de el shoker para conectar


    //Paso uno -> obtener la dirección
    direccion = InetAddress.getLocalHost();        // si es la dirección local dirección local

    //Paso dos -> obtener es servidor
    servidor =  Socket( direccion, puerto );

    // paso tres -> pasos de mensajes de lectura y escritura

    println("Bienvenido a la calculadora en remoto!" +
            "\n, a continuacion escrive los datos de la operacion que quieres realizar.")
    //mandar paquete
    println("dime un numero para hacer la operacion")
    var numeroUno = readLine()?.toIntOrNull()
    println("dime que operacion quieres hacer / * + -")
    var operacion = readLine()?.trim()
    println("dime otro numero para hacer la operacion")
    var numeroDos = readLine()?.toIntOrNull()


    try {
        //crear
        require(numeroUno!=null && numeroDos!=null && operacion!=null)     // casac si son nulos
        var paquete  = Paquete(operacion, numeroUno, numeroDos)

        println("Gracias, enviamos tu peticion en remoto...")
        //enviar
        val bufferObjetosSalida = ObjectOutputStream(servidor.getOutputStream())
        bufferObjetosSalida.writeObject(paquete)

        println("paquete enviado")
    }catch (e:Exception){
        println("no has metifdo los datos correctos")

    }


    //leer soring

    val datos = DataInputStream(servidor.getInputStream())
    println("has recibido la siguiente respuesta")
    println(datos.readUTF())

}