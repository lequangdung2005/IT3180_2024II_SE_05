package com.hust.ittnk68.cnpm.view;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.interactor.ClientHomeScreenInteractor;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;

import java.util.function.Supplier;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

// public class ClientHomeScreenView extends Region {
//     public ClientHomeScreenView() {
// 	super();
// 	this.getChildren().add(createTweetCard());
//     }
//
//     private Card createTweetCard() {
// 	Card tweetCard = new Card();
// 	tweetCard.setMinWidth(300);
// 	tweetCard.setMaxWidth(300);
//
// 	Label title = new Label("twitter username");
// 	title.getStyleClass().add(Styles.TITLE_4);
// 	tweetCard.setHeader(title);
//
// 	TextFlow text = new TextFlow(new Text(
// 	    "lorem lorem lorm isum isum isum lorm isum lorem isum lorem isum"
// 	));
// 	text.setMaxWidth(260);
// 	tweetCard.setBody(text);
//
// 	HBox footer = new HBox(10,
// 	    new FontIcon(Material2AL.FAVORITE),
// 	    new Label("861"),
// 	    new Spacer(20),
// 	    new FontIcon(Material2MZ.SHARE),
// 	    new Label("92")
// 	);
// 	footer.setAlignment(Pos.CENTER_LEFT);
// 	tweetCard.setFooter(footer);
//
// 	tweetCard.getStyleClass().add(Styles.ELEVATED_1);
// 	tweetCard.getStyleClass().add(Styles.INTERACTIVE);
//
// 	return tweetCard;
//     }
// }
public class ClientHomeScreenView extends DuongFXGridPane {
    private static final int numCols = 15;
    private static final int numRows = 12;

    public ClientHomeScreenView(ClientSceneController sceneController, ClientHomeScreenInteractor interactor) {
	super (numCols, numRows);

	// this.setGridLinesVisible (true);
	this.setHgap (10);
	this.setVgap (10);
	this.setPadding (new Insets (30, 50, 30, 50));

	this.getStyleClass().add("gradientbackground");
	 
	this.add (interactor.getMainScreen(), 1, 1, numCols - 1, numRows - 1);
	interactor.setRoot (new ClientHomePage ());

	BorderPane bp = new BorderPane ();
	// bp.setPadding (new Insets (0, 0, 0, 15));

	this.add (bp, 1, 0, numCols - 1, 1);

	Label greeting = new Label ("Xin chào, Đăng Dương!");
	greeting.setId ("greetinglabel");
	Label desc = new Label ("Cổng thông tin, dịch vụ chung cư BlueMoon");
	desc.setId ("desclabel");
	VBox greetingVBox = new VBox (greeting, desc);
	bp.setLeft (greetingVBox);

	IconButton chatBtn = new IconButton (new FontIcon (Material2AL.CHAT));
	IconButton notiBtn = new IconButton (new FontIcon (Material2MZ.NOTIFICATIONS));
	HBox notificationWrapper = new HBox (chatBtn, notiBtn);
	notificationWrapper.getStyleClass().addAll("graywrapper", "leftrightpadding");
	notificationWrapper.setAlignment (Pos.CENTER);
	bp.setRight (notificationWrapper);
	
	this.add (new Label(""), 0, 0, 1, 1);

	ClientHomeSideBar vb = new ClientHomeSideBar (interactor);
	this.add (vb, 0, 1, 1, numRows - 1);

    }
}
