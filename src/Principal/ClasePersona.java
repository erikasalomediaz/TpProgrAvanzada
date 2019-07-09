package Principal;

import Anotaciones.Columna;
import Anotaciones.Id;
import Anotaciones.Tabla;

@Tabla(nomTab = "Persona")
public class ClasePersona {

	@Id
	//@Columna(nomCol = "Id")
	public Long Id;
	
	@Columna(nomCol = "Nombre")
	public String Nombre;
	@Columna(nomCol = "Apellido")
	public String Apellido;
	@Columna(nomCol = "Dni")
	public Integer Dni;
	
	
	public long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public String getApellido() {
		return Apellido;
	}
	public void setApellido(String apellido) {
		Apellido = apellido;
	}

	public int getDni() {
		return Dni;
	}
	public void setDni(int dni) {
		Dni = dni;
	}
	
	public String toString() {
		return "Persona [Id=" + Id + ", Nombre=" + Nombre + ", Apellido=" + Apellido + ", Dni=" + Dni + "]";
	}
	
	
}
