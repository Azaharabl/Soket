import java.io.Serializable

class Paquete(var operacion : String, var numeroUno : Int, var numerodos : Int) : Serializable {
    override fun toString(): String {
        return "Paquete(operacion='$operacion', numeroUno=$numeroUno, numerodos=$numerodos)"
    }
}