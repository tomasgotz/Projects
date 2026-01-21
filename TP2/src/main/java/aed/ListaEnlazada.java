package aed;

public class ListaEnlazada<T>{
    private Nodo primero;

    private class Nodo {
        T valor;
        Nodo sig;
        Nodo ant;
        Nodo(T v) { 
            valor = v;
            sig = null;
            ant = null;
         }
    }

    public ListaEnlazada() {
        primero = null;
    }

    public int longitud() {
        if(primero == null){
            return 0;
        }else{
            Nodo actual = primero;
            int i = 1;
            while(actual.sig != null){
                actual = actual.sig;
                i += 1;
            }
            return i;
        }
    }

    public void agregarAdelante(T elem) {
        Nodo nuevo = new Nodo(elem);
        if(primero == null){
            primero = nuevo;
        }else{
            nuevo.sig = primero;
            nuevo.ant = primero.ant;
            primero.ant = nuevo;
            primero = nuevo;
        }
    }

    public void agregarAtras(T elem) {
        Nodo nuevo = new Nodo(elem);
        if(primero == null){
            primero = nuevo;
        }else{
            Nodo actual = primero;
            while(actual.sig != null){
                actual = actual.sig;
            }
            actual.sig = nuevo;
            nuevo.ant = actual;
        }
    }

    public T obtener(int i) {
        Nodo actual = primero;
        for(int j = 0; j < i; j++){
            actual = actual.sig;
        }
        return actual.valor;
    }
    

    public void eliminar(int i) {
        Nodo actual = primero;
        Nodo prev = primero;
        for(int j = 0; j < i; j++){
            prev = actual;
            actual = actual.sig;
        }
        if(this.longitud() == 1){
            primero = null;
        }else if(i == 0){
            primero = primero.sig;
            primero.ant = null;
        }else if(i == this.longitud()-1){
            prev.sig = null;
        }else{
            prev.sig = actual.sig;
            actual.sig.ant = prev;
        }
        }
    

    public void modificarPosicion(int indice, T elem) {
        Nodo actual = primero;
        for(int j = 0; j < indice; j++){
            actual = actual.sig;
        }
        actual.valor = elem;
    }

    public ListaEnlazada(ListaEnlazada<T> lista) {
        Nodo actual = lista.primero;
        while(actual != null){
            agregarAtras(actual.valor);
            actual = actual.sig;
        }
    }
    
    @Override
    public String toString() {
        String res = "[";
        Nodo actual = primero;
        while(actual.sig != null){
            res = res + actual.valor + ", ";
            actual = actual.sig;
        }
        res = res + actual.valor + "]";
        return res;
    }

    public class ListaIterador {
    	private int dedito;
        public ListaIterador(){
            dedito = 0;
        }
        
        public boolean haySiguiente() {
	        return dedito != longitud();
        }
        
        public boolean hayAnterior() {
	        return dedito != 0;
        }

        public T siguiente() {
            Nodo actual = primero;
            for(int j = 0; j < dedito; j++){
                actual = actual.sig;
            }
            dedito = dedito + 1;
            return actual.valor;
        }
        

        public T anterior() {
            Nodo actual = primero;
            for(int j = 0; j < dedito - 1; j++){
                actual = actual.sig;
            }
            dedito = dedito - 1;
            return actual.valor;
        }
    }

    public ListaIterador iterador() {
	    return new ListaIterador();
    }

}
