// WEATHER.GOV HIGH-QUALITY PROTOTYPE
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;

public class Assignment2 extends Application{
    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Assignment2: Weather.gov Prototype");

        BorderPane rootWindow = new BorderPane();
        rootWindow.setStyle("-fx-background-color:linear-gradient(to right, #49AAF4, #7BD0F8);");

        //region Top Nav Bar
        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setStyle("-fx-background-color: #3f98bbff; -fx-padding: 10;");
        Button homeButton = new Button("Home");
        Button forecastButton = new Button("Forecast");
        Button pastWeatherButton = new Button("Past Weather");
        Button newsButton = new Button("News");
        Button moreButton = new Button("More ▾");
        VBox moreMenu = new VBox();
        ContextMenu menu = new ContextMenu();
        menu.setStyle("-fx-background-color: #59aacaff;");

        menu.getItems().addAll(
            new MenuItem("About"),
            new MenuItem("Contact Us"),
            new MenuItem("Help")
        );

        // moreMenu dropdown on hovering over moreButton
        moreButton.setOnMouseEntered(e -> {
            if (!menu.isShowing()) {
                menu.show(moreButton, Side.BOTTOM, 0, 0);
            }
        });

        // on mouse leaving the button, check if we're hovering over either moreButton or the dropdown, if not hide the menu
        moreButton.setOnMouseExited(e -> {
            if (!moreButton.isHover() && !menu.getSkin().getNode().isHover()) {
                menu.hide();
            }
        });

        // edge case if we don't touch the dropdown menu and only the button, both will hide
        menu.setOnShowing(e -> {
            Node menuNode = menu.getSkin().getNode();
            menuNode.setOnMouseExited(ev -> {
                if (!moreButton.isHover()) {
                    menu.hide();
                }
            });
        });     

        homeButton.setStyle("-fx-background-color: #3f98bbff;");
        forecastButton.setStyle("-fx-background-color: #3f98bbff;");
        pastWeatherButton.setStyle("-fx-background-color: #3f98bbff;");
        newsButton.setStyle("-fx-background-color: #3f98bbff;");
        moreButton.setStyle("-fx-background-color: #3f98bbff;");
        moreMenu.setStyle("-fx-background-color: #3f98bbff;");
        moreMenu.setVisible(false);

        hBox.getChildren().addAll(homeButton, forecastButton, pastWeatherButton, newsButton, moreButton);
        //endregion
        rootWindow.setTop(new VBox(hBox, moreMenu));

        //region Map
        Image mapImage = new Image(getClass().getResource("map.png").toExternalForm());
        ImageView mapView = new ImageView(mapImage);

        mapView.setPreserveRatio(true);
        mapView.setFitWidth(800);
        mapView.setSmooth(true);

        Group mapContainer = new Group(mapView);
        mapContainer.setStyle("-fx-background-color:linear-gradient(to right, #49AAF4, #7BD0F8);");

        // scrollpane to zoom thanks stackoverflow
        ZoomableScrollPane mapScrollPane = new ZoomableScrollPane(mapContainer);
        mapScrollPane.setPrefHeight(800);
        mapScrollPane.setStyle("-fx-background-color: transparent; " + "-fx-background: transparent;");
        //endregion
        rootWindow.setCenter(mapScrollPane);

        Polygon florida = createState(new double[]{534.67,363.7,625.8,360.5,661.3,443.5,638.7,452.4,609.7,404,536.3,376.6,532.3,366.9}, "Death", mapContainer, "#FF593C");
        Polygon mexico = createState(new double[]{235.5, 255.6, 316.1, 264.5, 308.9, 350.8, 223.4, 352.4}, "High Wind Warning", mapContainer, "#FAD179");
        Polygon texas = createState(new double[]{316.9,275,356.5,276.6,355.6,306.5,437.9,325,445.2,391.1,392,459.7, 363.7, 450, 300, 400.8, 257.3, 354, 309.7, 352.4, 317, 275}, "High Wind Warning", mapContainer, "#FAD179");
  

        mapContainer.getChildren().addAll(florida, mexico, texas);

        // temporary helper to get coordinates for creating states
        mapView.setOnMouseClicked(e -> {
            System.out.println(e.getX() + ", " + e.getY());
        });

        //region mapKeyImage
        Image mapKey = new Image(getClass().getResource("mapKey.png").toExternalForm());
        ImageView mapKeyImage = new ImageView(mapKey);
        mapKeyImage.setPreserveRatio(true);
        mapKeyImage.setFitHeight(450);
        mapKeyImage.setFitWidth(450);

        HBox mapKeyBox = new HBox(mapKeyImage);
        mapKeyBox.setAlignment(Pos.CENTER);
        mapKeyBox.setPadding(new Insets(10));
        mapKeyBox.setStyle("-fx-background-color: linear-gradient(to right, #49AAF4, #7BD0F8);");
        mapKeyBox.setPrefWidth(450);
        rootWindow.setRight(mapKeyBox);
        //endregion

        //region localForecast node
        Image localForecast = new Image(getClass().getResource("localForecast.png").toExternalForm());
        ImageView localForecastImage = new ImageView(localForecast);
        localForecastImage.setPreserveRatio(true);
        localForecastImage.setFitHeight(450);
        localForecastImage.setFitWidth(450);

        HBox forecastBox = new HBox(localForecastImage);
        forecastBox.setAlignment(Pos.CENTER);
        forecastBox.setPadding(new Insets(10));
        forecastBox.setStyle("-fx-background-color: linear-gradient(to right, #49AAF4, #7BD0F8);");
        forecastBox.setPrefWidth(450);

        rootWindow.setLeft(forecastBox);

        //endregion
       /*  VBox contentNodes = new VBox();
        contentNodes.setAlignment(Pos.TOP_CENTER);  // align to top, not center
        contentNodes.getChildren().addAll(mapScrollPane, mapKeyBox, forecastBox);

        ScrollPane scrollPane = new ScrollPane(contentNodes);
        scrollPane.setFitToWidth(true);   // fill width of window
        scrollPane.setFitToHeight(false); // allow vertical scroll
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(contentNodes);
        scrollPane.setStyle("-fx-background-color: transparent; " + "-fx-background: transparent;");

        rootWindow.setCenter(scrollPane);*/

        Scene scene = new Scene(rootWindow, 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    private double MIN_SCALE = 1.24;
    private double MAX_SCALE = 6;

    private Polygon createState(double[] coordinates, String weatherInfo, Group mapContainer, String colorCode)
    {
        Polygon state = new Polygon(coordinates);
        state.setFill(Color.color(0, 0, 1, 0.0));
        state.setStroke(Color.TRANSPARENT);
        state.setStrokeWidth(2);

        state.setOnMouseEntered(e -> {
            state.setFill(Color.color(0, 0, 1, 0.3));
            state.setStroke(Color.DARKBLUE);
        });
        state.setOnMouseExited(e -> {
            state.setFill(Color.color(0, 0, 1, 0.0));
            state.setStroke(Color.TRANSPARENT);
        });
        state.setOnMouseClicked(e -> {
            showWeatherPopup(mapContainer, weatherInfo, state, colorCode);
        });

        return state;
    }

    private void showWeatherPopup(Group mapContainer, String weatherInfo, Polygon state, String colorCode)
    {
        Tooltip tooltip = new Tooltip(weatherInfo);
        tooltip.setGraphic(new Circle(4, Color.web(colorCode)));

        // place our tooltip next to its state
        Bounds bounds = state.getBoundsInParent();
        double tooltipX = bounds.getMinX();
        double tooltipY = bounds.getMinY() - 30; // slightly above polygon

        // because Tooltip.install() only for hover, have to do this
        tooltip.show(mapContainer.getScene().getWindow(), mapContainer.localToScreen(tooltipX, tooltipY).getX(), mapContainer.localToScreen(tooltipX, tooltipY).getY());

        // tooltip goes away when we click anywhere else
        mapContainer.setOnMousePressed(event -> {
            tooltip.hide();
            mapContainer.getScene().setOnMousePressed(null); // so we don't try to hide nonexistant tooltips
        });
    }


    // thanks dude on stackoverflow who created ZoomableScrollPane, adjusted to add clamps
    public class ZoomableScrollPane extends ScrollPane {
        private double scaleValue = 0.7;
        private double zoomIntensity = 0.02;
        private Node target;
        private Node zoomNode;

        public ZoomableScrollPane(Node target) {
            super();
            this.target = target;
            this.zoomNode = new Group(target);
            setContent(outerNode(zoomNode));

            setPannable(true);
            setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            setFitToHeight(true); //center
            setFitToWidth(true); //center

            updateScale();
        }

        private Node outerNode(Node node) {
            Node outerNode = centeredNode(node);
            outerNode.setOnScroll(e -> {
                e.consume();
                onScroll(e.getTextDeltaY(), new Point2D(e.getX(), e.getY()));
            });
            return outerNode;
        }

        private Node centeredNode(Node node) {
            VBox vBox = new VBox(node);
            vBox.setAlignment(Pos.CENTER);
            return vBox;
        }

        private void updateScale() {

            scaleValue = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scaleValue));
            target.setScaleX(scaleValue);
            target.setScaleY(scaleValue);
        }

        private void onScroll(double wheelDelta, Point2D mousePoint) {
            double zoomFactor = Math.exp(wheelDelta * zoomIntensity);

            Bounds innerBounds = zoomNode.getLayoutBounds();
            Bounds viewportBounds = getViewportBounds();

            // calculate pixel offsets from [0, 1] range
            double valX = this.getHvalue() * (innerBounds.getWidth() - viewportBounds.getWidth());
            double valY = this.getVvalue() * (innerBounds.getHeight() - viewportBounds.getHeight());

            scaleValue = scaleValue * zoomFactor;
            scaleValue = Math.max(MIN_SCALE, Math.min(MAX_SCALE, scaleValue));

            updateScale();
            this.layout(); // refresh ScrollPane scroll positions & target bounds

            // convert target coordinates to zoomTarget coordinates
            Point2D posInZoomTarget = target.parentToLocal(zoomNode.parentToLocal(mousePoint));

            // calculate adjustment of scroll position (pixels)
            Point2D adjustment = target.getLocalToParentTransform().deltaTransform(posInZoomTarget.multiply(zoomFactor - 1));

            // convert back to [0, 1] range
            // (too large/small values are automatically corrected by ScrollPane)
            Bounds updatedInnerBounds = zoomNode.getBoundsInLocal();
            this.setHvalue((valX + adjustment.getX()) / (updatedInnerBounds.getWidth() - viewportBounds.getWidth()));
            this.setVvalue((valY + adjustment.getY()) / (updatedInnerBounds.getHeight() - viewportBounds.getHeight()));
        }
    }

}
