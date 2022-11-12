package com.api.Json.JsonDescription;

public class Description {
    private Preview preview;
    private Wikipedia_extracts wikipedia_extracts;

    public String getImage(){
        return preview.getSource();
    }

    public boolean isEmpty(){
        if (wikipedia_extracts == null || preview == null) {
            return true;
        }

        else {
            return false;
        }
    }
    public String getDescription() {
        String res = wikipedia_extracts.getText();
        return res;
    }
}
