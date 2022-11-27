import java.io.Serializable
import kotlin.random.Random

class Alumno(var nombre : String?, var nota: Int = -1 ): Serializable{
    override fun toString(): String {
        return "\n Alumno(nombre=$nombre, nota=$nota)"
    }
}