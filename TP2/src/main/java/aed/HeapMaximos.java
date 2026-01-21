package aed;

public class HeapMaximos {

    private Nodo raiz;
    private int cardinal;

    private class Nodo {
        Handle<NotaFinal> handle;
        Nodo padre;
        Nodo izq;
        Nodo der;

        Nodo(Handle<NotaFinal> h) {
            this.handle = h;
            this.padre = null;
            this.izq = null;
            this.der = null;

            h.setNodo(this);
        }
    }

    public HeapMaximos(){
        raiz = null;
        cardinal = 0;
    }

    public int getCardinal(){
        return this.cardinal;
    }

    public void agregar(Handle<NotaFinal> h) {
        Nodo nuevo = new Nodo(h);
        h.setNodo(nuevo);
        
        if (raiz == null) {
            raiz = nuevo;
            cardinal++;
            return;
        } 
        
        Nodo padre = buscarPadre(cardinal + 1);
        if (padre.izq == null) {
            padre.izq = nuevo;
        } else {
            padre.der = nuevo;
        }
        nuevo.padre = padre;
        cardinal++;

        subir(nuevo);
    }

    public NotaFinal sacar_maximo() {
        if (raiz == null) return null;
        NotaFinal max = raiz.handle.valor();

        if (cardinal == 1) {
            raiz.handle.setNodo(null);
            raiz = null;
            cardinal = 0;
            return max;
        }

        Nodo ultimo = buscarNodo(cardinal);
        intercambiar(raiz, ultimo);

        ultimo.handle.setNodo(null);

        eliminarUltimo(ultimo);
        cardinal--;

        bajar(raiz);

        return max;
    }

    // Auxiliares

    // Lógica de navegación y helpers idéntica a NoEntregados, pero con comparación Max
    
    private void subir(Nodo nodo) {
        while (nodo.padre != null) {
            // Si nodo > padre, intercambiar
            if (comparar(nodo.handle.valor(), nodo.padre.handle.valor())) {
                intercambiar(nodo, nodo.padre);
                nodo = nodo.padre;
            } else {
                break;
            }
        }
    }

    private void bajar(Nodo nodo) {
        while (nodo.izq != null) {
            Nodo mayor = nodo.izq;
            if (nodo.der != null && comparar(nodo.der.handle.valor(), nodo.izq.handle.valor())) {
                mayor = nodo.der;
            }

            if (comparar(mayor.handle.valor(), nodo.handle.valor())) {
                intercambiar(nodo, mayor);
                nodo = mayor;
            } else {
                break;
            }
        }
    }

    private void intercambiar(Nodo n1, Nodo n2) {
        Handle<NotaFinal> temp = n1.handle;
        n1.handle = n2.handle;
        n2.handle = temp;
        n1.handle.setNodo(n1);
        n2.handle.setNodo(n2);
    }

    // True si n1 > n2 (Max Heap: Mayor nota, desempate Mayor ID)
    private boolean comparar(NotaFinal n1, NotaFinal n2) {
        if (n1.getNota() > n2.getNota()) return true;
        if (n1.getNota() < n2.getNota()) return false;
        return n1.getId() > n2.getId();
    }

    
    // Reutilizar encontrarPadre, encontrarNodo y eliminarNodoFisico de NoEntregados
    // (o implementar una clase base abstracta Heap para no repetir código)
    private Nodo buscarPadre(int n) {
        String binario = aBinario(n);
        Nodo actual = raiz;
        for (int i = 1; i < binario.length() - 1; i++) {
            if (binario.charAt(i) == '0') {
                actual = actual.izq;
            }
            else {
                actual = actual.der;
            }
        }
        return actual;
    }

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

    private void eliminarUltimo(Nodo nodo) {
        if (nodo.padre == null) raiz = null;
        else {
            if (nodo.padre.izq == nodo) {
                nodo.padre.izq = null;
            }
            else {
                nodo.padre.der = null;
            }
            nodo.padre = null;
        }
    }
}