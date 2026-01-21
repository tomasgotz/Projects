package aed;

public class PuntajeKey {
    Double puntaje;
    Integer id;

    public PuntajeKey(Double puntaje, Integer id) {
        this.puntaje = puntaje;
        this.id = id;
    }

    public Double puntaje() { 
        return puntaje; 
    }

    public Integer id() { 
        return id; 
    }

    public void cambiar_puntaje(Double puntaje) {
        this.puntaje = puntaje;
    }
}
