public class KeyManager {
    private String key1;
    private String key2;
    private String key3;

    KeyManager(String inputKey1, String inputKey2, String inputKey3) {
        this.key1 = inputKey1;
        this.key2 = inputKey2;
        this.key3 = inputKey3;

    }

    public String getKey1() {
        return this.key1;
    }

    public String getKey2() {
        return this.key2;
    }

    public String getKey3() {
        return this.key3;
    }

    public String getKeyUsingOperation(String inputOperationName) {
        if (inputOperationName.equals("ECB")) {
            return AESComponent.encryptMessage(key1, key3);
        } else if (inputOperationName.equals("CBC")) {
            return AESComponent.encryptMessage(key2,key3);
        } else {
            return null;
        }
    }
}
