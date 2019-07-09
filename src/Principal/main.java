package Principal;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import Servicios.Consultas;
import Servicios.Consultas.*;
import Utilities.Conexion;

public class main {

	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, SQLException, NoSuchFieldException, SecurityException, InstantiationException, InvocationTargetException {
		// TODO Auto-generated method stub
	ClasePersona p = new ClasePersona();
	Class c = p.getClass();
	p.setId((long)29);
	p.setApellido("Farro");
	p.setNombre("pedro");
	p.setDni(24546981);
	//Consultas.guardar(p);
	//Consultas.modificar(p);
	Consultas.eliminar(p);
	//Consultas.obtenerPorId(c,  18 );
   // Object o = Consultas.guardar2(p);
					
	}

}
