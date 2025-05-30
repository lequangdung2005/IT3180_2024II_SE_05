package com.hust.ittnk68.cnpm.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.hust.ittnk68.cnpm.callback.UpdateMessageInterface;
import com.hust.ittnk68.cnpm.communication.PaymentRequest;
import com.hust.ittnk68.cnpm.communication.ServerCheckBankingResponse;
import com.hust.ittnk68.cnpm.communication.ServerObjectByIdQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerPaymentStatusQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerQrCodeGenerateResponse;
import com.hust.ittnk68.cnpm.communication.UserGetPaymentQrCode;
import com.hust.ittnk68.cnpm.communication.UserQueryObjectById;
import com.hust.ittnk68.cnpm.communication.UserQueryPaymentStatus;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.Expense;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.type.ExpenseType;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import atlantafx.base.controls.Card;
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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

public class ClientExpensePage extends DuongFXTabPane {

    private final List<Node> notPayedTile = new ArrayList<> ();
    private final List<Node> payedTile = new ArrayList<> ();

    private final ClientSceneController sceneController;

    public ClientExpensePage (ClientSceneController sceneController) {

	this.sceneController = sceneController;
	loadPage ();
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

	    this.getTabs().add (createTab ("Chưa thanh toán", notPayedTile));
	    this.getTabs().add (createTab ("Đã thanh toán", payedTile));
	});

	new Thread (fetchDataTask).start ();
    }

    private void deleteAll ()
    {
	notPayedTile.clear ();
	payedTile.clear ();
	this.getTabs().clear ();
    }

    private void fetchData (ClientSceneController sceneController, UpdateMessageInterface caller) {

	caller.updateMsg("Fetching expense ...");

	UserQueryPaymentStatus request = new UserQueryPaymentStatus (sceneController.getUsername());
	ServerPaymentStatusQueryResponse res = sceneController.queryPaymentStatus (request);

	System.out.println ("debug:");
	System.out.println (res.getResponseStatus());
	System.out.println (res.getResponseMessage());

	if(res.getResponseStatus() != ResponseStatus.OK) {
	    sceneController.getClientInteractor().showFailedWindow (res);
	    return;
	}

	List<Map<String, Object>> list = res.getResult();
	int expenseCount = 0;

	for (Map<String, Object> map : list) {
	    PaymentStatus ps = PaymentStatus.convertFromMap (map);

	    caller.updateMsg (String.format ("Fetching expense %d ...", ++expenseCount));

	    Expense tmpExpense = new Expense ();
	    tmpExpense.setId (ps.getExpenseId ());
	    UserQueryObjectById expenseRequest = new UserQueryObjectById (sceneController.getUsername(), tmpExpense);
	    ServerObjectByIdQueryResponse expenseResult = sceneController.userQueryObjectById (expenseRequest);

	    if (expenseResult.getResponseStatus() != ResponseStatus.OK) {
		sceneController.getClientInteractor().showFailedWindow (expenseResult);
		return;
	    }

	    Expense expense = null;
	    for (Map<String, Object> map2 : expenseResult.getResult()) {
		expense = Expense.convertFromMap (map2);
	    }

	    if (expense.getRequired ()) {
		System.out.println (expense.getExpenseTitle());
		System.out.printf ("%d/%d\n", ps.getTotalPay(), expense.getTotalCost());

		boolean notPayed = (ps.getTotalPay() < expense.getTotalCost());
		(notPayed ? notPayedTile : payedTile)	
		    .add (createExpenseTile (expense, ps, notPayed));
	    }

	}
    }

    private Tab createTab (String tabName, List<Node> tileList) {
	VBox con = new VBox (16);

	con.getChildren().addAll(tileList);

	ScrollPane sp = new ScrollPane ();
	sp.setPadding (new Insets (5, 0, 0, 0));
	sp.setContent (con);
	sp.widthProperty().addListener ((obs, oldVal, newVal) -> {
	    // minus scrollbar's width ...
	    con.setPrefWidth (newVal.doubleValue() - 10);
	});

	AnchorPane ap = new AnchorPane();
	ap.getChildren().add (sp);
	AnchorPane.setTopAnchor (sp, 0.0);
	AnchorPane.setLeftAnchor (sp, 0.0);
	AnchorPane.setRightAnchor (sp, 0.0);
	AnchorPane.setBottomAnchor (sp, 0.0);

	Tab tab = new Tab ();
	tab.setText (tabName);
	tab.setContent (ap);
	return tab;
    }

    private void qrCodeGenerate (PaymentStatus paymentStatus, int amount)
    {
	PaymentRequest paymentRequest = new PaymentRequest (sceneController.getUsername(), paymentStatus.getId(), amount);
	UserGetPaymentQrCode req = new UserGetPaymentQrCode (sceneController.getUsername (), paymentRequest, sceneController.getUriBase(), 500, 500);
	ServerQrCodeGenerateResponse res = sceneController.getPaymentQrCode (req);

	System.out.println (res.getResponseStatus());
	System.out.println (res.getResponseMessage());
	System.out.println (res.getToken());
	System.out.println (res.getQrImageBase64());

	if (! res.getResponseStatus ().equals (ResponseStatus.OK)) {
	    sceneController.getClientInteractor ().showFailedWindow (res);
	    return ;
	}

	ImageView qrCode = QrImage.build (res.getQrImageBase64 (), req.getWidth ());

	Stage stage = new Stage ();
	sceneController.openSubStage (stage, qrCodePaymentBox(stage, qrCode, amount, res.getToken ()));
    }

    private Card qrCodePaymentBox (Stage stage, ImageView qrCode, int amount, String token)
    {
	Card card = new Card ();
	// card.getStyleClass ().add (Styles.ELEVATED_1);

	card.setHeader (qrCode);

	Circle ring = new Circle(16);
        ring.setFill(null);
        ring.setStroke(Color.DODGERBLUE);
        ring.setStrokeWidth(5);
        ring.setStrokeLineCap(StrokeLineCap.ROUND);
        ring.getStrokeDashArray().addAll(20.0, 15.0); // Create a gap to look like a ring
        // Rotate animation
        RotateTransition rotate = new RotateTransition(Duration.seconds(1), ring);
        rotate.setByAngle(360);
        rotate.setCycleCount(RotateTransition.INDEFINITE);
        rotate.setAutoReverse(false);
        rotate.play();

	Label moneyLabel = new Label (String.format ("%,d đ", amount));
	moneyLabel.setFont (new Font (20));

	HBox hbox = new HBox (5, ring, moneyLabel);
	hbox.setAlignment (Pos.CENTER);
	hbox.setTranslateX (-21);

	card.setSubHeader (hbox);

	// create new thread to wait client transfer money
	new Thread (() -> {

	    // wait for stage be shown
	    while (!stage.isShowing ());

	    AtomicReference<ServerCheckBankingResponse> res = new AtomicReference<>();
	    // check if qr has been scanned
	    while (true) {

		if (! stage.isShowing ()) {
		    return ;
		}

		res.set (sceneController.checkBanking (token));
		System.out.println (res.get().getResponseStatus ());
		System.out.println (res.get().getResponseMessage ());
		if (res.get().isHasBanked ()) {
		    break;
		}
		try {
		    Thread.sleep (2000);
		}
		catch (InterruptedException e) {
		    e.printStackTrace ();
		}
	    }

	    Platform.runLater(() -> {
		rotate.stop ();
		stage.close ();
		if (res.get().getResponseStatus().equals (ResponseStatus.OK))
		{
		    sceneController.getClientInteractor ().notificate ("Thanh toán thành công", "Thanh toán thành công");
		    deleteAll ();
		    loadPage ();
		}
		else
		{
		    sceneController.getClientInteractor ().showFailedWindow (res.get());
		}
	    });

	}).start ();

	return card;
    }

    private VBox createExpenseTile (Expense ex, PaymentStatus ps, boolean notPayed)
    {
	Text title = new Text (ex.getExpenseTitle ());
	title.getStyleClass ().addAll (Styles.ACCENT, Styles.TEXT, Styles.TITLE_2);

	Button infoBtn = new Button ("Chi tiết");
	infoBtn.getStyleClass ().add (Styles.BUTTON_OUTLINED);
	infoBtn.setPrefWidth (125);
	infoBtn.setOnAction (event -> {
	    // show info
	});

	Button payBtn = new Button ("Thanh toán");
	payBtn.getStyleClass ().addAll (Styles.BUTTON_OUTLINED, Styles.SUCCESS);
	payBtn.setPrefWidth (125);
	payBtn.setOnAction (event -> {
	    Stage stage = new Stage ();
	    PaymentView payView = new PaymentView (sceneController, stage, ex, ps, (a, b) -> qrCodeGenerate (a, b));
	    sceneController.openSubStage (stage, payView);
	});

	HBox btnBox = new HBox (5, infoBtn);
	if (notPayed) btnBox.getChildren ().add (payBtn);

	VBox vb = new VBox ( 15,
	    new TextFlow (title),
	    new VBox ( 5,
		new Label (String.format ("Ngày tạo: %s", ex.getPublishedDate ())),
		new Label (String.format ("Đã thanh toán: %,d / %,d đ", ps.getTotalPay (), ex.getTotalCost ()))
	    ),
	    btnBox
	);
	vb.getStyleClass ().addAll (Styles.INTERACTIVE, "expensetile");
	return vb;
    }
}
