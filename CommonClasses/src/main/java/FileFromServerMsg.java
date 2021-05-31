import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;

public class FileFromServerMsg implements Serializable {
    private LinkedList<File> filesToRequest;


    public FileFromServerMsg(LinkedList<File> files) {
        this.filesToRequest = files;
    }

    public LinkedList<File> getFilesToRequest() {
        return filesToRequest;
    }


}