package aed;

public class NoEntregados {

    private Nodo raiz;
    private int cardinal;

    private class Nodo {
        Handle<PuntajeKey> handle; // El nodo guarda el Handle con el PuntajeKey para ordenar las hojas (Primero puntaje y despues id por desempate)
        Nodo padre;
        Nodo izq;
        Nodo der;

        Nodo(Handle<PuntajeKey> h) {
            this.handle = h;
            this.padre = null;
            this.izq = null;
            this.der = null;

            h.setNodo(this);
        }
    }

    public NoEntregados() {
        raiz = null;
        cardinal = 0;
    }
    // O(1)

    public int getCardinal(){
        return this.cardinal;
    }

    public void agregar(Handle<PuntajeKey> h) {
        Nodo nuevo = new Nodo(h);
        h.setNodo(nuevo); // Vinculamos el Handle a este nuevo nodo
        
        if (raiz == null) {
            raiz = nuevo;
            cardinal++;
            return;
        } 
        
        Nodo padre = buscarPadre(cardinal + 1); // Como se llena nivel por nivel, debemos buscar el padre del que va a ser el nuevo nodo, que estaria en la ultima hoja
        if (padre.izq == null) {
            padre.izq = nuevo; // Primero se pregunta por el izquierdo ya que los heaps se llenan de izquierda a derecha
        } else {
            padre.der = nuevo;
        }
        nuevo.padre = padre;
        cardinal++;

        subir(nuevo); // En el caso de que el nuevo no corresponda en esa posicion, se sube hasta que el heap quede ordenado nuevamente
    }
    // Complejidad total: O(log n)

    public PuntajeKey sacar_minimo() {
        if (raiz == null) return null;

        PuntajeKey min = raiz.handle.valor();

        // Si solo hay un elemento
        if (cardinal == 1) {
            raiz.handle.setNodo(null); // Desenlazamos el nodo de la raiz
            raiz = null;
            cardinal = 0;
            return min;
        }

        // Buscamos el ultimo nodo insertado para reemplazar la raiz
        Nodo ultimo = buscarNodo(cardinal);
        
        // Se intercambian los contenidos entre raiz y ultimo
        intercambiar(raiz, ultimo);

        ultimo.handle.setNodo(null); // Desenlazamos su referencia al nodo que ya no existira
        
        // Ahora el nodo que era ultimo tiene el valor minimo de la raiz y el que era la raiz ahora tiene lo que tenia el que era el ultimo.
        // Eliminamos el nodo que era ultimo del arbol.
        eliminarUltimo(ultimo);
        cardinal--;
        

        // Si el que es la raiz ahora no es el minimo de todo el heap, se baja hasta que quede ordenado el heap
        bajar(raiz);

        return min;
    }
    // Complejidad total: O(log n)


    public void eliminar(Handle<PuntajeKey> handle) {
        // Obtenemos el nodo directamente desde el handle
        Nodo nodoAEliminar = (Nodo) handle.getNodo();
        if (nodoAEliminar == null) return;

        // Si es el último nodo del árbol, simplemente lo borramos
        Nodo ultimo = buscarNodo(cardinal);

        if (nodoAEliminar == ultimo) {
            eliminarUltimo(nodoAEliminar);
            cardinal--;
            handle.setNodo(null); // Desvincular handle
            return;
        }

        // Si no es el último, intercambiamos su handle por el del último
        intercambiar(nodoAEliminar, ultimo);
        
        // Ahora lo que queremos eliminar esta en el nodo ultimo
        eliminarUltimo(ultimo);
        cardinal--;
        handle.setNodo(null); // Desenlazamos el nodo con el handle

        // Restauramos el orden
        reacomodarNodo(nodoAEliminar);
        
    }
    // Complejidad total: Lo unico que agrega complejidad son las funciones de subir y bajar las cuales son O(log n) ambas. Como no hay ningun ciclo ni nada la complejidad total es O(log n)

    // --- Auxiliares ---

    public void actualizarPuntaje(Handle handle, Double nuevoPuntaje) {
        Nodo nodo = (Nodo) handle.getNodo(); // O(1)
        
        if (nodo == null) {
            return;
        }
        ((PuntajeKey) handle.valor()).cambiar_puntaje(nuevoPuntaje); // O(1)

        reacomodarNodo(nodo); // O(log n)
    }
    // O(log n)

    public void reacomodarNodo(Nodo nodo){
        if (nodo.padre != null && comparar(nodo.handle.valor(), nodo.padre.handle.valor())) {
            subir(nodo); // Si es mas chico que su padre deberia subir, y sino deberia bajar hasta que quede ordenado el heap // O(log n)
        } else {
            bajar(nodo); // O(log n)
        }
    }
    // O(log n)


    // Sift-up // O(log n)
    private void subir(Nodo nodo) {
        while (nodo.padre != null) { // Si no es la raiz
            if (comparar(nodo.handle.valor(), nodo.padre.handle.valor())) {
                intercambiar(nodo, nodo.padre);
                nodo = nodo.padre; // Se asigna el nodo actual a donde esta ahora el handle que movimos recien, para seguir subiendo el handle en el caso que no sea la raiz o que todavia este desordenado
            } else {
                break; // Esto ayuda en el caso que nos den los estudiantes con puntaje 0.0 en el construcor de Edr, por lo cual ordenariamos el heap exclusivamente por id del estudiante, y como estan ordenados ya que va agregando por indice del for en el constructor, siempre que entre de vuelta a esta funcion va a ir al break porque el handle con el id a insertar no tiene que subir porque es mas grande que el padre
            }
        }
    }

    // Sift-down // O(log n)
    private void bajar(Nodo nodo) {
        // Como mucho log n iteraciones
        while (nodo.izq != null) { // Mientras tenga al menos un hijo (Por propiedad de Heaps siempre va a ser el izquierdo si solo tiene un hijo ya que se llena de izquierda a derecha)
            Nodo menor = nodo.izq; // 
            if (nodo.der != null && comparar(nodo.der.handle.valor(), nodo.izq.handle.valor())) {
                menor = nodo.der;
            }

            if (comparar(nodo.handle.valor(), menor.handle.valor())) {
                break; // Ya está ordenado
            }

            intercambiar(nodo, menor);
            nodo = menor;
        }
    }

    private void intercambiar(Nodo n1, Nodo n2) {
        Handle<PuntajeKey> temp = n1.handle;
        n1.handle = n2.handle;
        n2.handle = temp;

        // Actualizamos la referencia de los nodos en los handles
        n1.handle.setNodo(n1);
        n2.handle.setNodo(n2);
    }
    // O(1)

    // Devuelve true si k1 < k2 y desempata por id del estudiante
    private boolean comparar(PuntajeKey k1, PuntajeKey k2) {
        if (k1.puntaje() < k2.puntaje()) return true;
        if (k1.puntaje() > k2.puntaje()) return false;
        return k1.id() < k2.id();
    }
    // O(1)

    private Nodo buscarPadre(int n) {
        // Pasamos el cardinal a binario
        String binario = aBinario(n); // O(log n)
        Nodo actual = raiz;
        // Recorremos desde el segundo char hasta el anteúltimo ya que queremos el padre del ultimo
        for (int i = 1; i < binario.length() - 1; i++) { // O(log n) ya que recorre la cantidad de bits de n, y esto es log2(n)
            if (binario.charAt(i) == '0') {
                actual = actual.izq;
            }
            else { 
                actual = actual.der;
            }
        }
        return actual;
    }
    // Complejidad total: O(log n) + O(log n) = O(log n)

    private String aBinario(int n){
        String binario = "";
        int resto = 0;
        while(n > 0) {
            resto = n % 2;
            binario = resto + "" + binario;
            n = n / 2;
        }
        return binario;
    }
    // Este metodo tiene una complejidad de O(log n) ya que se ejecuta n/2 a la k veces, y k es la cantidad de iteraciones del while


    //Igual que buscarPadre solo que si llega al ultimo nodo (hoja)
    private Nodo buscarNodo(int n) {
        if (n == 1) return raiz;
        String binario = aBinario(n);
        Nodo actual = raiz;
        for (int i = 1; i < binario.length(); i++) {
            if (binario.charAt(i) == '0') {
                actual = actual.izq;
            }
            else { 
                actual = actual.der;
            }
        }
        return actual;
    }
    // Tambien O(log n)



    private void eliminarUltimo(Nodo nodo) {
        if (nodo.padre == null) { // Es la raíz
            raiz = null;
        } 
        else { // Se cortan las hojas/nodos del arbol
            if (nodo.padre.izq == nodo) {
                nodo.padre.izq = null;
            }
            else {
                nodo.padre.der = null;
            }
            nodo.padre = null;
        }
    }
    // O(1)
}