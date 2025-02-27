package com.hust.ittnk68.cnpm.view;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;

import java.util.function.Supplier;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
public class ClientHomeScreenView extends AnchorPane {
    public ClientHomeScreenView() {
	super();

	Tab homeTab = new Tab("Trang chủ", new ClientHomeMainTab());
	homeTab.setGraphic(new FontIcon(Material2AL.HOME));

	Tab memberTab = new Tab("Thành viên", new ClientHomeFamilyMemberTab());
	memberTab.setGraphic(new FontIcon(Material2AL.FAMILY_RESTROOM));

	Tab vehicleTab = new Tab("Xe cộ");
	vehicleTab.setGraphic(new FontIcon(Material2AL.DIRECTIONS_CAR));

	Tab expenseTab = new Tab("Chi phí");
	expenseTab.setGraphic(new FontIcon(Material2AL.ATTACH_MONEY));

	Tab donateTab = new Tab("Quyên góp");
	donateTab.setGraphic(new FontIcon(Material2AL.GRADE));

	Tab settingTab = new Tab("Cài đặt");
	settingTab.setGraphic(new FontIcon(Material2MZ.SETTINGS));

	TabPane tabPane = new TabPane();
	tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	tabPane.getTabs().addAll(homeTab, memberTab, vehicleTab, expenseTab, donateTab, settingTab);
	tabPane.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);

	this.getChildren().add(tabPane);
	AnchorPane.setTopAnchor(tabPane, 0.0);
	AnchorPane.setLeftAnchor(tabPane, 0.0);
	AnchorPane.setRightAnchor(tabPane, 0.0);
	AnchorPane.setBottomAnchor(tabPane, 0.0);
    }
}
