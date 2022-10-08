package demo.dvo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FakeConsumerDTO {
    @JsonProperty("id")
    private String id;

    public FakeConsumerDTO(String id) {
        this.id = id;
    }
    public FakeConsumerDTO(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FakeConsumerDTO{" +
            "id='" + id + '\'' +
            '}';
    }
}
