import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {

    private static final int QUEUE_SIZE = 100;
    private static final int TEXT_AMOUNT = 10_000;
    private static final int TEXT_LENGTH = 100_000;

    private static BlockingQueue<String> textA = new ArrayBlockingQueue(QUEUE_SIZE);
    private static BlockingQueue<String> textB = new ArrayBlockingQueue(QUEUE_SIZE);
    private static BlockingQueue<String> textC = new ArrayBlockingQueue(QUEUE_SIZE);

    public static void main(String[] args) {

        Thread fillTexts = new Thread(() -> {
            for (int i = 0; i < TEXT_AMOUNT; i++) {
                String text = generateText("abc", TEXT_LENGTH);
                try {
                    textA.put(text);
                    textB.put(text);
                    textC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        fillTexts.start();

        Thread maxA = new Thread(() -> {
            String text = findMax(textA, 'a');
            System.out.println("Текст, в котором содержится максимальное количество символов 'а': " + text);
        });

        Thread maxB = new Thread(() -> {
            String text = findMax(textB, 'b');
            System.out.println("Текст, в котором содержится максимальное количество символов 'b': " + text);
        });

        Thread maxC = new Thread(() -> {
            String text = findMax(textC, 'c');
            System.out.println("Текст, в котором содержится максимальное количество символов 'c': " + text);
        });

        maxA.start();
        maxB.start();
        maxC.start();

    }

    public static String findMax(BlockingQueue<String> textFromQueue, char letter) {
        int count = 0;
        int maxCount = 0;
        String maxText = null;

        for (int i = 0; i < TEXT_AMOUNT; i++) {
            String text = null;
            try {
                text = textFromQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int j = 0; j < text.length(); j++) {
                if (text.charAt(j) == letter) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                maxText = text;
            }
            count = 0;

        }
        return maxText;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
