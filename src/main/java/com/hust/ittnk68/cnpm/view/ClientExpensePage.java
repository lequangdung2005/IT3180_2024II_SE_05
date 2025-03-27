package com.hust.ittnk68.cnpm.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import com.hust.ittnk68.cnpm.communication.ServerObjectByIdQueryResponse;
import com.hust.ittnk68.cnpm.communication.ServerPaymentStatusQueryResponse;
import com.hust.ittnk68.cnpm.communication.UserQueryObjectById;
import com.hust.ittnk68.cnpm.communication.UserQueryPaymentStatus;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.Expense;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.type.ExpenseType;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

interface updateMessageInterface {
    void updateMsg (String message);
}

public class ClientExpensePage extends DuongFXTabPane {

    List<Node> notPayedTile = new ArrayList<> ();
    List<Node> payedTile = new ArrayList<> ();

    public ClientExpensePage (ClientSceneController sceneController) {

	Task<Void> fetchDataTask = new Task<>() {

	    @Override
	    public Void call () {
		fetchData (sceneController, new updateMessageInterface() {
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

    private void fetchData (ClientSceneController sceneController, updateMessageInterface caller) {

	caller.updateMsg("Fetching expense ...");

	UserQueryPaymentStatus request = new UserQueryPaymentStatus (sceneController.getToken ());
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
	    UserQueryObjectById expenseRequest = new UserQueryObjectById (sceneController.getToken (), tmpExpense);
	    ServerObjectByIdQueryResponse expenseResult = sceneController.userQueryObjectById (expenseRequest);

	    if (expenseResult.getResponseStatus() != ResponseStatus.OK) {
		sceneController.getClientInteractor().showFailedWindow (expenseResult);
		return;
	    }

	    Expense expense = null;
	    for (Map<String, Object> map2 : expenseResult.getResult()) {
		expense = Expense.convertFromMap (map2);
	    }

	    System.out.println (expense.getExpenseTitle());
	    System.out.printf ("%d/%d\n", ps.getTotalPay(), expense.getTotalCost());

	    (ps.getTotalPay() < expense.getTotalCost() ? notPayedTile : payedTile)	
		.add (createExpenseTile (expense, ps));
	}
    }

    private Tab createTab (String tabName, List<Node> tileList) {
	VBox con = new VBox (5);

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

    private Tile createExpenseTile (Expense expense, PaymentStatus ps) {
	Tile tile = new Tile (expense.getExpenseTitle(),
		expense.getPublishedDate() + " --- " + String.format("%,d / %,d", ps.getTotalPay(), expense.getTotalCost()) + " --- " + expense.getExpenseDescription());
	FontIcon graphicIcon = new FontIcon (ExpenseType.getIkonCode(expense.getExpenseType()));
	tile.setGraphic (graphicIcon);

	IconButton pay = new IconButton (new FontIcon (Material2MZ.PAYMENT));
	IconButton more = new IconButton (new FontIcon (Material2OutlinedAL.INFO)); 
	VBox vb = new VBox (5, pay, more);
	tile.setAction (vb);

	tile.getStyleClass().addAll (Styles.INTERACTIVE, "expensetile");

	return tile;
    }
}
