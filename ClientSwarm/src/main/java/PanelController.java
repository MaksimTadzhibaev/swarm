import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PanelController implements Initializable {

    @FXML
    TableView<FileInfo> filesTable; //таблица с файлами и директориями

    @FXML
    ComboBox<String> pathBox; //главная директория (диск)

    @FXML
    TextField pathField; //путь от главной директории до нашего месторасположения

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<FileInfo, String> fileType = new TableColumn<>("Type");
        fileType.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getNameType()));
        fileType.setPrefWidth(60);

        TableColumn<FileInfo, String> fileName = new TableColumn<>("Name");
        fileName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        fileName.setPrefWidth(200);

        TableColumn<FileInfo, Long> fileSize = new TableColumn<>("Size");
        fileSize.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSize.setCellFactory(column -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });
        fileSize.setPrefWidth(100);

        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        TableColumn<FileInfo, String> fileData = new TableColumn<>("Data of change");
        fileData.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDateOfChange().format(date)));
        fileData.setPrefWidth(170);

        filesTable.getColumns().addAll(fileType, fileName, fileSize, fileData);
        filesTable.getSortOrder().add(fileType);

        pathBox.getItems().clear();
        for (Path p : FileSystems.getDefault().getRootDirectories()) {
            pathBox.getItems().add(p.toString());
        }

        pathBox.getSelectionModel().select(0);

        filesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    Path path = Paths.get(pathField.getText()).resolve(filesTable.getSelectionModel().getSelectedItem().getName());
                    if (Files.isDirectory(path)) {
                        updateColumns(path);
                    }

                }
            }
        });

        updateColumns(Paths.get(".", "ClientSwarm", "FilesClient"));

    }

    //добавление файлов в колонку
    public void updateColumns(Path path) {
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            filesTable.getItems().clear();
//            filesTable.getItems().add(new FileInfo("[..]", -2L));
            filesTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            filesTable.sort();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "failed to update the file list", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //кнопка перехода в родительский каталог
    public void btnPathUp(ActionEvent actionEvent) {
        Path upPath = Paths.get(pathField.getText()).getParent();
        if (upPath != null) {
            updateColumns(upPath);
        }
    }

    //переход при выборе диска
    public void diskAction(ActionEvent actionEvent) {
        ComboBox<String> disk = (ComboBox<String>) actionEvent.getSource();
        updateColumns(Paths.get(disk.getSelectionModel().getSelectedItem()));
    }

    //какой файл выбран
    public String getSelectFileName() {
        if (!filesTable.isFocused()) {
            return null;
        }
        return filesTable.getSelectionModel().getSelectedItem().getName();
    }

    //какая папка выбрана
    public String getCurrentPath() {
        return pathField.getText();
    }



}
