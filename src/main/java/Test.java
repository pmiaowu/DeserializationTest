import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Test {
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("poc.ser");
        ObjectInputStream ois = new ObjectInputStream(fis);
        ois.readObject();
        ois.close();
        System.out.println("执行完毕");
    }
}