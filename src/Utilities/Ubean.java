package Utilities;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Ubean {

	public static ArrayList<Field>  obtenerAtributos(Object o){
		//Devuelve un ArrayList<Field> con todos los atributos que posee el parámetro Object.
		Class c = o.getClass();
		Field[] f =null;
		
		 f = c.getDeclaredFields();
		 
		 ArrayList<Field> a = new ArrayList();

		for(int i = 0; i < f.length; i++){
			System.out.println(f[i].getName());
			a.add(f[i]);
		
		}
		return a;
	}

	public static void ejecutarSet(Object o, String att, Object valor){
	//	Se debe ejecutar el método Setter del String dentro del Object.
		Class c = o.getClass();
		Method[] mets = c.getDeclaredMethods(); //me traigo todos los metodos de c. 	
		Object[] obj = new Object[1];

		obj[0]=valor;

		for(Method m: mets){
			if(m.getName().startsWith("set"+ att.substring(0, 1).toUpperCase()+att.substring(1).toLowerCase())){ // getname te devuelve el nombre del parametro
				try {
					m.invoke(o, obj);
					
					
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		

		}
	}

	public static Object ejecutarGet(Object o, String att)	{
		// devolverá el valor del atributo pasado por parámetro, ejecutando el getter dentro del objeto
		Class c = o.getClass();
		Method[] mets = c.getDeclaredMethods(); 
		Object[] obj = new Object[0];
	
		
		for(Method m: mets){

		if(m.getName().equals(("get"+ att.substring(0, 1).toUpperCase()+att.substring(1).toLowerCase()))){ // getname te devuelve el nombre del parametro
			try {
				
				return m.invoke(o, obj);		
				
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}	
		
		}
		return o;
			
	}
	
}
