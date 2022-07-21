import java.util.Random;
import java.util.Scanner;

public class MainClass {
    //https://stackoverflow.com/questions/2626835/is-there-functionality-to-generate-a-random-character-in-java (la nivel de vector)
    public static String initializeVector(int blockSize) {
        int leftLimit = 97;
        int rightLimit = 122;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(blockSize)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static void main(String[] args) {
        String my_initialized_vector = initializeVector(16);
        KeyManager myKeyManager = new KeyManager("k1", "k2", "K3");
        Scanner inputFirst = new Scanner(System.in);
        System.out.println("Scrieti ECB:");
        String inputGot = inputFirst.nextLine();
        NodeA nodeA = new NodeA(inputGot, my_initialized_vector, myKeyManager);
        NodeB nodeB = new NodeB(my_initialized_vector, myKeyManager);

        nodeA.associateWithNodeB(nodeB);
        nodeB.associateWithNodeA(nodeA);
        nodeA.notifyAlgorithmB();

        nodeA.getKeyFromKM();

        nodeA.decryptKeyWithAES();
        nodeB.decryptKeyWithAES();

        nodeA.encryptMessage("Salut Eu sunt Gabi Student in Anul 3 la FII");
    }
}