package com.johnolafenwa.pytorchandroid;

import android.graphics.Bitmap;
import org.pytorch.Tensor;
import org.pytorch.Module;
import org.pytorch.IValue;
import org.pytorch.torchvision.TensorImageUtils;
import org.pytorch.LiteModuleLoader;


public class Regressor {

    Module model;
    float[] mean = {0.5231f, 0.5180f, 0.5115f};
    float[] std = {0.2014f, 0.2018f, 0.2100f};

    public Regressor(String modelPath){

        // model = Module.load(modelPath);
        model = LiteModuleLoader.load(modelPath);

    }

    public void setMeanAndStd(float[] mean, float[] std){

        this.mean = mean;
        this.std = std;
    }

    public Tensor preprocess(Bitmap bitmap, int size){

        bitmap = Bitmap.createScaledBitmap(bitmap,size,size,false);
        return TensorImageUtils.bitmapToFloat32Tensor(bitmap,this.mean,this.std);

    }

    public float Max(float[] inputs){

        int maxIndex = -1;
        float maxvalue = 0.0f;

        for (int i = 0; i < inputs.length; i++){

            if(inputs[i] > maxvalue) {

                maxIndex = i;
                maxvalue = inputs[i];
            }

        }


        return maxvalue;
    }

    public String predict(Bitmap bitmap){

        Tensor tensor = preprocess(bitmap,224);

        IValue inputs = IValue.from(tensor);
        Tensor outputs = model.forward(inputs).toTensor();
        float[] score_array = outputs.getDataAsFloatArray();
        float score = Max(score_array);
        String pollution = String.format("%.5g%n", score);
        // String pollution = String.valueOf(score);

        return pollution;

    }

}

