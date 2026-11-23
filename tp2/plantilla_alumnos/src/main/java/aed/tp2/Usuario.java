package aed.tp2;

public class Usuario {
    int identificador;
    double millasAcumuladas;
    double millasCanjeadas;
    double millasTransferidas;
    Usuario padre; //Solo se usa para el iterador
    Usuario hijoi; //Ambos hijos se usan para el ABB
    Usuario hijod;
    Usuario exclusividad; //Si es null, no hubo transferencias. Si apunta a otro usuario, solo hizo transferencias con ese. Si apunta a sí mismo, hizo transferencias con más de uno

    Usuario (int data){
            identificador = data;
            millasAcumuladas = 0;
            millasCanjeadas = 0;
            millasTransferidas = 0;
            hijoi = null;
            hijod = null;
            padre = null;
            exclusividad = null;
        }    
    }
