package com.hust.ittnk68.cnpm.view;

import java.util.stream.IntStream;

import com.hust.ittnk68.cnpm.controller.ClientSceneController;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import atlantafx.base.controls.Calendar;
import atlantafx.base.controls.Spacer;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

public class ClientHomePage extends DuongFXGridPane {
    public ClientHomePage (ClientSceneController sceneController) {
	super (12, 9);
	// this.setGridLinesVisible (true);
	// this.getStyleClass().addAll ("fullpadding");
	this.setHgap (10);
	this.setVgap (10);

	this.add (createMarqueeText (), 2, 0, 8, 1);

	this.add (createFirst (), 0, 1, 8, 5);
	this.add (createSecond(), 8, 1, 4, 5);

	this.add (createThird (), 0, 6, 4, 3);
	this.add (createFourth (), 4, 6, 8, 3);
    }

    private Pane createMarqueeText ()
    {
	String longText = "Tưng bừng chào mừng ngày lễ 30/4! Kính mời cư dân Bluemoon đến khuôn viên trung tâm xem diễu binh – đồ ăn, thức uống phục vụ miễn phí!";
	Text text = new Text (longText);

	text.getStyleClass ().add (Styles.TITLE_3);

	Pane view = new Pane (text);

	text.setLayoutY (43);

	Rectangle clip = new Rectangle();
	clip.widthProperty ().bind (view.widthProperty ());
	clip.heightProperty ().bind (view.heightProperty ());
	view.setClip (clip);

	final double SPEED_FACTOR = 0.25;

	TranslateTransition transition = new TranslateTransition ();
	transition.setDuration (Duration.seconds (10));
	transition.setNode (text);
	transition.setInterpolator (Interpolator.LINEAR);
	transition.setCycleCount (1);
	Runnable recalculateTransition = () -> {
	    transition.setToX(text.getBoundsInLocal().getMaxX() * -1 - 100);
	    transition.setFromX(view.widthProperty().get() + 100);

	    double distance = view.widthProperty().get() + 2 * text.getBoundsInLocal().getMaxX();
	    transition.setDuration(new Duration(distance / SPEED_FACTOR));
	};
	Runnable rerunAnimation = () -> {
	    transition.stop ();
	    recalculateTransition.run ();
	    transition.playFromStart ();
	};
	transition.setOnFinished (new EventHandler<ActionEvent>() {
	    @Override
	    public void handle (ActionEvent actionEvent)
	    {
		rerunAnimation.run ();
	    }
	});
	rerunAnimation.run ();

	view.getStyleClass ().add ("graywrapper");
	return view;
    }

    private Pane createFourth ()
    {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tháng");

        NumberAxis yAxis = new NumberAxis(2_000_000, 5_000_000, 500_000);
        yAxis.setLabel("Chi tiêu (₫)");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Chi tiêu hàng tháng");
        chart.setLegendVisible(false);
        chart.setCreateSymbols(true);
        chart.setAnimated(true);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Tháng 12", 3_200_000));
        series.getData().add(new XYChart.Data<>("Tháng 1", 2_500_000));
        series.getData().add(new XYChart.Data<>("Tháng 2", 4_000_000));
        series.getData().add(new XYChart.Data<>("Tháng 3", 3_600_000));
        series.getData().add(new XYChart.Data<>("Tháng 4", 4_800_000));

        for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip(String.format("%,d ₫", data.getYValue().intValue()));
            Tooltip.install(data.getNode(), tooltip);
        }

        chart.getData().add(series);

	StackPane view = new StackPane (chart);
	view.getStyleClass ().add ("graywrapper");
	return view;
    }

    private VBox createThird ()
    {
	Text title = new Text ("Liên hệ khẩn cấp");
	title.getStyleClass ().add (Styles.TITLE_3);
	Label item1 = new Label ("Quản lý tòa nhà: 0987 654 321", new FontIcon (Material2AL.LOCAL_PHONE));
	Label item2 = new Label ("Bảo vệ: 0911 223 344", new FontIcon (Material2OutlinedMZ.SECURITY));

	item1.getStyleClass().add (Styles.TITLE_4);
	item2.getStyleClass().add (Styles.TITLE_4);

	Button btn = new Button ("Yêu cầu giúp đỡ");
	btn.getStyleClass ().add (Styles.ACCENT);
	VBox vb = new VBox ( 15,
	    new TextFlow (title),
	    new VBox ( 5,
		item1, item2
	    ),
	    btn
	);
	vb.getStyleClass ().add ("redwrapper");
	vb.setPadding (new Insets (36));
	vb.setAlignment (Pos.CENTER);
	return vb;
    }

    private Tile createEventItem (String date, String title, String location, String time)
    {
	Tile res = new Tile(
	    title,
	    String.format ("%s \u2022 %s", location, time)
	);

	Text dateText = new Text (date);
	dateText.getStyleClass ().addAll (Styles.TITLE_2, "date-text");

	res.setGraphic (new TextFlow (dateText));
	return res;
    }

    private VBox createSecond ()
    {
	VBox eventContainer = new VBox(
	    createEventItem ("12/05", "Hội chợ cư dân", "Khuôn viên trung tâm", "08:00 - 16:00"),
	    new Separator (Orientation.HORIZONTAL),
	    createEventItem ("15/05", "Buổi tập yoga cộng đồng", "Phòng Gym tầng 3", "18:30"),
	    new Separator (Orientation.HORIZONTAL),
	    createEventItem ("20/05", "Họp cư dân tháng 5", "Phòng sinh hoạt chung", "19:00")
	);

	Text title = new Text ("Sự kiện sắp tới");
	title.getStyleClass ().add (Styles.TITLE_1);

	VBox vb = new VBox ( 15,
	    new TextFlow (title),
	    eventContainer
	);
	vb.getStyleClass ().add ("graywrapper");
	vb.setPadding (new Insets (36));

	return vb;
    }

    private HBox createHbox ()
    {
	HBox hb = new HBox ();
	hb.getStyleClass ().add ("graywrapper");
	return hb;
    }

    private VBox createFirst ()
    {
	Text l1 = new Text ("“BlueMoon — Nơi an cư, vững bước tương lai.”");
	l1.getStyleClass().add (Styles.TITLE_1);

	Text l2 = createNormalText ("BlueMoon là khu căn hộ cao cấp tọa lạc tại trung tâm thành phố, cung cấp môi trường sống hiện đại với đầy đủ tiện ích như hồ bơi, phòng gym, khu BBQ và hệ thống an ninh 24/7. Cộng đồng cư dân thân thiện, không gian xanh mát, mang lại cảm giác thoải mái và tiện nghi mỗi ngày.");
    
	Text item1 = createNormalText ("\t\u2022 Hồ bơi hiện đại\n");
	Text item2 = createNormalText ("\t\u2022 Phòng gym đầy đủ thiết bị\n");
	Text item3 = createNormalText ("\t\u2022 Khu BBQ ngoài trời\n");
	Text item4 = createNormalText ("\t\u2022 Hệ thống an ninh 24/7\n");
	Text item5 = createNormalText ("\t\u2022 Cộng đồng cư dân thân thiện\n");

	Text l3 = new Text ("Các tiện ích tại BlueMoon");
	l3.getStyleClass().add(Styles.TITLE_3);

	VBox vb = new VBox (15,
		new VBox (
		    new TextFlow(l1),
		    new Separator (Orientation.HORIZONTAL)
		),
		new TextFlow (l2),
		new VBox (
		    new TextFlow (l3),
		    new TextFlow (item1, item2, item3, item4, item5)
		)
	);
	vb.getStyleClass ().add ("graywrapper");
	vb.setPadding (new Insets (36));
	return vb;
    }

    private Text createNormalText (String content)
    {
	Text res = new Text (content);
	res.getStyleClass().add(Styles.TITLE_4);
	return res;
    }
}
