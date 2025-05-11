package com.hust.ittnk68.cnpm.view;

import com.hust.ittnk68.cnpm.view.QrImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class QrImage {

    public static ImageView build (String base64, int width)
    {
        byte[] imageBytes = Base64.getDecoder ().decode (base64);

        ByteArrayInputStream inputStream = new ByteArrayInputStream (imageBytes);
        Image image = new Image (inputStream);

        ImageView imageView = new ImageView (image);
        imageView.setFitWidth (width);
        imageView.setPreserveRatio (true);
        return imageView;
    }
    
}
