import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Controller {

    @FXML
    VBox clientPanel, serverPanel;



    //кнопка выхода из программы
    public void btnExitProgram(ActionEvent actionEvent) {
        Platform.exit();
    }

    //кнопка копирования файла
    public void btnCopyFile(ActionEvent actionEvent) {
        PanelController clientPC = (PanelController) clientPanel.getProperties().get("controller");
        PanelController serverPC = (PanelController) serverPanel.getProperties().get("controller");

        if (clientPC.getSelectFileName() == null && serverPC.getSelectFileName() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "select a file");
            alert.showAndWait();
            return;
        }

        PanelController ofPC = null, inPC = null;
        if (clientPC.getSelectFileName() != null) {
            ofPC = clientPC;
            inPC = serverPC;
        }
        if (serverPC.getSelectFileName() != null) {
            ofPC = serverPC;
            inPC = clientPC;
        }

        Path ofPath = Paths.get(ofPC.getCurrentPath(), ofPC.getSelectFileName());
        Path inPath = Paths.get(inPC.getCurrentPath()).resolve(ofPath.getFileName().toString());

        try {
            Files.copy(ofPath, inPath);
            inPC.updateColumns(Paths.get(inPC.getCurrentPath()));
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "select a file");
            alert.showAndWait();
        }
    }

}
