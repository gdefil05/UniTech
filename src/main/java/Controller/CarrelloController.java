package Controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;

import Model.Carrello;
import Model.ElementoCarrello;
import Util.AnimazioneUtil;
import Util.NavigationManager;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.geometry.Insets;

public class CarrelloController {

    @FXML
    private VBox boxProdotti;

    @FXML
    private Label lblCarrelloVuoto;

    @FXML
    private Label lblTotale;

    @FXML
    private Button btnCheckout;

    @FXML
    private Button esciCarrelloBtn;

    @FXML
    private Button esci;

    @FXML
    public void initialize() {
        aggiornaCarrello();
        AnimazioneUtil.aggiungiAnimazione(esciCarrelloBtn);
        AnimazioneUtil.aggiungiAnimazione(esci);
    }

    public void aggiornaCarrello() {

        var prodotti = Carrello.getIstanza().getProdotti();

        boxProdotti.getChildren().clear();


        if (prodotti.isEmpty()) {
            lblCarrelloVuoto.setVisible(true);
            lblCarrelloVuoto.setManaged(true);
            lblTotale.setText("€ 0.00");
            btnCheckout.setVisible(false);
            btnCheckout.setManaged(false);
            return;
        }

        lblCarrelloVuoto.setVisible(false);
        lblCarrelloVuoto.setManaged(false);
        btnCheckout.setVisible(true);
        btnCheckout.setManaged(true);

        for (ElementoCarrello p : prodotti) {

            HBox card = new HBox(15);
            card.setStyle("-fx-background-color:  #EEF2FB; -fx-padding: 10; -fx-background-radius: 20; -fx-border-radius:20;");

            ImageView img = new ImageView(p.getImmagine());
            img.setFitWidth(70);
            img.setFitHeight(70);
            img.setPreserveRatio(true);

            VBox info = new VBox(5);

            Label nome = new Label(p.getNome());
            Label prezzo = new Label("Prezzo: € " + p.getPrezzo());
            Label quantita = new Label("Quantità: " + p.getQuantita());
            Label caratteristiche = new Label(p.getCaratteristiche());
            Label totale = new Label("Totale: € " + p.getTotale());

            Button btnMinus = new Button("-");
            Button btnPlus = new Button("+");
            btnMinus.getStyleClass().add("btn-minus");
            btnPlus.getStyleClass().add("btn-plus");

            FontIcon btnDelete = new FontIcon("fas-times");
            btnDelete.setIconSize(18);
            btnDelete.getStyleClass().add("x-icon");
            btnDelete.setCursor(javafx.scene.Cursor.HAND);

            btnMinus.setOnAction(e -> {
                Carrello.getIstanza().diminuisciProdotto(p);
                aggiornaCarrello();
            });

            btnPlus.setOnAction(e -> {
                p.aumentaQuantita();
                aggiornaCarrello();
            });

            btnDelete.setOnMouseClicked(e -> apriDialogRimozione(p));

            HBox controls = new HBox(10, btnMinus, btnPlus);

            info.getChildren().addAll(nome, prezzo, quantita, totale, controls, caratteristiche);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox leftSide = new HBox(15, img, info);

            card.getChildren().addAll(leftSide, spacer, btnDelete);

            boxProdotti.getChildren().add(card);
        }
        lblTotale.setText("€ " + String.format("%.2f", Carrello.getIstanza().getTotale()));
    }

    /**
     * Dialog di conferma rimozione prodotto dal carrello.
     * Stesso stile bottoni e stesse animazioni (fade + slide) dell'overlay
     * "Elimina Account" della pagina profilo, ma con dimensioni e
     * posizionamento compatti (piccolo Stage centrato).
     */
    private void apriDialogRimozione(ElementoCarrello p) {

        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(boxProdotti.getScene().getWindow());
        dialog.setResizable(false);

        FontIcon warningIcon = new FontIcon("fas-exclamation-triangle");
        warningIcon.setIconColor(javafx.scene.paint.Color.web("#e53935"));
        warningIcon.setIconSize(40);
        warningIcon.getStyleClass().add("popUp-icon");
        // --- NUOVA LABEL: titolo grande e nero, come "Elimina Account" ---
        Label titolo = new Label("Eliminazione Articolo");
        titolo.setStyle("-fx-text-fill: #1a1a1a; -fx-font-size: 20; -fx-font-weight: bold;");
        VBox.setMargin(titolo, new Insets(20, 0, 0, 0));
        // -----------------------------------------------------------------
        Label msg = new Label("Sei sicuro di voler eliminare questo articolo dal carrello?");
        msg.setStyle("-fx-font-size: 13; -fx-text-fill: #888888;");
        msg.setWrapText(true);
        msg.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        VBox.setMargin(msg, new Insets(10, 0, 0, 0));

        Separator separator = new Separator();
        separator.setPrefWidth(340);

        Button no = new Button("Annulla");
        no.setPrefWidth(140);
        no.setPrefHeight(38);
        no.setStyle("""
            -fx-background-color: #f0f0f0;
            -fx-text-fill: #333333;
            -fx-border-radius: 20;
            -fx-background-radius: 20;
            -fx-font-weight: bold;
            -fx-font-size: 15;
            -fx-cursor: hand;
        """);

        Button si = new Button("Conferma");
        si.setPrefWidth(140);
        si.setPrefHeight(38);
        si.setStyle("""
            -fx-background-color: #e53935;
            -fx-text-fill: white;
            -fx-border-radius: 20;
            -fx-background-radius: 20;
            -fx-font-weight: bold;
            -fx-font-size: 15;
            -fx-cursor: hand;
        """);

        AnimazioneUtil.aggiungiAnimazioneScale(no);
        AnimazioneUtil.aggiungiAnimazioneScale(si);

        HBox buttons = new HBox(15, no, si);
        buttons.setAlignment(javafx.geometry.Pos.CENTER);

        VBox root = new VBox(12, warningIcon,titolo, msg, separator, buttons);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setPrefWidth(400);
        root.setStyle("""
            -fx-background-color: #FFFFFF;
            -fx-background-radius: 18;
            -fx-border-width: 1.5;
            -fx-border-color: #d0d0d0;
            -fx-border-radius: 18;
            -fx-padding: 30 30 40 30;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 25, 0, 0, 8);
        """);
        VBox.setMargin(buttons, new Insets(5, 0, 15, 0));

        // stato iniziale per l'animazione di apertura (come AnimazioneUtil.apriOverlay)
        root.setOpacity(0.0);
        root.setTranslateY(-30);

        javafx.scene.layout.StackPane wrapper = new javafx.scene.layout.StackPane(root);
        wrapper.setStyle("-fx-background-color: transparent;");
        wrapper.setPadding(new Insets(20, 20, 30, 20));

        Scene scene = new Scene(wrapper);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        dialog.setScene(scene);

        wrapper.applyCss();
        wrapper.layout();
        dialog.sizeToScene();
        dialog.centerOnScreen();

        // animazione di apertura identica a AnimazioneUtil.apriOverlay
        dialog.setOnShown(ev -> {
            TranslateTransition slide = new TranslateTransition(Duration.millis(400), root);
            slide.setFromY(-30);
            slide.setToY(0);
            slide.setInterpolator(Interpolator.EASE_OUT);

            FadeTransition fade = new FadeTransition(Duration.millis(400), root);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);

            new ParallelTransition(fade, slide).play();
        });

        // animazione di chiusura identica a AnimazioneUtil.chiudiOverlay
        Runnable chiudiConAnimazione = () -> {
            TranslateTransition slide = new TranslateTransition(Duration.millis(300), root);
            slide.setFromY(0);
            slide.setToY(-30);
            slide.setInterpolator(Interpolator.EASE_IN);

            FadeTransition fade = new FadeTransition(Duration.millis(300), root);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);

            ParallelTransition chiudi = new ParallelTransition(slide, fade);
            chiudi.setOnFinished(ev -> dialog.close());
            chiudi.play();
        };

        si.setOnAction(ev -> {
            Carrello.getIstanza().rimuoviProdotto(p);
            aggiornaCarrello();
            chiudiConAnimazione.run();
        });

        no.setOnAction(ev -> chiudiConAnimazione.run());

        dialog.showAndWait();
    }


    @FXML
    private void esciDalCarrello() {

        Parent previousRoot = Util.Navigation.getPreviousRoot();

        if (previousRoot != null) {

            Scene scene = boxProdotti.getScene();
            Node currentRoot = scene.getRoot();

            FadeTransition fadeOut = new FadeTransition(Duration.millis(600), currentRoot);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(event -> {

                previousRoot.setOpacity(0.0);
                scene.setRoot(previousRoot); // stesso Stage, niente flicker

                FadeTransition fadeIn = new FadeTransition(Duration.millis(600), previousRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });

            fadeOut.play();
        }
    }

    @FXML
    private void vaiAlCheckout(javafx.event.ActionEvent event) {
        NavigationManager.apriCheckout((Node) event.getSource());
    }


}