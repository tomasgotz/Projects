package aed;

import java.util.ArrayList;

public class Examen {
    ArrayList<Integer> respuestas;

    public Examen(int R) {
        // Inicializa el ArrayList con capacidad R
        this.respuestas = new ArrayList<>(R); 
        
        // Llena la lista con R valores "en blanco" (-1)
        for (int i = 0; i < R; i++) {
            this.respuestas.add(-1); 
        }
    }

    public Integer respuesta(Integer indice) { //devuelve la respuesta en la posicion indice
        return this.respuestas.get(indice);
    }

    public void responder(Integer res, Integer i){ //asigna la respuesta res en la posicion i
        this.respuestas.set(i, res);
    }
}