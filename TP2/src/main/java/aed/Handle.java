package aed;

public class Handle<T> {

    private T valor;
    private Object nodo; // Ya no usamos indice, usamos una referencia al nodo contenedor
    // Usamos Object para no tener que definir una clase externa para Nodo, despues en cada heap casteamos el valor de nodo a la variable nodo de cada heap

    public Handle(T valor) {
        this.valor = valor;
        this.nodo = null;
    }

    public T valor() {
        return valor;
    }

    public void setNodo(Object nodo) {
        this.nodo = nodo;
    }

    public Object getNodo() {
        return this.nodo;
    }

}