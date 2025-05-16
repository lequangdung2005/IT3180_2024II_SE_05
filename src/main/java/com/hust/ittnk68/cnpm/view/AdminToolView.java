package com.hust.ittnk68.cnpm.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;

import com.hust.ittnk68.cnpm.communication.ServerResponseFile;
import com.hust.ittnk68.cnpm.controller.ClientSceneController;
import com.hust.ittnk68.cnpm.type.ResponseStatus;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AdminToolView extends DuongFXTabPane {

    final private ClientSceneController sceneController;

    public AdminToolView (ClientSceneController sceneController)
    {
        this.sceneController = sceneController;
        this.getTabs().add (new Tab ("Thống kê các khoản từ thiện", createDonationStatisticsView()));
        this.getTabs().add (new Tab ("Khoản thu bãi gửi xe", createVehicleStatisticsView ()));
        this.getTabs().add (new Tab ("Trình đọc CSV", createCsvReader()));
    }

    private VBox createCsvReader ()
    {
        Button chooseFolderButton = new Button (null, new FontIcon(Material2AL.FILE_UPLOAD));
        chooseFolderButton.getStyleClass().addAll (Styles.BUTTON_ICON, Styles.WARNING);

        TextField pathField = new TextField("");
        pathField.setPrefWidth (400);

        Button executeButton = new Button (null, new FontIcon(Material2MZ.REMOVE_RED_EYE));

        chooseFolderButton.setOnAction (e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle ("Chọn file CSV");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(csvFilter);
            File selectedFile = fileChooser.showOpenDialog (sceneController.getStage ());
            if (selectedFile != null) {
                pathField.setText (selectedFile.getAbsolutePath());
            }
        });

        CsvViewer csvViewer = new CsvViewer (sceneController, "Trình đọc CSV", "");

        executeButton.setOnAction(e -> {
            File file = new File (pathField.getText());
            if (! file.exists () || ! file.isFile ()) {
                sceneController.getClientInteractor().notificate ("Thất bại", "File không hợp lệ.");
                return ;
            }

            if (! file.getAbsolutePath ().toLowerCase ().endsWith (".csv")) {
                sceneController.getClientInteractor().notificate ("Thất bại", "Chỉ hỗ trợ File *.csv.");
                return ;
            }

            System.out.println ("choose file: " + file.getAbsolutePath());

            try {
                String content = Files.readString (file.toPath ());
                csvViewer.loadCsvToTable (content);
            }
            catch (Exception e2) {
                sceneController.getClientInteractor().notificate("Thất bại", e2.toString ());
            }

        });

        VBox view = new VBox (15,
            new InputGroup(pathField, chooseFolderButton, executeButton),
            csvViewer
        );
        return view;
    }

    private VBox createDonationStatisticsView ()
    {
        Button chooseFolderButton = new Button (null, new FontIcon(Material2AL.FOLDER_OPEN));
        chooseFolderButton.getStyleClass().addAll (Styles.BUTTON_ICON, Styles.WARNING);

        TextField pathField = new TextField("");
        pathField.setPrefWidth (400);

        Button executeButton = new Button (null, new FontIcon(Material2MZ.SEARCH));

        chooseFolderButton.setOnAction (e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle ("Chọn thư mục xuất File");
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File selectedFolder = directoryChooser.showDialog(sceneController.getStage ());
            if (selectedFolder != null) {
                pathField.setText (selectedFolder.getAbsolutePath());
            }
        });

        CsvViewer csvViewer = new CsvViewer (sceneController, "Thống kê các khoản từ thiện", "");

        executeButton.setOnAction(e -> {
            File folder = new File (pathField.getText());
            if (! folder.exists () || ! folder.isDirectory ()) {
                sceneController.getClientInteractor().notificate ("Thất bại", "Thư mục không hợp lệ.");
                return ;
            }
            System.out.println ("choose folder: " + folder.getAbsolutePath());

            Task<ServerResponseFile> donationGetTask = new Task<>() {
                @Override
                public ServerResponseFile call () {
                    updateMessage ("Loading donation statistics ...");
                    return sceneController.getDonationStatistics();
                }
            };

            LoadingView root = new LoadingView (donationGetTask);
            Stage stage = new Stage ();
            sceneController.openSubStage (stage, root);

            donationGetTask.setOnSucceeded (eeee -> {
                stage.close ();

                ServerResponseFile res = donationGetTask.getValue ();
                if (! res.getResponseStatus ().equals (ResponseStatus.OK)) {
                    sceneController.getClientInteractor().showFailedWindow(res);
                    return ;
                }

                String filename = "donation-statistics-" + System.currentTimeMillis() + ".csv";
                File outputFile = new File (folder, filename);
                outputFile.getAbsolutePath();

                try (Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile)))
                {
                    writer.write (res.getContent ());

                    ButtonType type = sceneController.getClientInteractor().showConfirmationWindow("Xác nhận", "File .csv đã được lưu tại " + outputFile.getAbsolutePath(),
                                            "Bạn có muốn xem nội dung của File không?").get ();
                    if (! type.equals (ButtonType.OK)) {
                        return; 
                    }
                    csvViewer.loadCsvToTable (res.getContent ());
                }
                catch (Exception e2) {
                    sceneController.getClientInteractor().notificate("Thất bại", e2.toString ());
                }

            });

            new Thread (donationGetTask).start ();

        });

        VBox view = new VBox (15,
            new InputGroup(pathField, chooseFolderButton, executeButton),
            csvViewer
        );

        return view;
    }

    private VBox createVehicleStatisticsView ()
    {
        Button chooseFolderButton = new Button (null, new FontIcon(Material2AL.FOLDER_OPEN));
        chooseFolderButton.getStyleClass().addAll (Styles.BUTTON_ICON, Styles.WARNING);

        TextField pathField = new TextField("");
        pathField.setPrefWidth (400);

        Button executeButton = new Button (null, new FontIcon(Material2MZ.SEARCH));

        chooseFolderButton.setOnAction (e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle ("Chọn thư mục xuất File");
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File selectedFolder = directoryChooser.showDialog(sceneController.getStage ());
            if (selectedFolder != null) {
                pathField.setText (selectedFolder.getAbsolutePath());
            }
        });

        CsvViewer csvViewer = new CsvViewer (sceneController, "Thống kê chi phí bãi gửi xe", "");

        executeButton.setOnAction(e -> {
            File folder = new File (pathField.getText());
            if (! folder.exists () || ! folder.isDirectory ()) {
                sceneController.getClientInteractor().notificate ("Thất bại", "Thư mục không hợp lệ.");
                return ;
            }
            System.out.println ("choose folder: " + folder.getAbsolutePath());

            Task<ServerResponseFile> donationGetTask = new Task<>() {
                @Override
                public ServerResponseFile call () {
                    updateMessage ("Loading vehicle statistics ...");
                    return sceneController.getVehicleCsv ();
                }
            };

            LoadingView root = new LoadingView (donationGetTask);
            Stage stage = new Stage ();
            sceneController.openSubStage (stage, root);

            donationGetTask.setOnSucceeded (eeee -> {
                stage.close ();

                ServerResponseFile res = donationGetTask.getValue ();

                if (! res.getResponseStatus ().equals (ResponseStatus.OK)) {
                    sceneController.getClientInteractor().showFailedWindow(res);
                    return ;
                }

                String filename = "vehicle-statistics-" + System.currentTimeMillis() + ".csv";
                File outputFile = new File (folder, filename);
                outputFile.getAbsolutePath();

                try (Writer writer = new OutputStreamWriter(new FileOutputStream(outputFile)))
                {
                    writer.write (res.getContent ());

                    ButtonType type = sceneController.getClientInteractor().showConfirmationWindow("Xác nhận", "File .csv đã được lưu tại " + outputFile.getAbsolutePath(),
                                            "Bạn có muốn xem nội dung của File không?").get ();
                    if (! type.equals (ButtonType.OK)) {
                        return; 
                    }
                    csvViewer.loadCsvToTable (res.getContent ());
                }
                catch (Exception e2) {
                    sceneController.getClientInteractor().notificate("Thất bại", e2.toString ());
                }
            });

            new Thread (donationGetTask).start ();

        });

        VBox view = new VBox (15,
            new InputGroup(pathField, chooseFolderButton, executeButton),
            csvViewer
        );

        return view;
    }

}
