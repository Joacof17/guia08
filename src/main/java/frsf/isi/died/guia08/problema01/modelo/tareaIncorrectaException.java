package frsf.isi.died.guia08.problema01.modelo;

public class tareaIncorrectaException extends Exception {
	public tareaIncorrectaException(){
		super("La tarea no se ha podido cargar" );
	}
}
