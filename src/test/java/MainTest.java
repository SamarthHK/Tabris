import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.viveka01.Main;

public class MainTest {

    static byte[] testCase;
    private static final Random rnd = new Random(42); // deterministic seed

    @Before
    public void setup() {
        int length = 50;
        byte[] arr = new byte[length];

        // fill with random printable ASCII
        for (int i = 0; i < length; i++) {
            arr[i] = (byte) (33 + rnd.nextInt(94));
        }

        // insert "\r\n\r\n" exactly once
        int position = rnd.nextInt(length - 3);
        arr[position]     = '\r';
        arr[position + 1] = '\n';
        arr[position + 2] = '\r';
        arr[position + 3] = '\n';

        testCase = arr;
    }

    @Test
    public void testCheckLineBreak() {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(testCase, 0, testCase.length);

        int value = Main.checkLineBreak(output);

        System.out.printf("Contains? %d\n", value);

        assertTrue(value >= 0);
    }
}