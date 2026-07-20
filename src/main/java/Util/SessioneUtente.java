package Util;

import Model.Utente;

//Classe di supporto per avere i dati dell'utente corrente

public class SessioneUtente {

    private static Utente utenteCorrente;
    public static void setUtente(Utente u){utenteCorrente = u;}
    public static Utente getUtente(){return utenteCorrente;}

}
