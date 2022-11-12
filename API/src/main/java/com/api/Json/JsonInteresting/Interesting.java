package com.api.Json.JsonInteresting;

import java.util.List;

public class Interesting {
    public List<Feature> features;

    public int getLength(){
        return features.size();
    }

    public String getName(int num){
        return features.get(num).getProperties().getName();
    }

    public String getId(int num){
        return features.get(num).getId();
    }

    public boolean isEmpty(int num){
        Feature feature = features.get(num);
        if(feature.getProperties().getName().length() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public String getInteresting(int num) {

        String res = "<html>name = " + features.get(num).getProperties().getName() +
                     "<br />kind = " + features.get(num).getProperties().getKinds();
        return res;
    }

}
