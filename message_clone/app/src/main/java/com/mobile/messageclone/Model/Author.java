package com.mobile.messageclone.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.stfalcon.chatkit.commons.models.IUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Author implements IUser {

    public String userId;
    public String userName;
    public String ProfileImg;
    public String firstName;
    public String lastName;

    public Author()
    {
        ProfileImg=null;
    }

    @Override
    public String getId() {
        return userId;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public String getAvatar() {

/*        if (ProfileImg==null)
        {
            return null;
        }
        else
        {
            return ProfileImg;
        }*/
        return null;

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) throws IOException {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        tempDir.mkdir();
        File tempFile = File.createTempFile("profile", ".jpg", tempDir);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();

        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(bitmapData);
        fos.flush();
        fos.close();
        return Uri.fromFile(tempFile);
    }
}
