package frsf.isi.died.guia08.problema01.modelo;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.time.format.DateTimeFormatter;

public class Empleado {

	public enum Tipo { CONTRATADO,EFECTIVO}; 
	private Integer cuil;
	private String nombre;
	private Tipo tipo;
	private Double costoHora;
	private List<Tarea> tareasAsignadas;
	private LocalDateTime fechaContratacion;
	private Function<Tarea, Double> calculoPagoPorTarea;		
	private Predicate<Tarea> puedeAsignarTarea;

	
	public Integer getCuil() {
		return cuil;
	}

	public void setCuil(Integer cuil) {
		this.cuil = cuil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Double getCostoHora() {
		return costoHora;
	}

	public void setCostoHora(Double costoHora) {
		this.costoHora = costoHora;
	}

	public List<Tarea> getTareasAsignadas() {
		return tareasAsignadas;
	}

	public void setTareasAsignadas(List<Tarea> tareasAsignadas) {
		this.tareasAsignadas = tareasAsignadas;
	}

	public LocalDateTime getFechaContratacion() {
		return fechaContratacion;
	}

	public void setFechaContratacion(LocalDateTime fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}

	public Function<Tarea, Double> getCalculoPagoPorTarea() {
		return calculoPagoPorTarea;
	}

	public void setCalculoPagoPorTarea(Function<Tarea, Double> calculoPagoPorTarea) {
		this.calculoPagoPorTarea = calculoPagoPorTarea;
	}

	public Predicate<Tarea> getPuedeAsignarTarea() {
		return puedeAsignarTarea;
	}

	public void setPuedeAsignarTarea(Predicate<Tarea> puedeAsignarTarea) {
		this.puedeAsignarTarea = puedeAsignarTarea;
	}
	
	//ITEM 2.b RESUELTO
	public Double salario() {
		Double aCobrar = 0.0;
		if (this.getTipo()==Tipo.EFECTIVO) {
			for (Tarea hw : tareasAsignadas) {
				if(hw.getFacturada()==false) {
					Integer aux = (int) Duration.between(hw.getFechaFin(), hw.getFechaInicio()).toDays();
					if (hw.getDuracionEstimada()<(aux*4)) {
						aCobrar += (this.costoHora*1.2)*(hw.getDuracionEstimada());
					}else {
						aCobrar += this.costoHora*hw.getDuracionEstimada();
					}
					hw.setFacturada(true);
				}
			}
		}else {
			for (Tarea hw : tareasAsignadas) {
				if(hw.getFacturada()==false) {
					Integer aux = (int) Duration.between(hw.getFechaFin(), hw.getFechaInicio()).toDays();
					if (hw.getDuracionEstimada()<(aux*4)) {
						aCobrar += (this.costoHora*1.3)*(hw.getDuracionEstimada());
					}else if (aux>2) {
						aCobrar += (this.costoHora*0.75)*(hw.getDuracionEstimada());
					}else {
						aCobrar += this.costoHora*hw.getDuracionEstimada();
					}
					hw.setFacturada(true);
				}
			}
		}
		
		return aCobrar;
	}
	//FIN ITEM 2.b RESUELTO
	
	// ITEM 2.a RESUELTO
	public Boolean asignarTarea(Tarea t) throws tareaIncorrectaException{
		if (this.getTipo()==Tipo.CONTRATADO) {
			if (this.tareasAsignadas.size() >=5) {
				return false;
			}
		} else {
			Integer aux = 0;
			for (Tarea hw : tareasAsignadas) {
				aux += hw.getDuracionEstimada();
			}
			if (aux>=15) {
				return false;
			}
		}
		if (t.getEmpleadoAsignado()==null && t.getFechaFin()==null) {
			this.tareasAsignadas.add(t);
		} else {
				throw new tareaIncorrectaException();
		}
		return false;

	}
	// FIN ITEM 2.a RESUELTO
	
	// ITEM 2.c RESUELTO
	public void comenzar(Integer idTarea) throws fechaTareaException{
		Boolean flag = false;
		for (Tarea hw : tareasAsignadas) {
			if (hw.getId()==idTarea) {
				 LocalDateTime ahora  = LocalDateTime.now();
				hw.setFechaInicio(ahora);
				flag = true;
			}
		}
		if (flag == false) throw new fechaTareaException("No se encontro la tarea. No se seteo una fecha de inicio.");
	}
	// FIN ITEM 2.c RESUELTO
	
	// ITEM 2.d RESUELTO
	public void finalizar(Integer idTarea) throws fechaTareaException{
		Boolean flag = false;
		for (Tarea hw : tareasAsignadas) {
			if (hw.getId()==idTarea) {
				 LocalDateTime ahora  = LocalDateTime.now();
				hw.setFechaFin(ahora);
				flag = true;
			}
		}
		if (flag == false) throw new fechaTareaException("No se encontro la tarea. No se seteo una fecha de fin.");
	}
	// FIN ITEM 2.d RESUELTO

	// ITEM 2.e RESUELTO
	public void comenzar(Integer idTarea,String fecha) throws fechaTareaException {
		Boolean flag = false;
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		LocalDateTime inicio = LocalDateTime.parse(fecha,formato);
		for (Tarea hw : tareasAsignadas) {
			if (hw.getId()==idTarea) {
				hw.setFechaInicio(inicio);
				flag = true;
			}
		}
		if (flag == false) throw new fechaTareaException("No se encontro la tarea. No se seteo una fecha de inicio.");
	}
	// FIN ITEM 2.e RESUELTO
	
	// ITEM 2.f RESUELTO
	public void finalizar(Integer idTarea,String fecha) throws fechaTareaException {
		Boolean flag = false;
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
		LocalDateTime fin = LocalDateTime.parse(fecha,formato);
		for (Tarea hw : tareasAsignadas) {
			if (hw.getId()==idTarea) {
				hw.setFechaInicio(fin);
				flag = true;
			}
		}
		if (flag == false) throw new fechaTareaException("No se encontro la tarea. No se seteo una fecha de inicio.");
	}
	// FIN ITEM 2.f RESUELTO
}
