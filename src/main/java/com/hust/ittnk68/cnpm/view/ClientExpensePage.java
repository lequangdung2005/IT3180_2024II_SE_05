package com.hust.ittnk68.cnpm.view;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.Expense;
import com.hust.ittnk68.cnpm.type.Date;
import com.hust.ittnk68.cnpm.type.ExpenseType;

import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ClientExpensePage extends DuongFXTabPane {

    public ClientExpensePage (ClientSceneController sceneController) {
	this.getTabs().add (createTab ("Chưa thanh toán"));
	this.getTabs().add (createTab ("Đã thanh toán"));
    }

    Tab createTab (String tabName) {
	VBox con = new VBox (5);

	// fetch and make content
	Expense e = new Expense ("Tiền dịch vụ tháng 10", "Tiền ...",
		    new Date (2025, 3, 20), 3000000, ExpenseType.SERVICE, true);
	for (int i = 0; i < 6; ++i)
	    con.getChildren().add (createExpenseTile (e));

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

    Tile createExpenseTile (Expense expense) {
	Tile tile = new Tile (expense.getExpenseTitle(), expense.getExpenseDescription());
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
