import java.io.Serializable;

public class AuthenticationMsg  implements Serializable {
    private String login;
    private String password;

    public AuthenticationMsg(String login, String password) {
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
