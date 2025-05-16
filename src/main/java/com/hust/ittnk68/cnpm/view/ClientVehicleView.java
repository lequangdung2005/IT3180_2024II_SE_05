package com.hust.ittnk68.cnpm.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.ittnk68.cnpm.callback.UpdateMessageInterface;
import com.hust.ittnk68.cnpm.communication.ClientMessageBase;
import com.hust.ittnk68.cnpm.communication.PaymentRequest;
import com.hust.ittnk68.cnpm.communication.PostVehicle;
import com.hust.ittnk68.cnpm.communication.ServerCheckBankingResponse;
import com.hust.ittnk68.cnpm.communication.ServerObjectByIdQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerPaymentStatusQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerQrCodeGenerateResponse;
import com.hust.ittnk68.cnpm.communication.ServerQueryPersonByFIdResponse;
import com.hust.ittnk68.cnpm.communication.ServerResponseBase;
import com.hust.ittnk68.cnpm.communication.ServerResponseVehicle;
import com.hust.ittnk68.cnpm.communication.UserGetPaymentQrCode;
import com.hust.ittnk68.cnpm.communication.UserQueryObjectById;
import com.hust.ittnk68.cnpm.communication.UserQueryPaymentStatus;
import com.hust.ittnk68.cnpm.communication.UserQueryPersonByFId;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.Expense;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.model.Person;
import com.hust.ittnk68.cnpm.model.Vehicle;
import com.hust.ittnk68.cnpm.type.ExpenseType;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.ResponseStatus;
import com.hust.ittnk68.cnpm.type.Sex;
import com.hust.ittnk68.cnpm.type.VehicleType;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.ModalPane;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ClientVehicleView extends VBox {

    private final List<Node> vehicleList = new ArrayList<> ();

    private final ClientSceneController sceneController;

    private final Button addBtn;
    private final Button viewBtn;

    private final VBox con;

    public ClientVehicleView (ClientSceneController sceneController) {

        this.sceneController = sceneController;

        addBtn = new Button(null, new FontIcon(Material2AL.LIBRARY_ADD));
        addBtn.getStyleClass().addAll (Styles.BUTTON_ICON, Styles.ACCENT);
        addBtn.setCursor (Cursor.HAND);
        addBtn.setOnAction (e -> {
            Stage stage = new Stage ();
            sceneController.openSubStage (stage, new VehicleForm(stage, sceneController, this::reload));
        });

        viewBtn = new Button (null, new FontIcon (Material2AL.INFO));
        viewBtn.getStyleClass().addAll (Styles.BUTTON_ICON, Styles.ACCENT);
        viewBtn.setCursor (Cursor.HAND);
        viewBtn.setOnAction (e -> {
            Stage stage = new Stage ();
            sceneController.openSubStage (stage, new VehicleInfoView(stage, sceneController));
        });

        loadPage ();

        con = new VBox (16);
        con.getChildren().addAll(vehicleList);

        VBox conWrapper = new VBox (con);
        conWrapper.setAlignment (Pos.CENTER);

        final ScrollPane sp = new ScrollPane ();
        sp.setPadding (new Insets (5, 0, 0, 0));
        sp.setContent (conWrapper);
        sp.setFitToWidth (false);
        sp.setFitToHeight (false);
        // sp.widthProperty().addListener ((obs, oldVal, newVal) -> {
        //     // minus scrollbar's width ...
        //     con.setPrefWidth (newVal.doubleValue() - 10);
        // });
        con.prefWidthProperty().bind(sp.widthProperty().multiply(0.6));

        this.setPadding (new Insets (36));
        this.getStyleClass ().add ("graywrapper");

        // HBox.setHgrow (sp, Priority.ALWAYS);
        sp.setMaxWidth (Double.MAX_VALUE);
        this.getChildren().addAll (
                new HBox (15, addBtn, viewBtn)
                , new Separator(), sp);
    }

    private void loadPage ()
    {
        Task<Void> fetchDataTask = new Task<>() {

            @Override
            public Void call () {
            fetchData (sceneController, new UpdateMessageInterface() {
                @Override
                public void updateMsg (String message) {
                updateMessage (message);
                }
            });
            return null;
            }

        };

        LoadingView root = new LoadingView (fetchDataTask);
        Stage stage = new Stage ();
        sceneController.openSubStage (stage, root);

        fetchDataTask.setOnSucceeded (e -> {
            stage.close();
            con.getChildren ().addAll (vehicleList);
        });

        new Thread (fetchDataTask).start ();
    }

    private void deleteAll ()
    {
        vehicleList.clear ();
        con.getChildren ().clear ();
    }

    private void reload ()
    {
        deleteAll ();
        loadPage ();
    }

    private void fetchData (ClientSceneController sceneController, UpdateMessageInterface caller) {
        caller.updateMsg ("Fetching person info ...");

        String username = sceneController.getClientModel().getUsername ();
        ClientMessageBase req = new ClientMessageBase (username);
        ServerResponseVehicle res = sceneController.queryVehicle (req);
        caller.updateMsg ("Done!");
        if (! res.getResponseStatus ().equals(ResponseStatus.OK)) {
            sceneController.getClientInteractor().showFailedWindow(res);
            return;
        }
        for (Map<String, Object> map : res.getResult ())
        {
            Vehicle ve = Vehicle.convertFromMap (map);
            vehicleList.add (createVehicleView (ve));
        }
    }

    Node createVehicleView (Vehicle ve)
    {

        FontIcon icon;

        String title;
        if (ve.getVehicleType().equals (VehicleType.CAR)) {
            title = "Xe hơi";
            icon = new FontIcon (Material2AL.DIRECTIONS_CAR);
        }
        else if (ve.getVehicleType().equals (VehicleType.BICYCLE)) {
            title = "Xe đạp";
            icon = new FontIcon (Material2AL.DIRECTIONS_BIKE);
        }
        else if (ve.getVehicleType().equals (VehicleType.MOTORCYCLE)) {
            title = "Xe máy";
            icon = new FontIcon (Material2MZ.MOTORCYCLE);
        }
        else if (ve.getVehicleType().equals (VehicleType.ELECTRIC_BICYCLE)) {
            title = "Xe điện";
            icon = new FontIcon ();
            icon = new FontIcon (Material2AL.ELECTRIC_BIKE);
        }
        else {
            icon = null;
            title = "";
        }

        Button removeBtn = new Button (null, new FontIcon (Material2MZ.REMOVE_CIRCLE));
        removeBtn.getStyleClass ().addAll (Styles.BUTTON_ICON, Styles.DANGER);
        removeBtn.setCursor (Cursor.HAND);
        removeBtn.setOnAction (event -> {
            ButtonType type = sceneController.getClientInteractor ().showConfirmationWindow ("Xác nhận", "Xác nhận xóa phương tiện", "Sau khi xóa sẽ không thể khôi phục.").get ();
            if (! type.equals (ButtonType.OK)) {
                return ;
            }

            PostVehicle req = new PostVehicle (sceneController.getUsername(), ve);
            ServerResponseBase res = sceneController.deleteVehicle (req);
            if (res.getResponseStatus ().equals (ResponseStatus.OK)) {
                sceneController.getClientInteractor().notificate ("Thành công", "Xóa phương tiện thành công.");
                reload ();
            }
            else {
                sceneController.getClientInteractor().showFailedWindow (res);
            }
        });

        HBox actionButtons = new HBox (10);
        actionButtons.setAlignment (Pos.CENTER_LEFT);
        actionButtons.setPrefSize (300, 80);
        actionButtons.getChildren().addAll (removeBtn);
        actionButtons.setPadding (new Insets (0, 0, 0, 15));

        Tile tile = new Tile ();
        tile.setGraphic (icon);
        tile.setTitle (title);
        tile.setDescription (ve.getPlateId ());
        tile.setMouseTransparent (true);

        StackPane slidingTile = new StackPane (tile);
        slidingTile.setPrefSize (300, 80);
        slidingTile.setStyle("-fx-background-color: #FFFFFF;"); 
        slidingTile.setCursor (Cursor.HAND);
        slidingTile.getStyleClass ().add ("tilewrapper");
        slidingTile.setOnMouseClicked (e -> {
            double targetX = slidingTile.getTranslateX () == 0 ? 63: 0;
            TranslateTransition slideTransition = new TranslateTransition (Duration.millis(300), slidingTile);
            slideTransition.setToX (targetX);
            slideTransition.play ();
        });

        Rectangle overlay = new Rectangle (300, 80);
        overlay.setFill (Color.rgb (255, 255, 255, 0.3));
        overlay.setMouseTransparent (true);
        slidingTile.getChildren().add (overlay);

        StackPane tileWrapper = new StackPane (actionButtons, slidingTile);
        return tileWrapper;
    }

}
