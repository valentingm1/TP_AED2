package aed.tp2;

import java.util.Comparator;

// l maximo esta en la posicion 0.
public final class MaxHeap<T> {
    
    public static final class Referencia<T> implements Handle<T> {
        private T valor;
        private final MaxHeap<T> propietario;
        // -1 indica que el handle no esta activo.
        private int indice = -1;
        private Referencia(T valor, MaxHeap<T> propietario) {
            this.valor = valor; this.propietario = propietario;
        }
        public T valor() { return valor; }
        // Reordena tras cambiar el valor o su prioridad
        public void modificar(T valor) {
            propietario.validar(this);
            this.valor = valor;
            propietario.actualizar(this);
        }
        public void eliminar() { propietario.eliminar(this); }
        // Comparamos valores
        public boolean equals(Handle<T> otro) {
            return otro != null && java.util.Objects.equals(valor, otro.valor());
        }
    }
    private final Comparator<? super T> comparador;
    // Ocupados: [0, cantidad). La capacidad es elementos.length
    private Referencia<T>[] elementos;
    private int cantidad;
    @SuppressWarnings("unchecked")
    public MaxHeap(Comparator<? super T> comparador) {
        this.comparador = comparador;
        // Java no permite crear directamente un array de Referencia<T>
        elementos = (Referencia<T>[]) new Referencia<?>[8];
    }
    @SuppressWarnings("unchecked")
    // Duplica capacidad en O(U) conservando los indices
    private void ampliar() {
        Referencia<T>[] nuevo = (Referencia<T>[]) new Referencia<?>[elementos.length * 2];
        for (int i = 0; i < cantidad; i++) nuevo[i] = elementos[i];
        elementos = nuevo;
    }
    public int tamanio() { return cantidad; }
    // O(log U) amortizado; O(U) si amplia el array
    public Referencia<T> insertar(T valor) {
        if (cantidad == elementos.length) ampliar();
        Referencia<T> h = new Referencia<>(valor, this);
        h.indice = cantidad;
        elementos[cantidad++] = h;
        subir(h.indice);
        return h;
    }
    // Extrae la raiz en O(log U) e invalida su handle
    public T extraerMaximo() {
        if (cantidad == 0) throw new IllegalStateException("Heap vacio");
        Referencia<T> h = elementos[0];
        eliminar(h);
        return h.valor;
    }
    // Verifica pertenencia y posicion vigente en O(1).
    private void validar(Referencia<T> h) {
        if (h == null || h.propietario != this || h.indice < 0
                || h.indice >= cantidad || elementos[h.indice] != h)
            throw new IllegalArgumentException("Handle invalido");
    }
    private void eliminar(Referencia<T> h) {
        validar(h);
        int i = h.indice;
        // Reemplaza la entrada por la ultima para no dejar huecos
        intercambiar(i, cantidad - 1);
        // Conserva capacidad para reinsertar el ranking sin ampliar
        elementos[--cantidad] = null;
        h.indice = -1;
        if (i < cantidad) actualizar(elementos[i]);
    }
    // Reordena en O(log U); solo una prioridad debe haber cambiado
    public void actualizar(Referencia<T> h) {
        validar(h);
        int i = h.indice;
        if (i > 0 && mayor(i, (i - 1) / 2)) subir(i);
        else bajar(i);
    }
    private boolean mayor(int a, int b) {
        return comparador.compare(elementos[a].valor, elementos[b].valor) > 0;
    }
    private void intercambiar(int a, int b) {
        Referencia<T> aux = elementos[a]; elementos[a] = elementos[b]; elementos[b] = aux;
        // Mantiene los handles sincronizados con el array
        elementos[a].indice = a; elementos[b].indice = b;
    }
    // Sube mientras supere al padre
    private void subir(int i) {
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (!mayor(i, padre)) return;
            intercambiar(i, padre); i = padre;
        }
    }
    // Baja intercambiando con el hijo de mayor prioridad
    private void bajar(int i) {
        while (i < cantidad / 2) {
            // Hijos: 2*i+1 y 2*i+2
            int hijo = 2 * i + 1;
            if (hijo + 1 < cantidad && mayor(hijo + 1, hijo)) hijo++;
            if (!mayor(hijo, i)) return;
            intercambiar(i, hijo); i = hijo;
        }
    }
}
