import ai.assignment.kmeans.EuclidianPoint;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Playground {

    public static void main(String[] args) throws IOException {
        String s = new EuclidianPoint(new BigDecimal[] {new BigDecimal("2"), new BigDecimal("1")}, 3).toString();
        String s2 = new EuclidianPoint(new BigDecimal[] {new BigDecimal("2"), new BigDecimal("1")}, 3).toString();
        System.out.println(Arrays.asList(s, s2));
    }
}
