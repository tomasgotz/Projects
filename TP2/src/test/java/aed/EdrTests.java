package aed;

// Este es el nuestro

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;


class EdrTests {
    Edr edr;
    Integer d_aula;
    Integer cant_alumnos;
    Examen sol;

    @BeforeEach
    void setUp(){
        d_aula = 5;
        cant_alumnos = 4;
        sol = new Examen(10);
        sol.respuestas.set(0, 0);
        sol.respuestas.set(1, 1);
        sol.respuestas.set(2, 2);
        sol.respuestas.set(3, 3);
        sol.respuestas.set(4, 4);
        sol.respuestas.set(5, 5);
        sol.respuestas.set(6, 6);
        sol.respuestas.set(7, 7);
        sol.respuestas.set(8, 8);
        sol.respuestas.set(9, 9);

        edr = new Edr(d_aula, cant_alumnos, sol);
    }

    @Test
    void nuevo_edr() {
        Double[] notas = edr.notas();
        Double[] notas_esperadas = new Double[]{0.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }
    
    @Test
    void los_alumnos_resuelven_un_problema() {
        Double[] notas;
        Double[] notas_esperadas;

        edr.resolver(0, 0, 0);
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.resolver(1, 0, 2);
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.resolver(2, 4, 4);
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 0.0, 10.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.resolver(3, 9, 8);
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 0.0, 10.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    @Test
    void los_alumnos_resuelven_varios_problemas() {
        Double[] notas;
        Double[] notas_esperadas;

        for(Integer pregunta = 0; pregunta < 5; pregunta++){
            edr.resolver(0, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new Double[]{10.0*(pregunta+1), 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }

        for(Integer pregunta = 9; pregunta > 2; pregunta--){
            edr.resolver(1, pregunta, 9-pregunta);
            notas = edr.notas();
            notas_esperadas = new Double[]{50.0, 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }

        for(Integer pregunta = 0; pregunta < 10; pregunta+=2){
            edr.resolver(2, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new Double[]{50.0, 0.0, 10.0*(pregunta/2+1), 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }

        Double nota_3 = 0.0;
        for(Integer pregunta = 0; pregunta < 10; pregunta++){
            //si la pregunta es par, responde bien, si no es par responde mal.
            Integer respuesta = pregunta % 2 == 0 ? pregunta : 9 - pregunta;
            nota_3 += 10.0 * ((pregunta+1)%2);
            edr.resolver(3, pregunta, respuesta);
            notas = edr.notas();
            notas_esperadas = new Double[]{50.0, 0.0, 50.0, nota_3};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }

        for(Integer pregunta = 5; pregunta < 10; pregunta++){
            edr.resolver(0, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new Double[]{10.0*(pregunta+1), 0.0, 50.0, 50.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
    }

    @Test
    void revisar_copias_examenes_completos() {
        Double[] notas;
        Double[] notas_esperadas;

        for(Integer pregunta = 0; pregunta < 10; pregunta++){
            edr.resolver(0, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new Double[]{10.0*(pregunta+1), 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(0);

        for(Integer pregunta = 9; pregunta > -1; pregunta--){
            edr.resolver(1, pregunta, 9-pregunta);
            notas = edr.notas();
            notas_esperadas = new Double[]{100.0, 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(1);

        Double nota_2 = 0.0;
        for(Integer pregunta = 0; pregunta < 10; pregunta++){
            //solo se responden bien las preguntas pares
            edr.resolver(2, pregunta, (pregunta % 2 == 0 ? pregunta : 0));
            nota_2 += pregunta % 2 == 0 ? 10.0 : 0.0;
            notas = edr.notas();
            notas_esperadas = new Double[]{100.0, 0.0, nota_2, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(2);

        Double nota_3 = 0.0;
        for(Integer pregunta = 0; pregunta < 10; pregunta++){
            //si la pregunta es par, responde bien, si no es par responde mal.
            Integer respuesta = pregunta % 2 == 0 ? pregunta : 9 - pregunta;
            nota_3 += 10.0 * ((pregunta+1)%2);
            edr.resolver(3, pregunta, respuesta);
            notas = edr.notas();
            notas_esperadas = new Double[]{100.0, 0.0, 50.0, nota_3};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(3);

        Integer[] copiones = edr.chequearCopias(); 
        Integer[] copiones_esperados = new Integer[]{3};
        assertTrue(Arrays.equals(copiones_esperados, copiones));
        
    }

    @Test
    void revisar_copias_examenes_incompletos() {
        Double[] notas;
        Double[] notas_esperadas;

        for(Integer pregunta = 0; pregunta < 3; pregunta++){
            edr.resolver(0, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new Double[]{10.0*(pregunta+1), 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(0);

        for(Integer pregunta = 9; pregunta > 8; pregunta--){
            edr.resolver(1, pregunta, 9-pregunta);
            notas = edr.notas();
            notas_esperadas = new Double[]{30.0, 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(1);

        edr.resolver(2, 9, 9);
        notas = edr.notas();
        notas_esperadas = new Double[]{30.0, 0.0, 10.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        edr.entregar(2);

        for(Integer pregunta = 0; pregunta < 5; pregunta++){
            edr.resolver(3, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new Double[]{30.0, 0.0, 10.0, 10.0*(pregunta+1)};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(3);

        Integer[] copiones = edr.chequearCopias(); 
        Integer[] copiones_esperados = new Integer[]{0};
        assertTrue(Arrays.equals(copiones_esperados, copiones));
        
    }

    @Test
    void no_hay_copiones() {
        Double[] notas;
        Double[] notas_esperadas;

        for(Integer pregunta = 0; pregunta < 10; pregunta++){
            edr.resolver(0, pregunta, 0);
            edr.resolver(1, pregunta, 1);
            edr.resolver(2, pregunta, 2);
            edr.resolver(3, pregunta, 3);
        }
        for(Integer alumno = 0; alumno < 4; alumno++){
            edr.entregar(alumno);
        }
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 10.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        Integer[] copiones = edr.chequearCopias();
        Integer[] copiones_esperados = new Integer[]{};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(10.0, 3),
            new NotaFinal(10.0, 2),
            new NotaFinal(10.0, 1),
            new NotaFinal(10.0, 0)
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));

    }

    @Test
    void todos_copiones() {    //cambiar este test
        Double[] notas;
        Double[] notas_esperadas;

        for(Integer pregunta = 0; pregunta < 3; pregunta+=2){
            edr.resolver(0, pregunta, 5);
            edr.resolver(1, pregunta, 5);
            edr.resolver(2, pregunta, 5);
            edr.resolver(3, pregunta, 5);
        }

        for(Integer pregunta = 5; pregunta < 8; pregunta+=2){
            edr.resolver(0, pregunta, 5);
            edr.resolver(1, pregunta, 5);
            edr.resolver(2, pregunta, 5);
            edr.resolver(3, pregunta, 5);
        }

        for(Integer alumno = 0; alumno < 4; alumno++){
            edr.entregar(alumno);
        }

        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 10.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        Integer[] copiones = edr.chequearCopias();
        Integer[] copiones_esperados = new Integer[]{0,1,2,3};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{};

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));

    }

    @Test
    void copias_de_exacto_25_porciento() {

        Edr edr_9 = new Edr(d_aula, 9, sol);
        Double[] notas;
        Double[] notas_esperadas;

        for(Integer pregunta = 0; pregunta < 3; pregunta++){
            edr_9.resolver(0, pregunta, 5);
            edr_9.resolver(1, pregunta, 5);
            edr_9.resolver(2, pregunta, 5);
        }

        for(Integer pregunta = 5; pregunta < 9; pregunta++){
            edr_9.resolver(3, pregunta, pregunta);
            edr_9.resolver(4, pregunta, pregunta);
            edr_9.resolver(5, pregunta, pregunta);
        }
        edr_9.resolver(3, 9, 9);

        for(Integer alumno = 0; alumno < 9; alumno++){
            edr_9.entregar(alumno);
        }

        notas = edr_9.notas();
        notas_esperadas = new Double[]{0.0, 0.0, 0.0, 50.0, 40.0, 40.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        Integer[] copiones = edr_9.chequearCopias();
        Integer[] copiones_esperados = new Integer[]{0,1,2,4,5};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = edr_9.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(50.0, 3),
            new NotaFinal(0.0, 8),
            new NotaFinal(0.0, 7),
            new NotaFinal(0.0, 6)
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));

    }

    @Test
    void alumnos_se_copian_una_vez(){

        edr = new Edr(7, cant_alumnos, sol);
        Double[] notas;
        Double[] notas_esperadas;

        edr.resolver(0, 0, 0);
        edr.resolver(1, 1, 1);
        edr.resolver(2, 2, 2);
        edr.resolver(3, 3, 3);

        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 10.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.copiarse(2);
        
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 10.0, 20.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.copiarse(3);
        
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 10.0, 20.0, 20.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        for(Integer alumno = 0; alumno < 4; alumno++){
            edr.entregar(alumno);
        }

        Integer[] copiones = edr.chequearCopias();
        Integer[] copiones_esperados = new Integer[]{2,3};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(10.0, 1),
            new NotaFinal(10.0, 0)
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));

    }

    @Test
    void alumnos_se_copian_mas_de_una_vez(){

        edr = new Edr(7, cant_alumnos, sol);
        Double[] notas;
        Double[] notas_esperadas;

        edr.resolver(0, 0, 0);
        edr.resolver(1, 1, 1);
        edr.resolver(2, 2, 2);
        edr.resolver(3, 3, 3);

        
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 10.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.copiarse(1);
        
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 20.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.copiarse(1);
        
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 30.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.copiarse(2);
        
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 30.0, 20.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.copiarse(2);
        
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 30.0, 30.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.copiarse(3);
        
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 30.0, 30.0, 20.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.resolver(0, 4, 0);
        edr.resolver(1, 5, 1);

        edr.copiarse(2);
        
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 30.0, 40.0, 20.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.copiarse(3);
        
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 30.0, 40.0, 30.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));


        for(Integer alumno = 0; alumno < 4; alumno++){
            edr.entregar(alumno);
        }

        Integer[] copiones = edr.chequearCopias();
        Integer[] copiones_esperados = new Integer[]{2,3};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(30.0, 1),
            new NotaFinal(10.0, 0)
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    @Test 
    void un_alumno_se_copia_de_la_darkweb(){
        Double[] notas;
        Double[] notas_esperadas;
        //todes resuelven bien un ejercicio excepto el estudiante 0
        for(Integer estudiante = 1; estudiante < 4; estudiante++){
            for(Integer pregunta = 0; pregunta <= estudiante; pregunta++){
                edr.resolver(estudiante, pregunta, estudiante);
            }
        }

        //alguien sube la solución con acceso para una persona, debe acceder el alumno 0
        edr.consultarDarkWeb(1, sol);

        notas = edr.notas();
        notas_esperadas = new Double[]{100.0, 10.0, 10.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        for(Integer estudiante = 0; estudiante < 4; estudiante++){
            edr.entregar(estudiante);
        }
        Integer[] copiones = edr.chequearCopias();
        Integer[] copiones_esperados = new Integer[]{};

        assertTrue(Arrays.equals(copiones, copiones_esperados));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(100.0, 0),
            new NotaFinal(10.0, 3),
            new NotaFinal(10.0, 2),
            new NotaFinal(10.0, 1),
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    @Test 
    void varios_alumnos_se_copian_de_la_darkweb(){
        Double[] notas;
        Double[] notas_esperadas;
        //todes resuelven bien un ejercicio excepto el estudiante 0
        for(Integer estudiante = 1; estudiante < 4; estudiante++){
            edr.resolver(estudiante, estudiante, estudiante);
        }

        //alguien sube la solución con acceso para una persona, debe acceder el alumno 0
        edr.consultarDarkWeb(3, sol);

        notas = edr.notas();
        notas_esperadas = new Double[]{100.0, 100.0, 100.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        for(Integer estudiante = 0; estudiante < 4; estudiante++){
            edr.entregar(estudiante);
        }
        Integer[] copiones = edr.chequearCopias();
        Integer[] copiones_esperados = new Integer[]{0,1,2,3};

        assertTrue(Arrays.equals(copiones, copiones_esperados));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{};

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    @Test
    void darkweb_no_incluye_estudiantes_que_entregaron() {
        Examen sol5 = new Examen(5);
        sol5.respuestas.set(0, 0);
        sol5.respuestas.set(1, 1);
        sol5.respuestas.set(2, 2);
        sol5.respuestas.set(3, 3);
        sol5.respuestas.set(4, 4);
        Edr edr = new Edr(5, 5, sol5);
        
        // Estudiante 0 entrega primero (con peor nota)
        edr.entregar(0);
        
        // Estudiantes 1-4 tienen notas bajas
        edr.resolver(1, 0, 0);
        edr.resolver(2, 1, 1);
        edr.resolver(3, 2, 2);
        edr.resolver(4, 3, 3);
        
        edr.consultarDarkWeb(2, sol5);
        
        Double[] notas = edr.notas();
        Double[] notas_esperadas = new Double[]{0.0, 100.0, 100.0, 20.0, 20.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    @Test
    void varios_alumnos_se_copian_de_varios_examenes(){
        Double[] notas;
        Double[] notas_esperadas;
        Edr edr_8 = new Edr(d_aula, 8, sol);
        Integer[] resolucion_1 = new Integer[]{9,8,7,6,5,4,3,2,1,0};
        Integer[] resolucion_2 = new Integer[]{2,2,2,2,2,2,2,2,2,2};
        Integer[] resolucion_3 = new Integer[]{0,0,2,2,5,6,7,0,0,9};


        ArrayList<Integer> lista1 = new ArrayList<>();
        ArrayList<Integer> lista2 = new ArrayList<>();
        ArrayList<Integer> lista3 = new ArrayList<>();
        
        for (Integer x : resolucion_1) lista1.add(x);
        for (Integer x : resolucion_2) lista2.add(x);
        for (Integer x : resolucion_3) lista3.add(x);

        Examen examen1 = new Examen(0);
        examen1.respuestas = lista1;

        Examen examen2 = new Examen(0);
        examen2.respuestas = lista2;

        Examen examen3 = new Examen(0);
        examen3.respuestas = lista3;



        for(Integer capa = 7; capa >= 0; capa--){
            for(Integer estudiante = 0; estudiante <= capa; estudiante++){
                edr_8.resolver(estudiante, capa, capa);
            }
        }

        notas = edr_8.notas();
        notas_esperadas = new Double[]{80.0, 70.0, 60.0, 50.0, 40.0, 30.0, 20.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        edr_8.consultarDarkWeb(3, examen1);

        notas = edr_8.notas();
        notas_esperadas = new Double[]{80.0, 70.0, 60.0, 50.0, 40.0, 0.0, 0.0, 0.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        edr_8.consultarDarkWeb(5, examen2);

        notas = edr_8.notas();
        notas_esperadas = new Double[]{80.0, 70.0, 60.0, 10.0, 10.0, 10.0, 10.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        edr_8.consultarDarkWeb(2, examen3);

        notas = edr_8.notas();
        notas_esperadas = new Double[]{80.0, 70.0, 60.0, 30.0, 30.0, 10.0, 10.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        for(Integer estudiante = 0; estudiante < 8; estudiante++){
            edr_8.entregar(estudiante);
        }
        Integer[] copiones = edr_8.chequearCopias();
        Integer[] copiones_esperados = new Integer[]{2,5,6,7};

        assertTrue(Arrays.equals(copiones, copiones_esperados));

        NotaFinal[] notas_finales = edr_8.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(80.0, 0),
            new NotaFinal(70.0, 1),
            new NotaFinal(30.0, 4),
            new NotaFinal(30.0, 3),
            
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    @Test 
    void no_hay_aliasing_con_los_examenes_subidos(){
        Double[] notas;
        Double[] notas_esperadas;
        Integer[] resolucion_dark = new Integer[]{0,0,2,2,5,6,7,0,0,9};

        ArrayList<Integer> listaDark = new ArrayList<>();

        for (Integer x : resolucion_dark) {
            listaDark.add(x);
        }

        Examen examenDark = new Examen(0);

        examenDark.respuestas = listaDark;

        //todes resuelven bien un ejercicio excepto el estudiante 0
        for(Integer estudiante = 1; estudiante < 4; estudiante++){
            edr.resolver(estudiante, estudiante, estudiante);
        }

        //alguien sube la solución con acceso para una persona, debe acceder el alumno 0
        edr.consultarDarkWeb(1, examenDark);

        notas = edr.notas();
        notas_esperadas = new Double[]{30.0, 10.0, 10.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        //al cambiar la resolución, no debería cambiar la nota ni el resultado del examen
        resolucion_dark[1] = 1;
        resolucion_dark[3] = 3;

        notas = edr.notas();
        Double[] notas_erroneas = new Double[]{50.0, 10.0, 10.0, 10.0};
        
        assertFalse(Arrays.equals(notas, notas_erroneas));

        for(Integer estudiante = 0; estudiante < 4; estudiante++){
            edr.entregar(estudiante);
        }

        Integer[] copiones = edr.chequearCopias();
        Integer[] copiones_esperados = new Integer[]{2};

        assertTrue(Arrays.equals(copiones, copiones_esperados));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(30.0, 0),
            new NotaFinal(10.0, 3),
            new NotaFinal(10.0, 1),
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    //Tests propios

    @Test
    void copiarse_de_enfrente() {
        Double[] notas;
        Double[] notas_esperadas;
        edr.resolver(0, 5, 5);
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        edr.copiarse(3);
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 0.0, 0.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    
    //   Se verifica que si un alumno cambia una respuesta correcta por una incorrecta,
    //   su puntaje disminuye como corresponde.
    @Test
    void resolverPierdePuntos() {
        Double[] notas;
        Double[] notas_esperadas;

        // El Estudiante 1 responde la pregunta 0 correctamente.
        edr.resolver(1, 0, 0);
        notas = edr.notas();
        notas_esperadas = new Double[]{0.0, 10.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        // El Estudiante 1 cambia su respuesta por una incorrecta.
        edr.resolver(1, 0, 9); // La respuesta correcta es 0
        notas = edr.notas();
        notas_esperadas = new Double[]{0.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        // El Estudiante 1 la vuelve a contestar bien.
        edr.resolver(1, 0, 0);
        notas = edr.notas();
        notas_esperadas = new Double[]{0.0, 10.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    //  Se crea un escenario donde el Estudiante 1 tiene dos vecinos (0 y 2)
    //  que le ofrecen la misma cantidad de respuestas.
    //  Debe elegir copiarse del Estudiante 2 (ID 2 > ID 0).
    @Test
    void copiarseDesempateIdMayor() {
        Double[] notas;
        Double[] notas_esperadas;
        
        // Configuración del escenario:
        // Estudiante 0 (vecino 1) responde la pregunta 1 (incorrectamente).
        edr.resolver(0, 1, 9); 
        // Estudiante 2 (vecino 2) responde la pregunta 2 (correctamente).
        edr.resolver(2, 2, 2);

        // Notas antes de la copia: {0.0, 0.0, 10.0, 0.0}
        notas = edr.notas();
        notas_esperadas = new Double[]{0.0, 0.0, 10.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        // El Estudiante 1 se copia.
        // Ambos vecinos (0 y 2) tienen 1 respuesta que él no tiene.
        // Debe desempatar por ID mayor y copiar al Estudiante 2.
        edr.copiarse(1);

        // Verificación:
        // El Estudiante 1 debe haber copiado la respuesta del Estudiante 2 (la preg 2).
        // Por lo tanto, su nota debe ser 10.0.
        notas = edr.notas();
        notas_esperadas = new Double[]{0.0, 10.0, 10.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    //  El enunciado dice: "desempata por menor id de estudiante".
    //  Se crea un empate de notas bajas y se verifica que 'consultarDarkWeb'
    //  elija a los estudiantes con los IDs más bajos.
    @Test
    void consultarDarkWebDesempateIdMenor() {
        Double[] notas;
        Double[] notas_esperadas;

        // Todos los estudiantes (0-3) tienen 10.0 de nota.
        edr.resolver(0, 0, 0);
        edr.resolver(1, 1, 1);
        edr.resolver(2, 2, 2);
        edr.resolver(3, 3, 3);
        
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 10.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        // Llama a consultarDarkWeb para k=2.
        //  Debe elegir a los 2 peores. En el empate, a los de menor ID.
        //  Debe elegir al Estudiante 0 y al Estudiante 1.
        edr.consultarDarkWeb(2, sol);

        // Verificación:
        // Los estudiantes 0 y 1 ahora deben tener 100.0.
        // Los estudiantes 2 y 3 deben seguir con 10.0.
        notas = edr.notas();
        notas_esperadas = new Double[]{100.0, 100.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    
    //  Se verifica la condición de guarda que impide que un estudiante
    //  que ya entregó su examen pueda modificarlo.
    @Test
    void copiarseDespuesDeEntregarNoHaceNada() {
        Double[] notas;
        Double[] notas_esperadas;

        // El Estudiante 0 resuelve una pregunta.
        edr.resolver(0, 1, 1);
        
        // El Estudiante 1 (el que va a intentar copiar) entrega su examen.
        edr.entregar(1);

        // Notas actuales: E0=10.0, E1=0.0 (entregado)
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        // El Estudiante 1 intenta copiarse (estando ya entregado).
        edr.copiarse(1);

        // Verificación:
        // La nota del Estudiante 1 no debe haber cambiado.
        notas = edr.notas();
        notas_esperadas = new Double[]{10.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    
    //  Se prueba que un estudiante NO es sospechoso si al menos una de sus
    //  respuestas es ÚNICA (total = 1).
    //  También se prueba que estudiantes con 0 respuestas no son sospechosos.
    @Test
    void chequearCopiasNadieEsSospechoso() {
        // Configuración del escenario con E=5 y R=10
        Edr edr5 = new Edr(5, 5, sol); 

        // E0: Responde P1 (compartida) y P2 (única)
        edr5.resolver(0, 1, 1); 
        edr5.resolver(0, 2, 2); 

        // E1: Responde P1 (compartida) y P3 (única)
        edr5.resolver(1, 1, 1); 
        edr5.resolver(1, 3, 3); 

        // E2: Responde P4 (única)
        edr5.resolver(2, 4, 4); 

        // E3 y E4: No responden (0 respuestas)
        
        // Entregar todos los exámenes
        for(Integer i = 0; i < 5; i++) {
            edr5.entregar(i);
        }
        
        // Verificación: El array de copiones debe ser vacío
        Integer[] copiones = edr5.chequearCopias(); 
        Integer[] copiones_esperados = new Integer[]{};
        assertTrue(Arrays.equals(copiones_esperados, copiones));
    }

}