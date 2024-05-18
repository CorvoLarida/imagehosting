package kz.am.imagehosting.utils;

public class ImageUtils {
    public static String getImageExtension(String imageName){
        String[] imageSplit = imageName.split("\\.");
        return imageSplit[imageSplit.length - 1];
    }
    public static String getImagePath(String imageName){
        return System.getProperty("user.dir") + "/images/" + imageName;
    }
}
