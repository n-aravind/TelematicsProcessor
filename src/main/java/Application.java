import processor.Consumer;
import processor.Producer;

public class Application {
    public static void main(String[] args) {
        new Producer().start();
        new Consumer().start();
    }
}
