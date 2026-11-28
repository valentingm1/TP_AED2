package aed.tp2;

public class Usuario implements Comparable<Usuario>{
    private int identificador;
    private double millasAcumuladas;
    private double millasCanjeadas;
    private double millasTransferidas;
    private Usuario exclusividad; //Si es null, no hubo transferencias. Si apunta a otro usuario, solo hizo transferencias con ese. Si apunta a sí mismo, hizo transferencias con más de uno

    Usuario (int data){
            this.identificador = data;
            this.millasAcumuladas = 0;
            this.millasCanjeadas = 0;
            this.millasTransferidas = 0;
            this.exclusividad = null;
        }    

    public int devolverIdentificador() {
        return this.identificador;
    }

    public double devolverMillasAcumuladas() {
        return this.millasAcumuladas;
    }

    public double devolverMillasCanjeadas() {
        return this.millasCanjeadas;
    }

    public double devolverMillasTransferidas() {
        return this.millasTransferidas;
    }

    public Usuario devolverExclusividad() {
        return this.exclusividad;
    }

    public void sumarMillasAcumuladas(double millas) {
        this.millasAcumuladas = this.millasAcumuladas + millas;
    }

    public void sumarMillasCanjeadas(double millas) {
        this.millasCanjeadas = this.millasCanjeadas + millas;
    }

    public void sumarMillasTransferidas(double millas) {
        this.millasTransferidas = this.millasTransferidas + millas;
    }

    public void exclusivo(Usuario otro){
        this.exclusividad = otro;
    }

    public void noExclusivo() {
        this.exclusividad = this;
    }

    @Override
    public int compareTo(Usuario otro) {
        return (this.identificador - otro.devolverIdentificador());
    }

}
