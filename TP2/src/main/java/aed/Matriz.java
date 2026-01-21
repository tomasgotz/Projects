package aed;

public class Matriz {
    
    private int[][] datos;
    private int filas;
    private int columnas;

    public Matriz(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.datos = new int[filas][columnas];
    }

    public int get(int fila, int col) {
        return this.datos[fila][col];
    }

    public void incrementar(int fila, int col) {
        this.datos[fila][col]++;
    }

    public int getFilas() {
        return this.filas;
    }

    public int getColumnas() {
        return this.columnas;
    }
}