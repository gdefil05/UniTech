package Util;

import Model.Carrello;
import Model.CheckoutSession;
import Model.ElementoCarrello;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.util.Duration;

public class NavigationManager {

    public static void apriCarrello(Node nodo) {

        Scene scene = nodo.getScene();
        Parent root = scene.getRoot();

        Navigation.setPreviousRoot(root);

        eseguiTransizioneVersoCarrello(scene, root);
    }

    /**
     * Torna al Carrello SENZA sovrascrivere il previousRoot già impostato.
     * Usato ad esempio quando si annulla il Checkout: il previousRoot deve
     * restare quello impostato prima di entrare nel Checkout (es. la pagina
     * prodotto da cui è partito un "Acquista ora"), altrimenti il tasto
     * "esci dal carrello" riporterebbe erroneamente al Checkout stesso.
     */
    public static void apriCarrelloMantenendoPrecedente(Node nodo) {

        Scene scene = nodo.getScene();
        Parent root = scene.getRoot();

        eseguiTransizioneVersoCarrello(scene, root);
    }

    private static void eseguiTransizioneVersoCarrello(Scene scene, Parent root) {

        FadeTransition fadeOut = new FadeTransition(Duration.millis(600), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        NavigationManager.class.getResource("/fxml/Carrello.fxml")
                );

                Parent newRoot = loader.load();

                newRoot.setOpacity(0.0);
                scene.setRoot(newRoot);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(600), newRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        fadeOut.play();
    }

    /**
     * Checkout "classico", raggiunto dalla pagina Carrello: mostra tutti i
     * prodotti attualmente presenti nel carrello.
     *
     * IMPORTANTE: non tocca il previousRoot. Deve restare quello impostato
     * quando si è entrati nel Carrello (tipicamente la pagina prodotto),
     * altrimenti "esci dal carrello" riporterebbe erroneamente al Carrello
     * stesso (che è la scena attiva in questo preciso momento).
     */
    public static void apriCheckout(Node nodo) {
        CheckoutSession.getIstanza().impostaCheckoutNormale();
        navigaVersoCheckoutSenzaSalvarePrecedente(nodo);
    }

    /**
     * Checkout "Acquista ora": il prodotto viene prima aggiunto normalmente
     * al Carrello (così se l'utente annulla lo ritrova lì), poi la sessione
     * di Checkout viene isolata sul solo prodotto scelto, in modo che il
     * resto del carrello non venga toccato né mostrato in questa schermata.
     * L'animazione di transizione è identica a quella del checkout normale.
     *
     * Qui, a differenza del checkout classico, il previousRoot VA impostato:
     * si parte direttamente dalla pagina prodotto, che deve diventare il
     * riferimento per un successivo "esci dal carrello".
     */
    public static void apriCheckout(Node nodo, ElementoCarrello prodotto) {
        Carrello.getIstanza().aggiungiProdotto(prodotto);
        CheckoutSession.getIstanza().impostaCheckoutRapido(prodotto);

        Navigation.setPreviousRoot(nodo.getScene().getRoot());
        navigaVersoCheckoutSenzaSalvarePrecedente(nodo);
    }

    private static void navigaVersoCheckoutSenzaSalvarePrecedente(Node nodo) {

        Scene scene = nodo.getScene();
        Parent root = scene.getRoot();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(600), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        NavigationManager.class.getResource("/fxml/Checkout.fxml")
                );

                Parent newRoot = loader.load();

                newRoot.setOpacity(0.0);
                scene.setRoot(newRoot);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(600), newRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        fadeOut.play();
    }

    public static void apriConferma(Node nodo) {

        Scene scene = nodo.getScene();
        Parent root = scene.getRoot();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(600), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        NavigationManager.class.getResource("/fxml/Conferma.fxml")
                );

                Parent newRoot = loader.load();

                newRoot.setOpacity(0.0);
                scene.setRoot(newRoot);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(600), newRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        fadeOut.play();
    }
}