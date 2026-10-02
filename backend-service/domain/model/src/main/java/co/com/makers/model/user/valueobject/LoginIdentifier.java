package co.com.makers.model.user.valueobject;

public class LoginIdentifier {

    private final String value;

    public LoginIdentifier(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
