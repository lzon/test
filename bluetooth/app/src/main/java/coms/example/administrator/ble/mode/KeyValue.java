package coms.example.administrator.ble.mode;

public class KeyValue {
    private String keyName;
    private String multiFunctionKeys;
    private Postion postion;
    private String mode;
    private RelateProp relateProp;

    public void setRelateProp(RelateProp relateProp) {
        this.relateProp = relateProp;
    }

    public RelateProp getRelateProp() {
        return relateProp;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getMultiFunctionKeys() {
        return multiFunctionKeys;
    }

    public void setMultiFunctionKeys(String multiFunctionKeys) {
        this.multiFunctionKeys = multiFunctionKeys;
    }

    public Postion getPostion() {
        return postion;
    }

    public void setPostion(Postion postion) {
        this.postion = postion;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
