package learn.mastery.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Response {
    private ArrayList<String> messages = new ArrayList<>();
    private String successMessage;

    public boolean isSuccess() {
        return messages.isEmpty();
    }

    public String getSuccessMessage() { return successMessage; }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public List<String> getErrorMessages() {
        return new ArrayList<>(messages);
    }

    public void addErrorMessage(String message) {
        messages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(messages, response.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(messages);
    }
}
