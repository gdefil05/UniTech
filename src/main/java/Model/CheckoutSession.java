package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Gestisce COSA deve mostrare la pagina di Checkout, senza mai toccare
 * direttamente il Carrello finché l'acquisto non viene confermato.
 *
 * Due modalità:
 *  - NORMALE: il checkout mostra tutti i prodotti presenti nel Carrello
 *             (percorso classico: Carrello -> Checkout)
 *  - RAPIDA:  il checkout mostra SOLO il prodotto su cui è stato premuto
 *             "Acquista ora" (il prodotto, nel frattempo, è già stato
 *             aggiunto normalmente al Carrello)
 *
 * Se l'utente annulla, non viene toccato nulla nel Carrello: il prodotto
 * (o i prodotti) restano dove erano. Se conferma, in modalità rapida viene
 * rimosso SOLO il prodotto acquistato, lasciando intatto il resto del
 * Carrello; in modalità normale viene svuotato tutto il Carrello.
 */
public class CheckoutSession {

    private static CheckoutSession istanza = null;

    private final ObservableList<ElementoCarrello> itemsCheckout = FXCollections.observableArrayList();
    private boolean modalitaRapida = false;

    private CheckoutSession() {}

    public static CheckoutSession getIstanza() {
        if (istanza == null) {
            istanza = new CheckoutSession();
        }
        return istanza;
    }

    /** Checkout "classico": mostra tutto il contenuto attuale del carrello. */
    public void impostaCheckoutNormale() {
        modalitaRapida = false;
        itemsCheckout.setAll(Carrello.getIstanza().getProdotti());
    }

    /** Checkout "Acquista ora": mostra solo il prodotto passato. */
    public void impostaCheckoutRapido(ElementoCarrello prodotto) {
        modalitaRapida = true;
        itemsCheckout.setAll(prodotto);
    }

    public ObservableList<ElementoCarrello> getItemsCheckout() {
        return itemsCheckout;
    }

    public boolean isModalitaRapida() {
        return modalitaRapida;
    }

    public double getTotaleCheckout() {
        double totale = 0;
        for (ElementoCarrello e : itemsCheckout) {
            totale += e.getTotale();
        }
        return totale;
    }

    /** Da chiamare quando l'utente CONFERMA l'acquisto nel Checkout. */
    public void confermaAcquisto() {
        if (modalitaRapida) {
            // Rimuovo dal carrello solo ciò che era in questa sessione di checkout
            for (ElementoCarrello e : itemsCheckout) {
                Carrello.getIstanza().rimuoviProdotto(e);
            }
        } else {
            // Checkout classico: il carrello viene svuotato del tutto
            Carrello.getIstanza().svuota();
        }
        resetSessione();
    }

    /** Da chiamare quando l'utente ANNULLA: il carrello non viene toccato. */
    public void annullaCheckout() {
        resetSessione();
    }

    private void resetSessione() {
        itemsCheckout.clear();
        modalitaRapida = false;
    }
}