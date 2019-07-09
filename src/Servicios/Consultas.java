package Servicios;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import Anotaciones.Columna;
import Anotaciones.Id;
import Anotaciones.Tabla;
import Utilities.Conexion;
import Utilities.Ubean;

public class Consultas {

	public static void guardar(Object o) throws IllegalArgumentException, IllegalAccessException {
		/*
		 * el cual debe guardar en la base de datos el objeto. Debe armarse la
		 * query por medio de reflexión utilizando las anotaciones creadas en el
		 * punto 2 y utilizando los métodos creados en UBean.
		 */
		Class c = o.getClass();
		Field[] atrib = c.getDeclaredFields();
		String campos = "";
		String tab = "";
		String valores = "";

		// obtengo campos de la tabla y valores del objeto
		for (Field f : atrib) {
			if (f.getAnnotation(Columna.class) != null) {
				// if (f.getAnnotation(Id.class) == null) {
				campos = campos.concat(f.getAnnotation(Columna.class).nomCol() + ",");
				Ubean.ejecutarGet(c, f.getAnnotation(Columna.class).nomCol());
				if (f.getType().equals(String.class))
					valores = valores.concat("'" + f.get(o) + "'" + ",");
				else
					valores = valores.concat(f.get(o) + ",");

			}
		}
		campos = campos.substring(0, campos.length() - 1);
		valores = valores.substring(0, valores.length() - 1);

		// obtengo nombre de tabla
		tab = ((Tabla) c.getAnnotation(Tabla.class)).nomTab(); 

		// realizo la conexion

		Connection cn = Conexion.getInstance();
		try {
			String Insertar = "";
			Insertar = Insertar.concat("Insert into " + tab + " (" + campos + ") values (" + valores + ")");
			PreparedStatement st = cn.prepareStatement(Insertar);
			st.execute();

			cn.close(); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public static void modificar(Object o) throws IllegalArgumentException, IllegalAccessException, SQLException {
		/*
		 * el cual debe modificar todas las columnas, excepto la columna Id, la
		 * cual se va a utilizar para la restricción(where). Debe armarse la
		 * query por medio de reflexión utilizando las anotaciones creadas en el
		 * punto 2 y utilizando los métodos creados en UBean.
		 */

		Class c = o.getClass();
		Field[] atrib = c.getDeclaredFields();

		String tab = "";
		String set = "Set ";
		String idAux = "";
		String campoId  = "";

		tab = ((Tabla) c.getAnnotation(Tabla.class)).nomTab();

		for (Field f : atrib) {

			if (f.getAnnotation(Id.class) != null){
				idAux = f.get(o).toString();
				campoId  = f.getName();
			}
			
			
			if (f.getAnnotation(Columna.class) != null) {
				// if (f.getAnnotation(Id.class) == null) {
				set = set.concat(f.getAnnotation(Columna.class).nomCol() + " = ");

				if (f.getType().equals(String.class))
					set = set.concat("'" + f.get(o) + "'" + ",");
				else
					set = set.concat(f.get(o) + ",");
			}
		}
		set = set.substring(0, set.length() - 1);
		Connection cn = Conexion.getInstance();
		String modificar = "Update " + tab + " " + set + " where " + campoId +" = " + idAux;
		PreparedStatement ps = cn.prepareStatement(modificar);
		ps.execute();
		cn.close();

	}

	public static void eliminar(Object o) throws NoSuchFieldException, SecurityException, SQLException {
		/*
		 * el cual debe eliminar el registro de la base de datos. Debe armarse
		 * la query por medio de reflexión utilizando las anotaciones creadas en
		 * el punto 2 y utilizando los métodos creados en UBean.
		 */
		Class c = o.getClass();
		Field[] atri = c.getDeclaredFields();
		String clave = "";
		String tab = "";

		tab = ((Tabla) c.getAnnotation(Tabla.class)).nomTab();

		for (Field f : atri) {
			if (f.getAnnotation(Id.class) != null)
				clave = f.getName();
		}
		Object obj = Ubean.ejecutarGet(o, clave);

		Connection cn = Conexion.getInstance();
		String eliminar = "Delete from " + tab + " where " + clave + " = " + obj;
		PreparedStatement st = cn.prepareStatement(eliminar);

		st.execute();
		cn.close();

	}

	public static Object obtenerPorId(Class c, Object id) throws SQLException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
		/*
		 * el cual debe devolver un objeto del tipo definido en el parámetro
		 * Class, con todos sus datos cargados. ). Debe armarse la query por
		 * medio de reflexión utilizando las anotaciones creadas en el punto 2 y
		 * utilizando los métodos creados en UBean
		 */
		Object obj = c.getConstructors()[0].newInstance(null);
		String campos = "";
		String tab = "";
		ArrayList<Field> ar = Ubean.obtenerAtributos(obj);

		tab = ((Tabla) c.getAnnotation(Tabla.class)).nomTab();
		for (int i = 0; i < ar.size(); i++) {
			campos = campos.concat(ar.get(i).getName() + ", ");
		}

		/*
		 * for (Field f : atri) {
		 * System.out.println(f.getAnnotation(Id.class));// sacar if
		 * (f.getAnnotation(Id.class) != null) campo = f.getName(); }
		 * Ubean.ejecutarGet(obj, campo); System.out.println(obj);// sacar
		 */
		String tipoId = "";
		for (Field f : ar) {
			if (f.getAnnotation(Id.class) != null)
				tipoId = f.getName();
		}

		campos = campos.substring(0, campos.length() - 2);
		Connection cn = Conexion.getInstance();
		String Seleccion = "Select " + campos + " from " + tab + " where " + tipoId + " = " + id;
		PreparedStatement std = cn.prepareStatement(Seleccion);

		ResultSet rs = std.executeQuery(); 

		int i = 0;
		while (rs.next()) { 
			for (Field f : ar) {

				Ubean.ejecutarSet(obj, ar.get(i).getName(), rs.getObject(f.getName()));

				i++;
				/*
				 * if (ar.get(i).getAnnotation(Id.class) != null) {
				 * Ubean.ejecutarSet(obj, ar.get(i).getName(),
				 * rs.getObject(((Id)f.getAnnotation(Id.class)).toString()));
				 * i++; } else { Ubean.ejecutarSet(obj, ar.get(i).getName(),
				 * rs.getObject(((Columna)
				 * f.getAnnotation(Columna.class)).nomCol())); i++; }
				 */
			}

		}

		cn.close();
		return obj;
	}

	public static Object guardar2(Object o) throws IllegalArgumentException, IllegalAccessException {

		Class c = o.getClass();
		Field[] atrib = c.getDeclaredFields();
		String campos = "";
		String tab = "";
		String valores = "";
		Object idNuevo = null;

		for (Field f : atrib) {
			if (f.getAnnotation(Columna.class) != null) {
				// if (f.getAnnotation(Id.class) == null) {
				campos = campos.concat(f.getAnnotation(Columna.class).nomCol() + ",");
				Ubean.ejecutarGet(c, f.getAnnotation(Columna.class).nomCol());
				if (f.getType().equals(String.class))
					valores = valores.concat("'" + f.get(o) + "'" + ",");
				else
					valores = valores.concat(f.get(o) + ",");
			}
		}
		campos = campos.substring(0, campos.length() - 1);
		valores = valores.substring(0, valores.length() - 1);
		tab = ((Tabla) c.getAnnotation(Tabla.class)).nomTab();

		Connection cn = Conexion.getInstance();
		try {
			String Insertar = "";
			Insertar = Insertar.concat("Insert into " + tab + " (" + campos + ") values (" + valores + ")");
			PreparedStatement st = cn.prepareStatement(Insertar, Statement.RETURN_GENERATED_KEYS);
			//st.execute();
			st.executeUpdate();

			ResultSet rs = st.getGeneratedKeys();
			if (rs.next())
				idNuevo = rs.getObject(1);

			cn.close();

			for (Field f : atrib) {
				if (f.getAnnotation(Id.class) != null)
					Ubean.ejecutarSet(o, f.getName(), idNuevo);

			}

			cn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return o;
	}
}
