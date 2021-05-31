import java.io.Serializable;

public class RegistrationMsg  implements Serializable {
    private String login;
    private String password;

    public RegistrationMsg(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}