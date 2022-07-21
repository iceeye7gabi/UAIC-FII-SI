public class NodeB {
    public int sizeOfBlockECB;
    public int sizeOfBlockCBC;
    public int sizeOfBlock;
    public KeyManager myKeyManager;
    public String Key3;
    public NodeA nodeA;
    public String receivedKeyKM;
    public String algorithm;
    public String auxVariab;
    private String encryptedSecretKeyKM;

    NodeB(String inputAuxVariab, KeyManager inputMyKeyManager){
        this.auxVariab = inputAuxVariab;
        this.myKeyManager = inputMyKeyManager;
        this.sizeOfBlockECB = 44;
        this.sizeOfBlockCBC = 16;
        this.Key3 = myKeyManager.getKey3();
    }

    public void getKeyFromKM(){
        encryptedSecretKeyKM = myKeyManager.getKeyUsingOperation(algorithm);
        System.out.println("NodulB a primit cheia  "+ encryptedSecretKeyKM +" de la KeyManager");
    }

    public void setAlgorithm(String inputAlgorithm) {
        this.algorithm = inputAlgorithm;
        getKeyFromKM();
        System.out.println("S-a ales ca algoritm: " + inputAlgorithm);
    }

    public void associateWithNodeA(NodeA inputNodeA) {
        this.nodeA = inputNodeA;
    }

    public void decryptKeyWithAES(){
        encryptedSecretKeyKM = AESComponent.decryptMessage(encryptedSecretKeyKM, Key3);
    }

    public void sendEncriptedMessageToNodeB(String inputMessage){
        String output="";
        if(algorithm.equals("ECB")) {
            output = decryptAlgorithmECB(inputMessage);
        }

        System.out.println("Criptat cu  "+algorithm+" : "+ inputMessage);
        System.out.println("Mesajul decriptat: "+ output);
        System.out.println("\n");
    }

    public String decryptAlgorithmECB(String inputText){
        int iterator=0;
        StringBuilder outputDecriptedMessage= new StringBuilder();
        StringBuilder auxStringBuilder;

        while(inputText.length()>=iterator+sizeOfBlockECB){
            auxStringBuilder = new StringBuilder(inputText.substring(iterator, iterator + sizeOfBlockECB));
            outputDecriptedMessage.append(AESComponent.decryptMessage(auxStringBuilder.toString(), Key3));
            iterator=iterator+sizeOfBlockECB;
        }

        return outputDecriptedMessage.toString();
    }
    }

