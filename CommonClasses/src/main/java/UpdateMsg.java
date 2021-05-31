import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

public class UpdateMsg {
    private HashMap<Integer, LinkedList<File>> cloudStorageContents;
    private String login;

    public UpdateMsg(HashMap<Integer, LinkedList<File>> cloudStorageContents) {
        this.cloudStorageContents = cloudStorageContents;
    }
    public UpdateMsg(String login){
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public HashMap<Integer, LinkedList<File>> getCloudStorageContents() {
        return cloudStorageContents;
    }
}
