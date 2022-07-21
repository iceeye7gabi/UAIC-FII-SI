public class NodeA {
    public int sizeOfBlockECB;
    public int sizeOfBlockCBC;
    public int sizeOfBlock;
    public KeyManager myKeyManager;
    public String Key3;
    public NodeB nodeB;
    public String receivedKeyKM;
    public String algorithm;
    public String auxVariab;
    private String encryptedSecretKeyKM;

    NodeA(String inputAlgorithm, String inputAuxVariab, KeyManager inputMyKeyManager){
        this.algorithm = inputAlgorithm;
        this.auxVariab = inputAuxVariab;
        this.myKeyManager = inputMyKeyManager;
        this.sizeOfBlock = 16;
        this.sizeOfBlockECB = 44;
        this.sizeOfBlockCBC = 16;
        this.Key3 = myKeyManager.getKey3();
    }

    public void associateWithNodeB(NodeB inputNodeB){
        this.nodeB = inputNodeB;
        System.out.println("Nodul A s-a asociat cu B.");
    }

    public void notifyAlgorithmB(){
        nodeB.setAlgorithm(algorithm);
    }

    public void getKeyFromKM(){
        encryptedSecretKeyKM = myKeyManager.getKeyUsingOperation(algorithm);
        System.out.println("NodulA a primit cheia  "+ encryptedSecretKeyKM +" de la KeyManager");
    }

    public void decryptKeyWithAES(){
        encryptedSecretKeyKM = AESComponent.decryptMessage(encryptedSecretKeyKM, Key3);
    }

    public void sendMessageToNodeA(){
        System.out.println("Nodul A a receptionat si se incepe comunicarea!");
    }

    public void notifyA(){
        System.out.println("Nodul A a primit mesajul.Start:");
    }

    public void encryptMessage(String inputMessage){
        if(algorithm.equals("ECB")){
            String output = encryptAlgorithmECB(inputMessage);
            nodeB.sendEncriptedMessageToNodeB(output);
        }
    }

    public String encryptAlgorithmECB(String inputMessage){
        int iterator=0;
        StringBuilder output = new StringBuilder();
        StringBuilder auxStringBuilder;

        while(inputMessage.length()>=iterator+sizeOfBlock){
            auxStringBuilder = new StringBuilder(inputMessage.substring(iterator, iterator + sizeOfBlock));
            output.append(AESComponent.encryptMessage(auxStringBuilder.toString(), Key3));
            iterator=iterator+sizeOfBlock;
        }

        if(inputMessage.length()>iterator) {
            auxStringBuilder = new StringBuilder(inputMessage.substring(iterator));
            while (auxStringBuilder.length()!=sizeOfBlock)
                auxStringBuilder.append(" ");
            output.append(AESComponent.encryptMessage(auxStringBuilder.toString(), Key3));

        }
        return output.toString();
    }
}
