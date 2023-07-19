import org.example.Server;

import org.junit.jupiter.api.*;
import java.io.*;

public class AppTest {


    @Test
    public void testWriteMsg() throws IOException {
        String msg = "Test message";
        File file = new File("C://file.log");
        long beforeLength = file.length();
        Server.writeFileServer(msg);
        long afterLength = file.length();
        boolean afterLengthOverBefore = afterLength > beforeLength;
        String fileContent = readLF(file.getPath());
        boolean haveTestMsg = fileContent.substring(fileContent.length() - msg.length()).contains(msg);
        Assertions.assertTrue(afterLengthOverBefore && haveTestMsg);
    }

    private static String readLF(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}