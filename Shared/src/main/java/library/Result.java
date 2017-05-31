package library;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Result implements Serializable {

    private Integer number;
    private String color;
    private Set<Integer> redNumbers = new HashSet<>(Arrays.asList(2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35));

    public Result(Integer number) {
        this.number = number;
        setColor();
    }

    public Integer getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }

    public void setColor() {
        this.color = redNumbers.contains(this.number) ? "Red" : "Black";
    }

    @Override
    public String toString() {
        return "Result{" +
                "number=" + number +
                ", color='" + color + '\'' +
                '}';
    }
}

