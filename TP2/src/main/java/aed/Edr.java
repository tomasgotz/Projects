package aed;

import java.util.ArrayList;

public class Edr {
    ArrayList<Estudiante> estudiantes;
    NoEntregados noEntregados;
    Examen examenCanonico;
    Integer dimension;


    public Edr(Integer ladoAula, Integer cantEstudiantes, Examen examenCanonico) {
        Integer filaActual = 0; // O(1)
        Integer columnaActual = 0; // O(1)
        this.noEntregados = new NoEntregados(); // O(1)
        this.estudiantes = new ArrayList<>(cantEstudiantes); // O(1)

        int R = examenCanonico.respuestas.size(); // O(1)

        // Inicio del ciclo 
        for(Integer i = 0; i < cantEstudiantes; i++){  // O(E) total de iteraciones

            filaActual = i / ((ladoAula + 1) / 2); // la fila cambia cuando se sientan el techo de la mitad de los asientos, O(1)
            columnaActual = (i % ((ladoAula + 1) / 2)) * 2; // deja una columna libre entre asientos, O(1)

            Posicion pos = new Posicion(filaActual, columnaActual); // calculo la posición O(1)
            
            Handle<PuntajeKey> h = new Handle<>(new PuntajeKey(0.0, i)); //crea un Handle con un indice para este estudiante, O(1)
            
            Estudiante estudiante = new Estudiante(pos, h, R); // O(R)

            this.estudiantes.add(estudiante); // añade el estudiante al array O(1)
            
            this.noEntregados.agregar(h); // agrega el handle a un nodo del heap O(1) en este caso especifico
        }
        // Fin del ciclo: Complejidad O (E*R)
        this.examenCanonico = examenCanonico; // O(1)
        this.dimension = ladoAula; // O(1)
    }

/*
Funcionamiento:

Organización Espacial: Calcula la ubicación de cada estudiante en el aula basándose en el parámetro ladoAula. Los estudiantes se sientan dejando un asiento libre de por medio, por lo que la cantidad de asientos por fila es (ladoAula +1) / 2.

Inicialización de Estudiantes: Crea una lista de E objetos Estudiante. Cada uno recibe un examen vacio (relleno con -1) y un handle que lo vincula a la estructura de prioridad.

Carga del Heap: Agrega cada estudiante al min-heap noEntregados. Como todos entran con puntaje 0.0 e IDs correlativos, el heap se mantiene ordenado naturalmente durante la inserción. 

Justificación de la complejidad: O(E*R)
El ciclo principal se ejecuta E veces.
Dentro del ciclo, la operación mas costosa es inicializar el ArrayList de respuestas del examen, que tiene tamaño R, resultando en O(R)
La inserción en el heap es O(1) en este caso especifico porque no requiere intercambios, pero la estructura general del ciclo domina la complejidad. 
*/
    
    /* Justificación de la complejidad O(E*R): El factor dominante es el ciclo que itera sobre los E estudiantes.
       Dentro de ese ciclo la operación con mayor complejidad es crear el estudiante con su respectivo examen que cuesta O(R)
       Esa complejidad se calcula como O(E) (cantidad total de estudiantes) * O(R) ya que esta operación se realiza E veces.
       De esta manera los costos se multiplican y la complejidad final de la operación es O(E*R).
       Aunque agregar en el heap es O(log E) en el peor caso, al insertar los estudiantes con puntaje 0.0 e ids ordenados de menor a mayor, 
       la propiedad de noEntregados se mantiene sin realizar intercambios (el nuevo hijo siempre es mayor al padre) entonces no necesita subir a nadie. 
       Por lo tanto la complejidad total se mantiene en O(E*R).
    */ 

    public void copiarse(Integer idEstudiante){
        Estudiante estudiante = this.estudiantes.get(idEstudiante); // O(1)
        
        if(estudiante.entregado == false) { // Si ya entrego, no hace nada. O(1)

            Examen examenEstudiante = estudiante.getExamen(); // O(1)
            ArrayList<Estudiante> vecinos = vecinos(idEstudiante, this.dimension); // O(1)

            RespuestaACopiar mejorOpcion = new RespuestaACopiar(0, -1, -1); // O(1)
            int idMejorVecino = -1; // O(1)

            //  Itera sobre los vecinos para encontrar al mejor
            for (Estudiante vecino : vecinos) { // Itera sobre un numero constante de vecinos. Complejidad: O(V) donde V es la constante de vecinos.
                int idVecinoActual = vecino.getId(); // O(1)

                RespuestaACopiar opcionActual = compararExamenes(examenEstudiante, vecino.getExamen()); // O(R)

                // Compara con el mejor actual
                if (opcionActual.totalDiferencias > mejorOpcion.totalDiferencias) { // O(1)
                    // Este vecino es estrictamente mejor
                    mejorOpcion = opcionActual;
                    idMejorVecino = idVecinoActual;
                } 
                else if (opcionActual.totalDiferencias == mejorOpcion.totalDiferencias && opcionActual.totalDiferencias > 0) { // O(1)
                    // Hay un empate, "Desempata por id mayor" 
                    if (idVecinoActual > idMejorVecino) {
                        mejorOpcion = opcionActual;
                        idMejorVecino = idVecinoActual;
                    }
                }
            }
            // Fin del ciclo: Complejidad O(V*R) = O(R) ya que V es una constante que es el numero de vecinos

            if (mejorOpcion.totalDiferencias > 0) { // O(1)
                resolver(idEstudiante, mejorOpcion.primerNroEjercicio, mejorOpcion.primeraRespuesta); // O(log E)
            }
    }
}

/* Funcionamiento:
Identificación de vecinos: Obtiene los estudiantes sentados a la izquierda, derecha y arriba mediante cálculos de ID y posición en el aula.
Análisis comparativo: Recorre el examen de cada vecino para contar cuantas respuestas tiene que el estudiante actual no respondio (ejercicios en -1).
Selección y Accion: Identifica al vecino con mas respondidas que el estudiante no tiene. 
En caso de empate, elige al de ID mayor. Si encuentra una opción valida, copia la primera respuesta diferente y actualiza el puntaje del estudiante llamando a resolver.

Justificación de la complejidad: O(R + log E)
La búsqueda de vecinos es O(1)
La comparación de exámenes recorre las R respuestas para cada uno de los vecinos, resultando en O(R)
La llamada a resolver actualiza el heap, lo cual es O(log E) 
*/

/* Justificación de la complejidad O(R + log E): Esta operación tiene 2 secciones secuenciales que son independientes de cada una.
 * Por lo tanto en este caso la complejidad se suma en vez de multiplicarse.
 * Como podemos notar la complejidad del ciclo es O(R) + la complejidad de resolver que por la cola de prioridad/Heap tiene una complejidad de O(log E)
 * De esta manera la complejidad temporal final de la operación es: O(R + log E)
 */

    public void resolver(Integer idEstudiante, Integer nroEjercicio, Integer resEjercicio) {
        // Obtener datos - O(1)
        Estudiante estudiante = this.estudiantes.get(idEstudiante);
        Examen examenEstudiante = estudiante.getExamen();
        int respuestaCorrecta = this.examenCanonico.respuesta(nroEjercicio);
        int R = this.examenCanonico.respuestas.size();

        // Obtener la respuesta anterior - O(1)
        int respuestaAntigua = examenEstudiante.respuesta(nroEjercicio);

        // Establecer la nueva respuesta - O(1)
        examenEstudiante.responder(resEjercicio, nroEjercicio);
        int respuestaNueva = resEjercicio;

        // Calcular el cambio (delta) en el puntaje - O(1)
        boolean eraCorrecta = (respuestaAntigua != -1 && respuestaAntigua == respuestaCorrecta);
        boolean esCorrecta = (respuestaNueva != -1 && respuestaNueva == respuestaCorrecta);

        double deltaPuntaje = 0.0;
        double puntajePorRespuesta = (R == 0) ? 0.0 : (100.0 / R); 

        if (!eraCorrecta && esCorrecta) {
            // El estudiante cambió una respuesta incorrecta (o vacía) por la correcta
            deltaPuntaje = puntajePorRespuesta;
        } else if (eraCorrecta && !esCorrecta) {
            // El estudiante cambió una respuesta correcta por una incorrecta
            deltaPuntaje = -puntajePorRespuesta;
        }
        // Si ambas eran correctas o ambas incorrectas, deltaPuntaje es 0.

        // Aplicar el delta al puntaje existente - O(1)
        double puntajeNuevo = estudiante.getPuntaje() + deltaPuntaje;
        estudiante.setPuntaje(puntajeNuevo);
        
        // Actualizar la posición del estudiante en el heap - O(log E)
        this.noEntregados.actualizarPuntaje(estudiante.getHandle(), puntajeNuevo);
    }

/* Funcionamiento: 
Modificación del examen: Cambia la respuesta del ejercicio nroEjercicio en el objeto Examen del estudiante.
Calculo de Puntaje: Compara la respuesta antigua y la nueva con el examenCanonico. Si paso de incorrecta a correcta, suma el proporcional del puntaje; si fue al reves lo resta.
Logica de puntaje: El codigo chequea si el estudiante ya tenia una respuesta en ese ejercicio. Si ya tenia una y es igual a la nueva, no hace nada. Si el ejercicio estaba en blanco (-1) y ahora tiene una respuesta correcta, aumenta el puntaje, si cambia una correcta por una incorrecta el puntaje disminuye. 
Sincronización del Heap: Como el puntaje cambio, la prioridad del estudiante en el heap de noEntregados puede haber variado. Se utiliza el Handle para encontrar el nodo en el árbol y reubicarlo mediante subir o bajar.


Justificación de la complejidad: O(log E)
El calculo del delta de puntaje es O(1)
La actualización del heap requiere navegar la altura del árbol binario, que es proporcional a log E. */

/* Justificación de la complejidad O(log E): La complejidad total del método esta dada por la operación mas costosa. 
 * Que en este caso es la cola de prioridad o Heap que como ya justificamos antes tiene una complejidad de O(log E).
*/


    public void consultarDarkWeb(Integer k, Examen examenDW){
        
        // Guarda los IDs de los estudiantes a modificar.
        ArrayList<Integer> peoresIDs = new ArrayList<>(); // O(1)
        
        for (int i = 0; i < k; i++) { // Itera k veces
            peoresIDs.add(this.noEntregados.sacar_minimo().id); // O(log E) costo del heap
        }
        // Costo del ciclo: O(k * log E)
        
        // Itera sobre esos IDs
        for(Integer id : peoresIDs) { // Itera k veces
            Estudiante estudiante = this.estudiantes.get(id); // O(1)
            Handle<PuntajeKey> handle = estudiante.handle; // Obtiene el handle del estudiante O(1)
            Examen examenCopiado = new Examen(examenDW.respuestas.size()); // O(1)
            examenCopiado.respuestas = new ArrayList<Integer>(examenDW.respuestas); // O(R)
            
            estudiante.setExamen(examenCopiado); // O(1)

            // Recalcula el puntaje
            calcularPuntaje(estudiante); // O(R)
            
            // Actualiza el puntaje DENTRO del handle
            handle.valor().cambiar_puntaje(estudiante.puntaje); // O(1)
            
            // Vuelve a AGREGAR el handle actualizado al heap
            this.noEntregados.agregar(handle); // O(log E)
        }
        // Costo del ciclo: O(k*(R + log E))
    }

/* Funcionamiento: 
Selección de peores: Extrae k veces la raíz del min-heap noEntregados, obteniendo a los estudiantes con menor puntaje, y desempata por menor ID.
Actualización: Para cada uno de esos k estudiantes, se reemplaza su examen completo por una copia del examenDW.
Recalculo y reinserción: Se computa el nuevo puntaje basado en el nuevo examen y se vuelven a insertar en el heap de no entregados.

Justificación de la complejidad: O(k * (R + log E))
Extraer k veces del heap cuesta O(k * log E)
Clonar el examen y recalcular el puntaje para cada uno cuesta O(R)
Reinsertar cuesta O(log E) para cada uno de los k estudiantes. */
    
    /* Justificación de la complejidad: El metodo esta hecho por 2 ciclos proporcionales a k, donde la complejidad total se da por la complejidad del segundo ciclo.
     * Por lo tanto la complejidad temporal total es: O(k*(R + log E)).
     */

    public Double[] notas(){
        Double[] notasFinales = new Double[this.estudiantes.size()]; // O(E)

        for(Integer i = 0; i < this.estudiantes.size(); i++){ // O(E)
            notasFinales[i] = this.estudiantes.get(i).puntaje; // O(1)
        }

        return notasFinales; 
    }

/* Funcionamiento:
Recorre secuencialmente el ArraylList donde se almacenan todos los estudiantes.
Extrae el valor del atributo puntaje y lo coloca en un nuevo arreglo.

Justificación de la complejidad: O(E) 
Es un único ciclo que itera exactamente E veces con operaciones de tiempo constante en su interior. */
    
    /* Justificación de la complejidad: El ciclo que itera sobre los E estudiantes es el factor dominante.
     * Como dentro del ciclo las operaciones son constantes, la complejidad final es: O(E)
     */
    
     public void entregar(Integer idEstudiante){
        Estudiante estudiante = this.estudiantes.get(idEstudiante);

        estudiante.entregado = true;
        this.noEntregados.eliminar(estudiante.handle); // O(log E)
    }

/* Funcionamiento: 
Marca el estado del estudiante como entregado = true
Elimina permanentemente al estudiante del heap noEntregados. Para esto, intercambia el nodo del estudiante con el ultimo nodo del heap y luego aplica bajar para restaurar el orden.

Justificación de la complejidad: O(log E)
Localizar el nodo con el handle es O(1)
Encontrar el ultimo nodo usando la ruta binaria del cardinal es O(log E)
El proceso bajar el nodo intercambiado recorre la altura del árbol: O(log E)
 */

// Justificacion de la complejidad: Por propiedad del heap, donde debe ordenarlo de vuelta cuando se remueve un nodo, cuesta O(log E) E siendo los estudiantes a reacomodar

    public Integer[] chequearCopias(){
        int E = this.estudiantes.size();
        int R = this.examenCanonico.respuestas.size();
        
        // Asumimos 10 opciones de respuesta (0-9)
        int OPCIONES = 10; 
        
        // Construir la matriz de frecuencias
        // Complejidad: O(E * R)
        Matriz frec = new Matriz(R, OPCIONES); // O(R * OPCIONES)
        
        for (int id = 0; id < E; id++) { // O(E)
            Estudiante s = this.estudiantes.get(id); 
            Examen ex = s.getExamen();
            
            for (int j = 0; j < R; j++) { // O(R)
                int respuesta = ex.respuesta(j);
                if (respuesta >= 0 && respuesta < OPCIONES) { // Asumo -1 es "en blanco"
                    frec.incrementar(j, respuesta); // O(1)
                }
            }
        }
        
        // Verificar estudiantes contra la matriz
        // Complejidad: O(E * R)
        ArrayList<Integer> listaSospechosos = new ArrayList<>();
        double umbral = 0.25 * E;

        for (int id = 0; id < E; id++) { // O(E)
            Estudiante s = this.estudiantes.get(id);
            Examen ex = s.getExamen();
            
            boolean esSospechoso = true; // Asumir que sí hasta que falle una respuesta
            int respuestasRespondidas = 0;
            
            for (int j = 0; j < R; j++) { // O(R)
                int respuesta = ex.respuesta(j);
                
                if (respuesta != -1) { // "sólo hay que considerar las respuestas..."
                    respuestasRespondidas++;
                    
                    int totalConEsaRespuesta = frec.get(j, respuesta); // O(1)
                    
                    // "sin contarlo a él/ella"
                    if ((totalConEsaRespuesta - 1) < (int) umbral) {
                        esSospechoso = false;
                        break; 
                    }
                }
            }
            
            if (respuestasRespondidas == 0) {
                esSospechoso = false;
            }
            
            // cumplir con el requisito 'inout'
            s.setSospechoso(esSospechoso);
            
            if (esSospechoso) {
                listaSospechosos.add(id); // Esto es O(1)
            }
        }
        // Convertir de ArrayList a Array (esto es O(E))
        Integer[] sospechosos = new Integer[listaSospechosos.size()];
        listaSospechosos.toArray(sospechosos);
        return sospechosos;
    }

/* Funcionamiento:
Fase de frecuencia: Crea una matriz donde cuenta cuantos estudiantes eligieron cada respuesta para cada ejercicio.
Fase de sospecha: Para cada estudiante verifica si todas sus respuestas respondidas coinciden con opciones que eligieron al menos el 25% de los demás alumnos.
Si un estudiante cumple la condición y tiene al menos una respuesta no vacia, se marca como sospechoso.

Justificación de la complejidad: O(E*R)
Llenar la matriz requiere recorrer E estudiantes y sus R respuestas
Verificar a cada estudiante requiere nuevamente recorrer sus R respuestas y consultar la matriz (O(1)), resultando en O(E*R) total */

    /* Justificación de la complejidad: En este metodo se aplicaron 2 ciclos anidados.
       Con ciclos anidados las complejidades se multiplican entre si.
       Por lo tanto la complejidad temporal final es de O(E * R).
    */

    public NotaFinal[] corregir(){
        HeapMaximos heapNotas = new HeapMaximos(); // O(1)

        // Llenar el heap O(E * log E)
        for(int i = 0; i < this.estudiantes.size(); i++){ // Itera la cantidad de estudiantes que hay, O(E)
            Estudiante estudiante = this.estudiantes.get(i);
            if (estudiante.sospechoso != true){
                Double nota = estudiante.puntaje;
                NotaFinal notaFinal = new NotaFinal(nota, i);
                Handle<NotaFinal> h = new Handle<>(notaFinal);
                heapNotas.agregar(h); // O(log E)
            }
        }

        // Extraer a una lista o directamente a un array.
        ListaEnlazada<NotaFinal> listaTemporalOrdenada = new ListaEnlazada<NotaFinal>();
        
        // (O(E log E))
        while (heapNotas.getCardinal() > 0){ // Se ejecuta E veces, E siendo todos los estudiantes de heapNotas
            NotaFinal notaOrdenada = heapNotas.sacar_maximo(); // O(log E) por el costo de reordenar los demas estudiantes
            listaTemporalOrdenada.agregarAtras(notaOrdenada); // O(1)
        }
        
        // Convertir a array en O(E)
        int tamano = listaTemporalOrdenada.longitud(); 
        NotaFinal[] notasFinales = new NotaFinal[tamano]; // O(1)

        ListaEnlazada<NotaFinal>.ListaIterador iterador = listaTemporalOrdenada.iterador();
        int i = 0;
        
        while(iterador.haySiguiente()){ //O(E)
            notasFinales[i] = iterador.siguiente();
            i++;
        }
        
        return notasFinales; // Complejidad total: O(E * log E)
}

/* Funcionamiento: 
Filtrado: Recorre todos los estudiantes y solo selecciona aquellos que no fueron marcados como sospechosos por chequearCopias.
Ordenamiento por prioridad: Inserta a los estudiantes validos en un heapMaximos. El criterio de comparación es: mayor nota primero y desempata por ID.
Recolección: Extrae sucesivamente la raíz del Max-Heap para obtener la lista ordenada de forma decreciente.

Justificación de la complejidad: O(E log E)
Insertar hasta E estudiantes en el heap cuesta O(E log E)
Extraer E elementos del heap cuesta O(E log E)
El traspaso final a un arreglo es O(E)
 */

/* Justificación de la complejidad: La complejidad de O(E * log E),
esta dada porque el ciclo donde se llena el heap tiene complejidad de O(E * log E) y el ciclo donde se vacia el heap que tiene complejidad O(E * log E).
La suma de estos 2 factores da como resultado la complejidad final de O(E * log E)
*/



//Auxiliares


    private void calcularPuntaje(Estudiante estudiante){
        Examen examenEstudiante = estudiante.examen;
        double contadorCorrectas = 0.0;
        
        // Obtenemos el número total de preguntas (R) O(1)
        int R = this.examenCanonico.respuestas.size();

        // Iteramos por el indice (número de ejercicio) de 0 a R-1 O(R)
        for (int nroEjercicio = 0; nroEjercicio < R; nroEjercicio++) {
            
            // Pasamos el numero de ejercicio (nroEjercicio)
            if(esCorrecta(this.examenCanonico, examenEstudiante, nroEjercicio)) {
                contadorCorrectas++;
            }
        }
        estudiante.puntaje = (R == 0) ? 0.0 : (contadorCorrectas / R) * 100.0; // O(1)
    }

/* Justificación de la complejidad: La complejidad de O(R),
esta dada porque el ciclo que necesita iterar R veces sobre el exámen, para ir por todas las respuestas.
El resto son operaciones elementales, como acceder al tamaño del arreglo o hacer la operación aritmética.
*/
    public Boolean esCorrecta(Examen examenCanonico, Examen examen, Integer nroEjercicio){
        if(examenCanonico.respuesta(nroEjercicio).equals(examen.respuesta(nroEjercicio))){
            return true;
        }
        return false;
    }
/* Justificación de la complejidad: La complejidad es O(1),
ya que se accede a un elemento del ArrayList en O(1), y luego se lo compara con .equals(). Como son Integer
la comparación también es O(1)
*/
    
    public ArrayList<Estudiante> vecinos(Integer idEstudiante, Integer dimension) {
        ArrayList<Estudiante> vecinos = new ArrayList<>();
        
        // Obtenemos los datos del estudiante actual en O(1)
        Estudiante estudianteActual = this.estudiantes.get(idEstudiante);
        Posicion posActual = estudianteActual.getPosicion(); 
        int filaActual = posActual.fila;
        int totalEstudiantes = this.estudiantes.size();

        // Vecino de la IZQUIERDA
        // El vecino de la izquierda es (idEstudiante - 1)
        int idVecinoIzquierda = idEstudiante - 1;

        // Verificamos que el ID exista (>= 0) y que esté en la misma fila.
        // Si no está en la misma fila, es el último de la fila de arriba.
        if (idVecinoIzquierda >= 0 && this.estudiantes.get(idVecinoIzquierda).getPosicion().fila == filaActual) {
            vecinos.add(this.estudiantes.get(idVecinoIzquierda));
        }

        // Vecino de la DERECHA
        // El vecino de la derecha es (idEstudiante + 1)
        int idVecinoDerecha = idEstudiante + 1;

        // Verificamos que el ID exista (< total) y que esté en la misma fila.
        // Si no está en la misma fila, es el primero de la fila de abajo.
        if (idVecinoDerecha < totalEstudiantes && this.estudiantes.get(idVecinoDerecha).getPosicion().fila == filaActual) {
            vecinos.add(this.estudiantes.get(idVecinoDerecha));
        }

        // Vecino de ARRIBA
        int asientosPorFila = (dimension + 1) / 2;
        int idVecinoArriba = idEstudiante - asientosPorFila;
        if (idVecinoArriba >= 0) {
            vecinos.add(this.estudiantes.get(idVecinoArriba));
        }

        return vecinos;
    }

/* Justificación de la complejidad: La complejidad es O(1),
ya que todas las operaciones son, acceder a elementos de Estudiantes, comparaciones o igualdedes de Integers y operaciones aritméticas, todas en O(1)
*/

    public RespuestaACopiar compararExamenes(Examen estudiante, Examen vecino) {
        int totalDiferencias = 0;
        int primerNroEjercicio = -1; // -1 significa "aún no encontrado"
        int primeraRespuesta = -1;
        
        // R es el número total de respuestas del examen
        int R = this.examenCanonico.respuestas.size(); // O(1)

        for (int i = 0; i < R; i++) { // O()
            int resEst = estudiante.respuesta(i); // Respuesta del estudiante
            int resVec = vecino.respuesta(i);   // Respuesta del vecino

            // Comprobamos la condición: el estudiante no la tiene y el vecino sí
            if (resEst == -1 && resVec != -1) {
                totalDiferencias++;
                if (primerNroEjercicio == -1) {
                    primerNroEjercicio = i;
                    primeraRespuesta = resVec;
                }
            }
        }
        
        return new RespuestaACopiar(totalDiferencias, primerNroEjercicio, primeraRespuesta);
    }

    /* Justificación de la complejidad: La complejidad es O(R), esta se debe al ciclo que itera sobre los Examenes. 
    El resto de las operaciones son O(1)
*/

}

/*
Diferencias con la implementacion con ArrayList: 
Principal diferencia de los heaps con referencias es que en vez de usar operaciones aritmeticas para calcular los hijos y el padre, se guardan punteros explicitos.
El handle es un objeto nodo mientras que en un heap con arraylist se guarda con un indice, 
que cuando se intercambian los valores para eliminar tambien se deben actualizar los indices.
Con nodos es un codigo mas complejo porque se gestionan manualmente los enlaces, y que ocupa mas espacio en memoria por las referencias al padre y los hijos 
*/

