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


    //Torna al Carrello SENZA sovrascrivere il previousRoot già impostato

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


    // Mostra tutti i prodotti attualmente presenti nel carrello.
    public static void apriCheckout(Node nodo) {
        CheckoutSession.getIstanza().impostaCheckoutNormale();
        navigaVersoCheckoutSenzaSalvarePrecedente(nodo);
    }




    /*
      Apre il checkout rapido "Acquista ora", isolando il prodotto selezionato
      senza modificare gli altri elementi presenti nel carrello.
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



    // Apertura del fxml dove l'acquisto viene confermato
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