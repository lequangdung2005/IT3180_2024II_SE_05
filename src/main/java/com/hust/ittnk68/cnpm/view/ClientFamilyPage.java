package com.hust.ittnk68.cnpm.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.ittnk68.cnpm.callback.UpdateMessageInterface;
import com.hust.ittnk68.cnpm.communication.PaymentRequest;
import com.hust.ittnk68.cnpm.communication.ServerCheckBankingResponse;
import com.hust.ittnk68.cnpm.communication.ServerObjectByIdQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerPaymentStatusQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerQrCodeGenerateResponse;
import com.hust.ittnk68.cnpm.communication.ServerQueryPersonByFIdResponse;
import com.hust.ittnk68.cnpm.communication.UserGetPaymentQrCode;
import com.hust.ittnk68.cnpm.communication.UserQueryObjectById;
import com.hust.ittnk68.cnpm.communication.UserQueryPaymentStatus;
import com.hust.ittnk68.cnpm.communication.UserQueryPersonByFId;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.Expense;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.model.Person;
import com.hust.ittnk68.cnpm.type.ExpenseType;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.ResponseStatus;
import com.hust.ittnk68.cnpm.type.Sex;

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
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ClientFamilyPage extends VBox {

    private final List<Node> familyList = new ArrayList<> ();

    private final ClientSceneController sceneController;

    private final Button addBtn;

    private final VBox con;

    public ClientFamilyPage (ClientSceneController sceneController) {

        this.sceneController = sceneController;

        addBtn = new Button(null, new FontIcon(Material2AL.LIBRARY_ADD));
        addBtn.getStyleClass().addAll (Styles.BUTTON_ICON, Styles.ACCENT);
        addBtn.setCursor (Cursor.HAND);
        addBtn.setOnAction (e -> {
            Stage stage = new Stage ();
            sceneController.openSubStage (stage, new TemporaryStayAbsentForm (stage));
        });

        loadPage ();

        con = new VBox (16);
        con.getChildren().addAll(familyList);
        final ScrollPane sp = new ScrollPane ();
        sp.setPadding (new Insets (5, 0, 0, 0));
        sp.setContent (con);
        sp.widthProperty().addListener ((obs, oldVal, newVal) -> {
            // minus scrollbar's width ...
            con.setPrefWidth (newVal.doubleValue() - 10);
        });

        this.setPadding (new Insets (36));
        this.getStyleClass ().add ("graywrapper");

        // HBox.setHgrow (sp, Priority.ALWAYS);
        sp.setMaxWidth (Double.MAX_VALUE);
        this.getChildren().addAll (addBtn, new Separator(), sp);
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
                con.getChildren ().addAll (familyList);
        });

        new Thread (fetchDataTask).start ();
    }

    private void deleteAll ()
    {
        familyList.clear ();
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
        UserQueryPersonByFId req = new UserQueryPersonByFId (username);
        ServerQueryPersonByFIdResponse res = sceneController.queryPersonByFamilyId (req);
        if (! res.getResponseStatus ().equals(ResponseStatus.OK)) {
            sceneController.getClientInteractor().showFailedWindow(res);
            reload ();
            return;
        }

        caller.updateMsg ("Done!");

        for (Map<String, Object> map : res.getList ())
        {
            Person p = Person.convertFromMap(map);
            familyList.add (createPersonView(p));
        }
    }

    private HBox createMetadataLine (String field, String value)
    {
        Text a = new Text (field + ":");
        a.getStyleClass ().addAll (Styles.TEXT_BOLD);
        Text b = new Text (value);
        return new HBox (2, a, b);
    }

    private Card createPersonView (Person p)
    {

        Text fullname = new Text (p.getFullname ());
        fullname.getStyleClass ().addAll (Styles.TITLE_2, Styles.ACCENT, Styles.TEXT);

        p.getPhoneNumber ();
        p.getResidenceStatus ();
        p.getCitizenIdentificationNumber ();

        VBox metadata = new VBox ( 15,
            createMetadataLine ("Ngày sinh (YYYY-MM-DD)", p.getDateOfBirth ().toString ()),
            createMetadataLine ("Giới tính", (p.getSex ().equals (Sex.MALE) ? "Nam" : "Nữ")),
            createMetadataLine ("Số điện thoại", p.getPhoneNumber ()),
            createMetadataLine ("CCCD", p.getCitizenIdentificationNumber ()),
            createMetadataLine ("Tình trạng cư trú", (p.getResidenceStatus ().equals (ResidenceStatus.ABSENT) ? "Tạm vắng" : "Tạm trú"))
        );

        Card card = new Card ();
        card.setHeader (new VBox (fullname, new Separator()));
        card.setBody (metadata);
        card.getStyleClass ().addAll (Styles.INTERACTIVE);
        return card;
    }

}
