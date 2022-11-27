import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.stream.Collectors

class ListaAlumnosMonitor {

    var lista = ArrayList<Alumno>()
    var lock = ReentrantReadWriteLock()
    var cerrojoLectura = lock.readLock()
    var cerrojoEscritura = lock.writeLock()

    fun borrar(s : String): String{
        cerrojoEscritura.lock()
        //ver que no exixte por nombre
        var isAlumno = lista.stream().filter{x -> x.nombre.equals(s, true)}.collect(Collectors.toList())
        if (isAlumno.count()==0) {
            cerrojoEscritura.unlock()
            return "tu alumno NO sido borrado correctamente por que NO está su nombre en la bbdd"
        }else{
            lista.remove(isAlumno.get(0))
            cerrojoEscritura.unlock()
            return "tu alumno ha sido Borrado correctamente"
        }
        cerrojoEscritura.unlock()

    }

    fun modificar(p : Peticion): String{
        cerrojoEscritura.lock()
        var isAlumno = lista.stream().filter{x -> x.nombre.equals(p.nombreAntiguo, true)}
            .collect(Collectors.toList())
        println(isAlumno)
        if (isAlumno.count()==0) {
            cerrojoEscritura.unlock()
            return "tu alumno NO sido Actualizado correctamente por no está su nombre ne la bbdd"

        }else{
            var a : Alumno = isAlumno.get(0)
            lista.remove(a)
            lista.add(p.alumno)
            cerrojoEscritura.unlock()
            return "tu alumno ha sido Actualizado correctamente"
        }

    }

    fun añadir(p : Peticion): String{
        cerrojoEscritura.lock()

        //ver que no exixte por nombre
        var isAlumno = lista.stream().filter{x -> x.nombre.equals(p.alumno.nombre, true)}
        if (isAlumno.count()==0L) {
            lista.add(p.alumno)
            cerrojoEscritura.unlock()
            return "tu alumno ha sido introducido correctamente"
            //si no exixte guardar y dar mensaje de ok
        }else{
            cerrojoEscritura.unlock()
            return "tu alumno NO sido introducido correctamente por que ya está su nombre ne la bbdd"
            //si exixte mandar mensaje de erro
        }
        cerrojoEscritura.unlock()
    }

    fun leer(): ArrayList<Alumno> {
        cerrojoLectura.lock()
        var listaAMandar = lista.clone() as ArrayList<Alumno>
        cerrojoLectura.unlock()
        return listaAMandar
    }


}