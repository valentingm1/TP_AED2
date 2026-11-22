package aed.tp2;

import java.util.Comparator;


public class MaxHeap<T> {

    public class Referencia implements Handle<T> {
        private T valor;
        // -1 indica que el handle no está activo.
        private int indice = -1;
        private Referencia(T valor) {
            this.valor = valor;
        }
        public T valor() { return valor; }
        // Reordena tras cambiar el valor o su prioridad
        public void modificar(T nuevoValor) {
            this.valor = nuevoValor;
            MaxHeap.this.actualizar(this);
        }
        public void eliminar() { MaxHeap.this.eliminar(this); }
        // Comparamos valores
        public boolean equals(Handle<T> otro) {
            return otro != null && java.util.Objects.equals(valor, otro.valor());
        }
    }

    private final Comparator<T> comparador;

    private Referencia[] elementos;
    private int cantidad;

    //Probablemente haya que cambiarlo con un compareTo, todavía no estoy seguro.
    // Creo que quiere que inicie vacío el heap, ahora está el criterio de comparación en la función que crea el heap
    public MaxHeap(Comparator<T> comparador) {
        this.comparador = comparador;
        elementos = new MaxHeap.Referencia[8];
    }
    //Lo casteo porque si no me tira error de array genérico/si pongo MaxHeap<> sin nada entre los <> me tira illegal start of type
    private void ampliar() {
        Referencia[] nuevo =
                (Referencia[]) new MaxHeap<?>.Referencia[elementos.length * 2];
        for (int i = 0; i < cantidad; i++) nuevo[i] = elementos[i];
        elementos = nuevo;
    }


    public int tamanio() { return cantidad; }


    public Referencia insertar(T valor) {
        if (cantidad == elementos.length) ampliar();
        Referencia h = new Referencia(valor);
        h.indice = cantidad;
        elementos[cantidad++] = h;
        subir(h.indice);
        return h;
    }
    
    public T extraerMaximo() {
        if (cantidad == 0) throw new IllegalStateException("Heap vacio");
        Referencia h = elementos[0];
        eliminar(h);
        return h.valor;
    }

    // Lo uso para debuggear, después lo borro
    public T verMaximo() {
        if (cantidad == 0) throw new IllegalStateException("Heap vacio");
        return (T) elementos[0].valor;
    }

    private void eliminar(Referencia referencia) {
        int i = referencia.indice;
        intercambiar(i, cantidad - 1);
        elementos[--cantidad] = null;
        referencia.indice = -1;
        if (i < cantidad) actualizar(elementos[i]);
    }


    public void actualizar(Referencia referencia) {
        int i = referencia.indice;
        if (i > 0 && mayor(i, (i - 1) / 2)) subir(i);
        else bajar(i);
    }
    private boolean mayor(int a, int b) {
        return comparador.compare( elementos[a].valor, elementos[b].valor) > 0;
    }
    private void intercambiar(int a, int b) {
        Referencia aux = elementos[a]; elementos[a] = elementos[b]; elementos[b] = aux;
        elementos[a].indice = a; elementos[b].indice = b;
    }

    private void subir(int i) {
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (!mayor(i, padre)) return;
            intercambiar(i, padre); i = padre;
        }
    }

    private void bajar(int i) {
        while (i < cantidad / 2) {
            int hijo = 2 * i + 1;
            if (hijo + 1 < cantidad && mayor(hijo + 1, hijo)) hijo++;
            if (!mayor(hijo, i)) return;
            intercambiar(i, hijo); i = hijo;
        }
    }


}
