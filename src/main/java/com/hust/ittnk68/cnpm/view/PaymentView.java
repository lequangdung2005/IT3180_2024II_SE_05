package com.hust.ittnk68.cnpm.view;

import com.hust.ittnk68.cnpm.callback.QrCodeGeneratorCallback;
import com.hust.ittnk68.cnpm.communication.ServerResponseBase;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.model.Expense;
import com.hust.ittnk68.cnpm.model.PaymentStatus;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PaymentView extends Card {

    private final DuongFXIntegerField amountIntField;
    private final Button confirmButton;
    private final Button cancelButton;
    private final Button payAll;

    public PaymentView (ClientSceneController sceneController, Stage stage, Expense e, PaymentStatus p, QrCodeGeneratorCallback callback)
    {
        this.setPrefWidth (300);
        this.setPrefHeight (200);

        this.setHeader (new Tile (
            "Thanh toán",
            e.getExpenseTitle ()
        ));

        amountIntField = new DuongFXIntegerField ();

        final DuongFXIntegerField totalIntField = new DuongFXIntegerField (String.format("%,d", e.getTotalCost() - p.getTotalPay ()));
        totalIntField.setEditable (false);

        payAll = new Button (null, new FontIcon(Material2AL.CHECK_CIRCLE));
        payAll.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
        payAll.setCursor(Cursor.HAND);
        payAll.setOnAction (event -> amountIntField.textProperty().set (String.valueOf (e.getTotalCost() - p.getTotalPay ())));
        
        InputGroup box = new InputGroup (
            amountIntField,
            new Label ("/"),
            totalIntField,
            payAll
        );
        this.setBody (box);

        confirmButton = new Button ("Xác nhận");
        confirmButton.setDefaultButton (true);
        confirmButton.setPrefWidth (150);
        confirmButton.setCursor(Cursor.HAND);
        confirmButton.setOnAction (event -> {

            int amount;

            try {
                amount = Integer.parseInt (amountIntField.textProperty().get());
                if (amount <= 0 || amount > e.getTotalCost() - p.getTotalPay ()) {
                    ServerResponseBase res = new ServerResponseBase (ResponseStatus.ILLEGAL_OPERATION, "Số tiền đóng không nằm trong phạm vi hợp lệ.");
                    stage.close ();
                    sceneController.getClientInteractor ().showFailedWindow(res);
                    return ;
                }
            }
            catch (Exception exception)
            {
                // case that input box empty or
                // any exception i don't know ...
                return ;
            }

            stage.close ();
            callback.run (p, amount);

        });

        cancelButton = new Button ("Hủy bỏ");
        cancelButton.setPrefWidth (150);
        cancelButton.setOnAction (event -> stage.close ());
        cancelButton.setCursor(Cursor.HAND);

        this.setFooter (new HBox (20, confirmButton, cancelButton));
    }

}
