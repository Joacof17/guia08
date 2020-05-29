package frsf.isi.died.guia08.problema01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.fechaTareaException;
import frsf.isi.died.guia08.problema01.modelo.tareaIncorrectaException;

public class AppRRHH {

	private List<Empleado> empleados;
	
	//ITEM 4.a RESUELTO
	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {
		Empleado e = new Empleado();
		e.setCostoHora(costoHora);
		e.setCuil(cuil);
		e.setNombre(nombre);
		e.setTipo(Tipo.CONTRATADO);
		this.empleados.add(e);
	}
	//FIN ITEM 4.a RESUELTO
	
	//ITEM 4.b RESUELTO
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		Empleado e = new Empleado();
		e.setCostoHora(costoHora);
		e.setCuil(cuil);
		e.setNombre(nombre);
		e.setTipo(Tipo.EFECTIVO);
		this.empleados.add(e);
	}
	//FIN ITEM 4.b RESUELTO
	
	//ITEM 4.c RESUELTO
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) throws tareaIncorrectaException {
		Predicate<Empleado> condicion = e -> e.getCuil()==cuil;
		Optional <Empleado> e = this.buscarEmpleado(condicion);
		Empleado employee = e.get();
		Tarea t = new Tarea();
		t.setId(idTarea);
		t.setDescripcion(descripcion);
		t.setDuracionEstimada(duracionEstimada);
		try {
			if(employee!=null) {
				employee.asignarTarea(t);
			}
		} catch (tareaIncorrectaException hw) {
			throw new tareaIncorrectaException();
		}
	}
	//FIN ITEM 4.c RESUELTO
	
	//ITEM 4.d RESUELTO
	public void empezarTarea(Integer cuil,Integer idTarea) throws fechaTareaException {
		Predicate<Empleado> condicion = e -> e.getCuil()==cuil;
		Optional <Empleado> e = this.buscarEmpleado(condicion);
		Empleado employee = e.get();
		try {
			employee.comenzar(idTarea);
		} catch (fechaTareaException hw) {
			throw new fechaTareaException("No se pudo cargar el inicio de la tarea.");
		}
	}
	//FIN ITEM 4.d RESUELTO
	
	//ITEM 4.e RESUELTO
	public void terminarTarea(Integer cuil,Integer idTarea) throws fechaTareaException {
		Predicate<Empleado> condicion = e -> e.getCuil()==cuil;
		Optional <Empleado> e = this.buscarEmpleado(condicion);
		Empleado employee = e.get();
		try {
			employee.finalizar(idTarea);
		} catch (fechaTareaException hw) {
			throw new fechaTareaException("No se pudo cargar el fin de la tarea.");
		}	
	}
	//FIN ITEM 4.e RESUELTO

	//ITEM 4.f RESUELTO
	public void cargarEmpleadosContratadosCSV(String nombreArchivo)  throws FileNotFoundException, IOException {
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine())!=null) {
					String[] fila = linea.split(";");
						Empleado e = new Empleado();
						e.setCuil(Integer.valueOf(fila[0]));
						e.setNombre(fila[1]);
						e.setCostoHora(Double.valueOf(fila[2]));
						e.setTipo(Tipo.CONTRATADO);
						this.empleados.add(e);
				}
			}
		}

	}
	//FIN ITEM 4.f RESUELTO

	//ITEM 4.g RESUELTO
	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine())!=null) {
					String[] fila = linea.split(";");
						Empleado e = new Empleado();
						e.setCuil(Integer.valueOf(fila[0]));
						e.setNombre(fila[1]);
						e.setCostoHora(Double.valueOf(fila[2]));
						e.setTipo(Tipo.EFECTIVO);
						this.empleados.add(e);
				}
			}
		}		
	}
	//FIN ITEM 4.g RESUELTO

	//ITEM 4.h RESUELTO
	public void cargarTareasCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		try(Reader fileReader = new FileReader(nombreArchivo)) {
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine())!=null) {
					String[] fila = linea.split(";");
						Tarea t = new Tarea();
						t.setId(Integer.valueOf(fila[0]));
						t.setDescripcion(fila[1]);
						t.setDuracionEstimada(Integer.valueOf(fila[2]));
						Predicate<Empleado> condicion = e -> e.getCuil()==Integer.valueOf(fila[3]);
						Optional <Empleado> e = this.buscarEmpleado(condicion);
						Empleado employee = e.get();
						t.setEmpleadoAsignado(employee);
						
				}
			}
		}
	}
	//FIN ITEM 4.h RESUELTO
	
	//ITEM 4.i RESUELTO
	private void guardarTareasTerminadasCSV() throws IOException {
		for (Empleado e : this.empleados) {
			for (Tarea t : e.getTareasAsignadas()) {
				if (t.getFacturada()==false && t.getFechaFin()!=null) {
					String wr = new String();
					wr = t.getId().toString() + ";" +
							t.getDescripcion() + ";" +
							t.getDuracionEstimada().toString() + ";" +
							t.getFechaInicio().toString() + ";" +
							t.getFechaFin().toString() + ";" +
							e.getCuil().toString() + ";" +
							e.getNombre() ;
					try(Writer fileWriter= new FileWriter("tareasTerminadas.csv",true)) {
						try(BufferedWriter out = new BufferedWriter(fileWriter)){
							out.write(wr + System.getProperty("line.separator"));
						}
					}
				}
			}
		}
	}
	//FIN ITEM 4.i RESUELTO
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() throws IOException {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}
}
