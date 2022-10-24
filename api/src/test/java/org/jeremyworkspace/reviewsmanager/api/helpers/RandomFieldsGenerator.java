package org.jeremyworkspace.reviewsmanager.api.helpers;

public class RandomFieldsGenerator {

    private static int counter = 0;

    public static String getRandomName(){
        return "Name" + getCounter();
    }

    private static int getCounter(){
        return counter++;
    }
}
