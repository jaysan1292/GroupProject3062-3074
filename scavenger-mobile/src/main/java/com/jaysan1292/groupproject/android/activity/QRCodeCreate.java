package com.jaysan1292.groupproject.android.activity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Hashtable;

/**
 * Created with IntelliJ IDEA.
 * User: Kazedayz
 * Date: 11/12/12
 * Time: 12:40 PM
 */
public class QRCodeCreate {
    public static void main(String[] args) {
        Charset charset = Charset.forName("UTF-8");
        CharsetEncoder encode = charset.newEncoder();
        byte[] b = null;
        try {
            //Converting string to UTF -8 bytes in ByteBuffer
            ByteBuffer bbuf = encode.encode(CharBuffer.wrap("utf 8 characters"));
            b = bbuf.array();

        } catch (CharacterCodingException e) {
            System.out.println(e.getMessage());
        }

        String data;
        try {
            data = new String(b, "UTF-8");
            //Gets Bytematrix for data
            BitMatrix matrix = null;
            int h = 100;
            int w = 100;
            com.google.zxing.Writer writer = new MultiFormatWriter();
            try {
                Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
                hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                matrix = writer.encode(data, BarcodeFormat.QR_CODE, w, h, hints);

            } catch (WriterException e) {
                System.out.println(e.getMessage());
            }

            //gets the image
            String filePath = "C:/Users/Kazedayz/Dropbox/Shared - COMP 3074-3062 Group Project/QR code - 'Hello, world!'.png";
            File file = new File(filePath);
            try {
                MatrixToImageWriter.writeToFile(matrix, "PNG", file);
                System.out.println("printing to " + file.getAbsolutePath());

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }

    }
}
