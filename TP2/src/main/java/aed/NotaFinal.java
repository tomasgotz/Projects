package aed; 

public class NotaFinal implements Comparable<NotaFinal>{
    Double nota;
    Integer id;
    public NotaFinal(Double nota, Integer id){
        this.nota = nota;
        this.id = id;
    }

    public Double getNota(){
        return this.nota;
    }
    
    public Integer getId(){
        return this.id;
    }

    public int compareTo(NotaFinal otra){
        if (otra.id != this.id){
            return this.id - otra.id;
        }
        return Double.compare(this.nota, otra.nota);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotaFinal nf = (NotaFinal) o;

        return Double.compare(nf.nota, nota) == 0 &&
            id == nf.id;
    }
}