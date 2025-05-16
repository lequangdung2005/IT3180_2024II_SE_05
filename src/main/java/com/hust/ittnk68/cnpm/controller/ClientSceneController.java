package com.hust.ittnk68.cnpm.controller;

import com.hust.ittnk68.cnpm.communication.AdminCreateObject;
import com.hust.ittnk68.cnpm.communication.AdminDeleteObject;
import com.hust.ittnk68.cnpm.communication.AdminFindObject;
import com.hust.ittnk68.cnpm.communication.AdminUpdateObject;
import com.hust.ittnk68.cnpm.communication.ApiMapping;
import com.hust.ittnk68.cnpm.communication.ClientMessageBase;
import com.hust.ittnk68.cnpm.communication.ClientMessageStartSession;
import com.hust.ittnk68.cnpm.communication.PostTemporaryStayAbsentRequest;
import com.hust.ittnk68.cnpm.communication.PostVehicle;
import com.hust.ittnk68.cnpm.communication.ServerCheckBankingResponse;
import com.hust.ittnk68.cnpm.communication.ServerCreateObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerDeleteObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerFindObjectResponse;
import com.hust.ittnk68.cnpm.communication.ServerObjectByIdQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerPaymentStatusQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerQrCodeGenerateResponse;
import com.hust.ittnk68.cnpm.communication.ServerQueryPersonByFIdResponse;
import com.hust.ittnk68.cnpm.communication.ServerResponseBase;
import com.hust.ittnk68.cnpm.communication.ServerResponseFile;
import com.hust.ittnk68.cnpm.communication.ServerResponseObject;
import com.hust.ittnk68.cnpm.communication.ServerResponseStartSession;
import com.hust.ittnk68.cnpm.communication.ServerResponseTemporaryStayAbsentRequest;
import com.hust.ittnk68.cnpm.communication.ServerResponseVehicle;
import com.hust.ittnk68.cnpm.communication.ServerUpdateObjectResponse;
import com.hust.ittnk68.cnpm.communication.UserGetPaymentQrCode;
import com.hust.ittnk68.cnpm.communication.UserQueryObjectById;
import com.hust.ittnk68.cnpm.communication.UserQueryPaymentStatus;
import com.hust.ittnk68.cnpm.communication.UserQueryPersonByFId;
import com.hust.ittnk68.cnpm.interactor.ClientInteractor;
import com.hust.ittnk68.cnpm.model.ClientModel;
import com.hust.ittnk68.cnpm.model.CreateAccountModel;
import com.hust.ittnk68.cnpm.model.CreateExpenseModel;
import com.hust.ittnk68.cnpm.model.CreateFamilyModel;
import com.hust.ittnk68.cnpm.model.CreatePaymentStatusModel;
import com.hust.ittnk68.cnpm.model.CreatePersonModel;
import com.hust.ittnk68.cnpm.model.FindAccountModel;
import com.hust.ittnk68.cnpm.model.FindExpenseModel;
import com.hust.ittnk68.cnpm.model.FindFamilyModel;
import com.hust.ittnk68.cnpm.model.FindPaymentStatusModel;
import com.hust.ittnk68.cnpm.model.FindPersonModel;
import com.hust.ittnk68.cnpm.model.TemporaryStayAbsentRequest;
import com.hust.ittnk68.cnpm.model.UpdateAccountModel;
import com.hust.ittnk68.cnpm.model.UpdateExpenseModel;
import com.hust.ittnk68.cnpm.model.UpdateFamilyModel;
import com.hust.ittnk68.cnpm.model.UpdatePaymentStatusModel;
import com.hust.ittnk68.cnpm.model.UpdatePersonModel;
import com.hust.ittnk68.cnpm.type.AccountType;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class ClientSceneController {
    private Scene scene;
    private StackPane stackPane;
    private Stage stage;

    private UpdatePersonModel updatePersonModel;
    private FindPersonModel findPersonModel;
    private CreatePersonModel createPersonModel;

    private CreateFamilyModel createFamilyModel;
    private FindFamilyModel findFamilyModel;
    private UpdateFamilyModel updateFamilyModel;

    private CreateAccountModel createAccountModel;
    private FindAccountModel findAccountModel;
    private UpdateAccountModel updateAccountModel;

    private CreateExpenseModel createExpenseModel;
    private FindExpenseModel findExpenseModel;
    private UpdateExpenseModel updateExpenseModel;

    private CreatePaymentStatusModel createPaymentStatusModel;
    private FindPaymentStatusModel findPaymentStatusModel;
    private UpdatePaymentStatusModel updatePaymentStatusModel;

    private ClientModel clientModel;

    private ClientInteractor clientInteractor;

    public ClientSceneController() {
        clientModel = new ClientModel ();
        clientInteractor = new ClientInteractor (this);
        stackPane = new StackPane ();

        createPersonModel = new CreatePersonModel ();
        findPersonModel = new FindPersonModel ();
        updatePersonModel = new UpdatePersonModel ();

        createFamilyModel = new CreateFamilyModel ();
        findFamilyModel = new FindFamilyModel ();
        updateFamilyModel = new UpdateFamilyModel ();

        createAccountModel = new CreateAccountModel ();
        findAccountModel = new FindAccountModel ();
        updateAccountModel = new UpdateAccountModel ();
        
        createExpenseModel = new CreateExpenseModel ();
        findExpenseModel = new FindExpenseModel ();
        updateExpenseModel = new UpdateExpenseModel ();

        createPaymentStatusModel = new CreatePaymentStatusModel ();
        findPaymentStatusModel = new FindPaymentStatusModel ();
        updatePaymentStatusModel = new UpdatePaymentStatusModel ();
    }

    public void start(Stage stage, String title, double width, double height) {

        this.stage = stage;

        // window shape
        Rectangle rect = new Rectangle (width, height);
        rect.setArcHeight (60.0);
        rect.setArcWidth (60.0);

        stage.setTitle(title);
        stage.initStyle (StageStyle.TRANSPARENT);

        StackPane root = new StackPane ();
        scene = new Scene(root, width, height);
        scene.setFill (Color.TRANSPARENT);
        scene.getStylesheets().add(getClass().getResource("/css/client.css").toExternalForm());
        stage.setScene(scene);

        Button closeBtn = new Button (null, new FontIcon (Material2AL.CLOSE));
        closeBtn.getStyleClass ().addAll (Styles.BUTTON_ICON, Styles.DANGER);
        closeBtn.setOnAction (e -> stage.close ());
        HBox hb = new HBox(closeBtn);
        hb.setAlignment (Pos.TOP_RIGHT);
        hb.setPadding (new Insets (20, 20, 10, 20));

        root.setClip (rect);
        root.getChildren ().add (
            new VBox (0,
                hb,
                // new Separator (),
                this.stackPane
            )
        );
        VBox.setVgrow (this.stackPane, Priority.ALWAYS);
        this.stackPane.getChildren ().add (new LoginController (this).getView ());

	stage.show(); 
    }

    public ServerResponseStartSession startSession (ClientMessageStartSession message)
    {
        try {
            RestClient restClient = clientModel.getRestClient ();
            return restClient.post()
                    .uri (getUriBase() + ApiMapping.START_SESSION)
                    .body (message)
                    .retrieve ()
                    .body (ServerResponseStartSession.class);
        }
        catch (Exception e)
        {
            return new ServerResponseStartSession (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server", "", AccountType.UNVALID);
        }
    }

    public ServerCreateObjectResponse createObject (AdminCreateObject req) {


        try {
            RestClient restClient = clientModel.getRestClient ();
            ServerCreateObjectResponse res;
            res = restClient.post ()
                .uri (getUriBase () + ApiMapping.CREATE_OBJECT)
                .headers (headers -> headers.setBearerAuth (getToken ()))
                .body (req)
                .retrieve ()
                .body (ServerCreateObjectResponse.class);
            return res;
        }
        catch (Exception e)
        {
            return new ServerCreateObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }

    }

    public ServerResponseBase createTemporaryStayAbsentRequest (PostTemporaryStayAbsentRequest req)
    {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerResponseBase res = restClient.post()
                                            .uri (getUriBase() + ApiMapping.POST_TEMPORARY_STAY_ABSENT_REQUEST)
                                            .headers (headers -> headers.setBearerAuth (getToken ()))
                                            .body (req)
                                            .retrieve ()
                                            .body (ServerResponseBase.class);
            return res;
        }
        catch (Exception e) {
            return new ServerFindObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }
    public ServerResponseBase deleteTemporaryStayAbsentRequest (PostTemporaryStayAbsentRequest req)
    {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerResponseBase res = restClient.post()
                                            .uri (getUriBase() + ApiMapping.DELETE_TEMPORARY_STAY_ABSENT_REQUEST)
                                            .headers (headers -> headers.setBearerAuth (getToken ()))
                                            .body (req)
                                            .retrieve ()
                                            .body (ServerResponseBase.class);
            return res;
        }
        catch (Exception e) {
            return new ServerFindObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }
    public ServerResponseTemporaryStayAbsentRequest queryTemporaryStayAbsentRequest (ClientMessageBase req)
    {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerResponseTemporaryStayAbsentRequest res = restClient.post()
                                            .uri (getUriBase() + ApiMapping.QUERY_TEMPORARY_STAY_ABSENT_REQUEST)
                                            .headers (headers -> headers.setBearerAuth (getToken ()))
                                            .body (req)
                                            .retrieve ()
                                            .body (ServerResponseTemporaryStayAbsentRequest.class);
            return res;
        }
        catch (Exception e) {
            return new ServerResponseTemporaryStayAbsentRequest(ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...", null);
        }
    }

    public ServerResponseBase postVehicle (PostVehicle req)
    {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerResponseBase res = restClient.post()
                                            .uri (getUriBase() + ApiMapping.POST_VEHICLE)
                                            .headers (headers -> headers.setBearerAuth (getToken ()))
                                            .body (req)
                                            .retrieve ()
                                            .body (ServerResponseBase.class);
            return res;
        }
        catch (Exception e) {
            return new ServerFindObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }
    public ServerResponseBase deleteVehicle (PostVehicle req)
    {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerResponseBase res = restClient.post()
                                            .uri (getUriBase() + ApiMapping.DELETE_VEHICLE)
                                            .headers (headers -> headers.setBearerAuth (getToken ()))
                                            .body (req)
                                            .retrieve ()
                                            .body (ServerResponseBase.class);
            return res;
        }
        catch (Exception e) {
            return new ServerFindObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }
    public ServerResponseVehicle queryVehicle (ClientMessageBase req)
    {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerResponseVehicle res = restClient.post()
                                            .uri (getUriBase() + ApiMapping.QUERY_VEHICLE)
                                            .headers (headers -> headers.setBearerAuth (getToken ()))
                                            .body (req)
                                            .retrieve ()
                                            .body (ServerResponseVehicle.class);
            return res;
        }
        catch (Exception e) {
            return new ServerResponseVehicle(ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...", null);
        }
    }

    public ServerResponseObject getParkingFee (String username)
    {
        try {
            String finalUrl = UriComponentsBuilder
                            .fromUriString (getUriBase() + ApiMapping.GET_PARKING_FEE)
                            .queryParam ("username", username)
                            .build ()
                            .toString ();

            return clientModel.getRestClient().get()
                .uri (finalUrl)
                .headers (headers -> headers.setBearerAuth (getToken ()))
                .retrieve ()
                .body (ServerResponseObject.class);
        }
        catch (Exception e)
        {
            return new ServerResponseObject(ResponseStatus.INTERNAL_ERROR, e.toString (), null);
        }
    }

    public ServerResponseFile getVehicleCsv ()
    {
        try {
            String finalUrl = UriComponentsBuilder
                            .fromUriString (getUriBase() + ApiMapping.GET_VEHICLE_STATISTICS)
                            .queryParam ("username", getUsername ())
                            .build ()
                            .toString ();
            return clientModel.getRestClient().get()
                .uri (finalUrl)
                .headers (headers -> headers.setBearerAuth (getToken ()))
                .retrieve ()
                .body (ServerResponseFile.class);
        }
        catch (Exception e) {
            return new ServerResponseFile (ResponseStatus.INTERNAL_ERROR, e.toString (), null);
        }
    }

    public ServerResponseFile getDonationStatistics ()
    {
        try {
            String finalUrl = UriComponentsBuilder
                            .fromUriString (getUriBase() + ApiMapping.GET_DONATION_STATISTICS)
                            .queryParam ("username", getUsername ())
                            .build ()
                            .toString ();
            return clientModel.getRestClient().get()
                .uri (finalUrl)
                .headers (headers -> headers.setBearerAuth (getToken ()))
                .retrieve ()
                .body (ServerResponseFile.class);
        }
        catch (Exception e) {
            return new ServerResponseFile (ResponseStatus.INTERNAL_ERROR, e.toString (), null);
        }
    }

    public ServerFindObjectResponse findObject (AdminFindObject req) {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerFindObjectResponse res = restClient.post()
                                            .uri (getUriBase() + ApiMapping.FIND_OBJECT)
                                            .headers (headers -> headers.setBearerAuth (getToken ()))
                                            .body (req)
                                            .retrieve ()
                                            .body (ServerFindObjectResponse.class);
            return res;
        }
        catch (Exception e) {
            return new ServerFindObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }

    public ServerDeleteObjectResponse deleteObject (AdminDeleteObject req) {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerDeleteObjectResponse res = restClient.post()
                                                .uri (getUriBase() + ApiMapping.DELETE_OBJECT)
                                                .headers (headers -> headers.setBearerAuth (getToken ()))
                                                .body (req)
                                                .retrieve ()
                                                .body (ServerDeleteObjectResponse.class);
            return res;
        }
        catch (Exception e) {
            return new ServerDeleteObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }

    public ServerUpdateObjectResponse updateObject (AdminUpdateObject req) {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerUpdateObjectResponse res = restClient.post()
                                                .uri (getUriBase() + ApiMapping.UPDATE_OBJECT)
                                                .headers (headers -> headers.setBearerAuth (getToken ()))
                                                .body (req)
                                                .retrieve ()
                                                .body (ServerUpdateObjectResponse.class);
            return res;
        }
        catch (Exception e) {
            return new ServerUpdateObjectResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }

    public ServerPaymentStatusQueryResponse queryPaymentStatus (UserQueryPaymentStatus req) {
        try {
            RestClient restClient = clientModel.getRestClient();
            System.out.println ("i step in here");
            ServerPaymentStatusQueryResponse res = restClient.post()
                                                    .uri (getUriBase() + ApiMapping.QUERY_FAMILY_PAYMENT_STATUS)
                                                    .headers (headers -> headers.setBearerAuth (getToken ()))
                                                    .body (req)
                                                    .retrieve ()
                                                    .body (ServerPaymentStatusQueryResponse.class);
            return res;
        }
        catch (Exception e) {
            e.printStackTrace ();
            return new ServerPaymentStatusQueryResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }
    public ServerObjectByIdQueryResponse userQueryObjectById(UserQueryObjectById req) {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerObjectByIdQueryResponse res = restClient.post()
                                                    .uri (getUriBase() + ApiMapping.QUERY_OBJECT_BY_ID)
                                                    .headers (headers -> headers.setBearerAuth (getToken ()))
                                                    .body (req)
                                                    .retrieve ()
                                                    .body (ServerObjectByIdQueryResponse.class);
            return res;
        }
        catch (Exception e) {
            return new ServerObjectByIdQueryResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...");
        }
    }

    public ServerQrCodeGenerateResponse getPaymentQrCode (UserGetPaymentQrCode req)
    {
        try {
            RestClient restClient = clientModel.getRestClient();
            return restClient.post()
                .uri (getUriBase() + ApiMapping.QR_CODE_GENERATOR)
                .headers (headers -> headers.setBearerAuth (getToken ()))
                .body (req)
                .retrieve ()
                .body (ServerQrCodeGenerateResponse.class);
        }
        catch (Exception e) {
            return new ServerQrCodeGenerateResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...", null, null);
        }
    }

    public ServerCheckBankingResponse checkBanking (String bankingToken)
    {
        try {
            String finalUrl = UriComponentsBuilder
                            .fromUriString (getUriBase() + ApiMapping.CHECK_BANKING)
                            .queryParam ("token", bankingToken)
                            .build ()
                            .toString ();

            RestClient restClient = clientModel.getRestClient ();
            return restClient.get()
                .uri (finalUrl)
                .headers (headers -> headers.setBearerAuth (getToken ()))
                .retrieve ()
                .body (ServerCheckBankingResponse.class);
        }
        catch (Exception e)
        {
            return new ServerCheckBankingResponse (ResponseStatus.INTERNAL_ERROR, e.toString(), false);
        }
    }

    public ServerQueryPersonByFIdResponse queryPersonByFamilyId (UserQueryPersonByFId req) {
        try {
            RestClient restClient = clientModel.getRestClient();
            ServerQueryPersonByFIdResponse res = restClient.post()
                                                    .uri (getUriBase() + ApiMapping.QUERY_PERSON_BY_FID)
                                                    .headers (headers -> headers.setBearerAuth (getToken ()))
                                                    .body (req)
                                                    .retrieve ()
                                                    .body (ServerQueryPersonByFIdResponse.class);
            return res;
        }
        catch (Exception e) {
            return new ServerQueryPersonByFIdResponse (ResponseStatus.CANT_CONNECT_SERVER, "can't connect to server...", null);
        }
    }

    public void openSubStage (Stage subStage, Region rg)
    {
        subStage.initModality (Modality.APPLICATION_MODAL);
        subStage.initOwner (this.stage);
        subStage.initStyle (StageStyle.TRANSPARENT);
        Scene subStageScene = new Scene (rg);
        subStageScene.setFill (Color.TRANSPARENT);
        subStage.setScene (subStageScene);
        subStage.show ();
    }

    public void openWarningNoti (String str, FontIcon icon)
    {
        final Notification msg = new Notification (
            str,
            icon
        ); 
        msg.getStyleClass ().addAll (
            Styles.WARNING, Styles.ELEVATED_1
        );
        msg.setPrefWidth (300);
        msg.setPrefHeight (80);
        msg.setMaxHeight (80);

        StackPane.setAlignment (msg, Pos.TOP_RIGHT);
        StackPane.setMargin (msg, new Insets (10, 10, 0, 0));

        msg.setOnClose (e -> {
            Timeline out = Animations.slideOutUp (msg, Duration.millis (250));
            out.setOnFinished (f -> getStackPane().getChildren ().remove (msg));
            out.playFromStart ();
        });

        Timeline in = Animations.slideInDown (msg, Duration.millis (250));
        if (!getStackPane().getChildren().contains (msg)) {
            getStackPane().getChildren ().add (msg);
        }
        in.playFromStart ();
    }

    public StackPane getStackPane ()
    {
        return stackPane;
    }

    public Scene getView() {
        return stage.getScene();
    }
    public void setTitle(String s) {
        stage.setTitle(s);
    }
    public void setRoot(Parent n) {
        // this.getView().setRoot(n);
        this.getStackPane ().getChildren ().clear ();
        this.getStackPane ().getChildren ().add (n);
    }

    public void setUriBase(String url) {
        clientModel.setUriBase ( url );
    }
    public void setUriBase(String ip, String port) {
        clientModel.setUriBase ( String.format("http://%s:%s", ip, port) );
    }
    public String getUriBase () {
        return clientModel.getUriBase ();
    }

    public String getToken () {
        return clientModel.getToken ();
    }
    public void setToken (String token) {
        clientModel.setToken (token);
    }

    public String getUsername () {
        return clientModel.getUsername ();
    }

    public ClientModel getClientModel () {
        return clientModel;
    }

    public ClientInteractor getClientInteractor () {
        return clientInteractor;
    }

    public CreatePersonModel getCreatePersonModel () {
        return createPersonModel;
    }
    public FindPersonModel getFindPersonModel () {
        return findPersonModel;
    }
    public UpdatePersonModel getUpdatePersonModel () {
        return updatePersonModel;
    }

    public CreateFamilyModel getCreateFamilyModel () {
        return createFamilyModel;
    }
    public FindFamilyModel getFindFamilyModel () {
        return findFamilyModel;
    }
    public UpdateFamilyModel getUpdateFamilyModel () {
        return updateFamilyModel;
    }

    public CreateAccountModel getCreateAccountModel () {
        return createAccountModel;
    }
    public FindAccountModel getFindAccountModel () {
        return findAccountModel;
    }
    public UpdateAccountModel getUpdateAccountModel () {
        return updateAccountModel;
    }

    public CreateExpenseModel getCreateExpenseModel () {
        return createExpenseModel;
    }
    public FindExpenseModel getFindExpenseModel () {
        return findExpenseModel;
    }
    public UpdateExpenseModel getUpdateExpenseModel () {
        return updateExpenseModel;
    }

    public CreatePaymentStatusModel getCreatePaymentStatusModel () {
        return createPaymentStatusModel;
    }
    public FindPaymentStatusModel getFindPaymentStatusModel () {
        return findPaymentStatusModel;
    }
    public UpdatePaymentStatusModel getUpdatePaymentStatusModel () {
        return updatePaymentStatusModel;
    }

    public Scene getScene () {
        return scene;
    }

    public Stage getStage () {
        return this.stage;
    }

}
