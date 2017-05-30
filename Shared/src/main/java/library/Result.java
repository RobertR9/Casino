package library;

import java.io.Serializable;

public class Result implements Serializable {

    private Integer number;
    private String color;

    public Result(Integer number, String color) {
        this.number = number;
        this.color = color;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Result{" +
                "number=" + number +
                ", color='" + color + '\'' +
                '}';
    }
}

