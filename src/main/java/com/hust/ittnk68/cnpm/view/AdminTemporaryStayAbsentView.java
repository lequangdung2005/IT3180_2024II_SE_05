package com.hust.ittnk68.cnpm.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.ittnk68.cnpm.callback.UpdateMessageInterface;
import com.hust.ittnk68.cnpm.communication.AdminFindObject;
import com.hust.ittnk68.cnpm.communication.ClientMessageBase;
import com.hust.ittnk68.cnpm.communication.PostTemporaryStayAbsentRequest;
import com.hust.ittnk68.cnpm.communication.ServerFindObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerResponseBase;
import com.hust.ittnk68.cnpm.communication.ServerResponseTemporaryStayAbsentRequest;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.TemporaryStayAbsentRequest;
import com.hust.ittnk68.cnpm.type.ResidenceStatus;
import com.hust.ittnk68.cnpm.type.ResponseStatus;
import com.hust.ittnk68.cnpm.type.Sex;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdminTemporaryStayAbsentView extends VBox {

    private ClientSceneController sceneController;
    private List <Node> familyList;
    private VBox con;

    public AdminTemporaryStayAbsentView(ClientSceneController sceneController)
    {
        this.sceneController = sceneController;
        this.familyList = new ArrayList<>();

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

        // HBox.setHgrow (sp, Priority.ALWAYS);
        sp.setMaxWidth (Double.MAX_VALUE);

        Text title = new Text ("Đơn khai báo đã gửi");
        title.getStyleClass().addAll (Styles.TEXT, Styles.ACCENT, Styles.TITLE_3);

        this.setSpacing (5);
        this.getChildren ().addAll (
            title,
            new Separator(),
            sp
        );

        this.getStylesheets().addAll (
            getClass().getResource (
                "/css/client.css"
            ).toExternalForm()
        );
        this.getStyleClass ().addAll ("graywrapper", "border");
        this.setMinSize (700, 500);
        // this.setPadding (new Insets (36));

        Rectangle clip = new Rectangle ();
        clip.widthProperty ().bind (this.widthProperty ());
        clip.heightProperty ().bind (this.heightProperty ());
        clip.setArcWidth (70);
        clip.setArcHeight (70);
        this.setClip (clip);
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

        caller.updateMsg ("Fetching data ...");

        String username = sceneController.getClientModel().getUsername ();
        AdminFindObject req = new AdminFindObject(username, "1=1", new TemporaryStayAbsentRequest());
        ServerFindObjectResponse res = sceneController.findObject(req);

        if (! res.getResponseStatus ().equals(ResponseStatus.OK)) {
            sceneController.getClientInteractor().showFailedWindow(res);
            reload ();
            return;
        }

        caller.updateMsg ("Done!");

        for (Map<String, Object> map : res.getRequestResult())
        {
            TemporaryStayAbsentRequest p = TemporaryStayAbsentRequest.convertFromMap(map);
            familyList.add (createPersonView(p));
            try {
                ObjectMapper mapper = new ObjectMapper();
                System.out.println (mapper.writeValueAsString (map));
            }
            catch (Exception ignore) {}
        }
    }

    private HBox createMetadataLine (String field, String value)
    {
        Text a = new Text (field + ":");
        a.getStyleClass ().addAll (Styles.TEXT_BOLD);
        Text b = new Text (value);
        return new HBox (10, a, b);
    }
    private HBox createRedMetadataLine (String field, String value)
    {
        Text a = new Text (field + ":");
        a.setFill (Color.RED);
        a.getStyleClass ().addAll (Styles.TEXT_BOLD);
        Text b = new Text (value);
        return new HBox (10, a, b);
    }

    private Card createPersonView (TemporaryStayAbsentRequest p)
    {

        Text fullname = new Text (p.getFullname ());
        fullname.getStyleClass ().addAll (Styles.TITLE_2, Styles.ACCENT, Styles.TEXT);

        VBox metadata = new VBox ( 15,
            createRedMetadataLine ("Family Id", String.valueOf (p.getFamilyId ())),
            createMetadataLine ("Ngày sinh (YYYY-MM-DD)", p.getDateOfBirth ().toString ()),
            createMetadataLine ("Giới tính", (p.getSex ().equals (Sex.MALE) ? "Nam" : "Nữ")),
            createMetadataLine ("Số điện thoại", p.getPhoneNumber ()),
            createMetadataLine ("CCCD", p.getCitizenIdentificationNumber ()),
            createMetadataLine ("Tình trạng cư trú", (p.getResidenceStatus ().equals (ResidenceStatus.ABSENT) ? "Tạm vắng" : "Tạm trú"))
        );

        Button deleteBtn = new Button(null, new FontIcon(Material2MZ.RESTORE_FROM_TRASH));
        deleteBtn.getStyleClass().addAll (Styles.BUTTON_ICON, Styles.DANGER);
        deleteBtn.setCursor (Cursor.HAND);
        deleteBtn.setOnAction (event -> {

            Optional<ButtonType> type = sceneController.getClientInteractor().showConfirmationWindow("Xác nhận", "Xác nhận xóa đơn khai báo?", "Đơn khai báo không thể khôi phục nếu xóa.");

            if (type.get().equals(ButtonType.OK))
            {
                String username = sceneController.getClientModel().getUsername();
                PostTemporaryStayAbsentRequest req = new PostTemporaryStayAbsentRequest(username, p);
                ServerResponseBase ress = sceneController.deleteTemporaryStayAbsentRequest(req);
                if (ress.getResponseStatus().equals (ResponseStatus.OK))
                    sceneController.getClientInteractor().notificate ("Thành công", "Xóa khai báo thành công.");
                else
                    sceneController.getClientInteractor().showFailedWindow (ress);
                reload ();
            }

        });

        Card card = new Card ();
        card.setHeader (new VBox (fullname, new Separator()));
        card.setBody (metadata);
        card.setFooter (deleteBtn);
        card.getStyleClass ().addAll (Styles.INTERACTIVE);
        return card;
    }

}
