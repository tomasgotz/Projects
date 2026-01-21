package aed;

public class Estudiante {
    Examen examen;
    Boolean entregado;
    Boolean sospechoso;
    Double puntaje;
    Posicion posicion;
    Handle<PuntajeKey> handle;

    public Estudiante(Posicion posicion, Handle<PuntajeKey> handle, int R){
        this.posicion = posicion;
        this.examen = new Examen(R);
        this.puntaje = 0.0;
        this.sospechoso = false;
        this.entregado = false;
        this.handle = handle;
    }

    public Examen getExamen(){
        return this.examen;
    }

    public Boolean getEntregado(){
        return this.entregado;
    }

    public Boolean getSospechoso(){
        return this.sospechoso;
    }

    public Double getPuntaje(){
        return this.puntaje;
    }

    public Posicion getPosicion(){
        return this.posicion;
    }

    public Handle<PuntajeKey> getHandle(){
        return this.handle;
    }

    public void setEntregado(Boolean entregado){
        this.entregado = entregado;
    }

    public void setExamen(Examen examen){
        this.examen = examen;
    }
    
    public void setPuntaje(Double puntaje){
        this.puntaje = puntaje;
    }

    public void setSospechoso(Boolean sospechoso){
        this.sospechoso = sospechoso;
    }

    public Integer getId() {
    
        return this.handle.valor().id;
    }

}
