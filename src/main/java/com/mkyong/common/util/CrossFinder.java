package com.mkyong.common.util;

public class CrossFinder {
    public static boolean rowCrossed(String[][] area, int sides) {
        for (int i = 0; i < sides; i++) {
            if (area[i][0] == area[i][1] &&
                    area[i][1] == area[i][2] &&
                    area[i][0] != null)
                return true;
        }
        return false;
    }

    public static boolean columnCrossed(String[][] area, int sides) {
        for (int i = 0; i < sides; i++) {
            if (area[0][i] == area[1][i] &&
                    area[1][i] == area[2][i] &&
                    area[0][i] != null)
                return true;
        }
        return false;
    }

    public static boolean diagonalCrossed(String[][] area, int sides) {
        if (area[0][0] == area[1][1] &&
                area[1][1] == area[2][2] &&
                area[0][0] != null)
            return true;

        if (area[0][2] == area[1][1] &&
                area[1][1] == area[2][0] &&
                area[0][2] != null)
            return true;
        return false;
    }


    public static String rowPlaceToInsert(String[][] area, String placeHolder, int sides) {
        String result = null;
        for (int i = 0; i < sides; i++) {
            if (area[i][0] == area[i][1] && area[i][1] == placeHolder && area[i][2] == null)
                return result = i + " " + 2;
            else if (area[i][1] == area[i][2] && area[i][2] == placeHolder && area[i][0] == null)
                return result = i + " " + 0;
            else if (area[i][0] == area[i][2] && area[i][2] == placeHolder && area[i][1] == null)
                return result = i + " " + 1;
        }
        return result;
    }


    public static String columnPlaceToInsert(String[][] area, String placeHolder, int sides) {
        String result = null;
        for (int i = 0; i < sides; i++) {
            if (area[0][i] == area[1][i] && area[1][i] == placeHolder && area[2][i] == null)
                return result = 2 + " " + i;
            else if (area[1][i] == area[2][i] && area[2][i] == placeHolder && area[0][i] == null)
                return result = 0 + " " + i;
            else if (area[0][i] == area[2][i] && area[2][i] == placeHolder && area[1][i] == null)
                return result = 1 + " " + i;
        }
        return result;
    }

    public static String diagonalPlaceToInsert(String[][] area, String placeHolder, int sides) {
        String result = null;

        if (area[0][0] == area[1][1] && area[1][1] == placeHolder && area[2][2] == null)
            return result = 2 + " " + 2;
        else if (area[1][1] == area[2][2] && area[2][2] == placeHolder && area[0][0] == null)
            return result = 0 + " " + 0;
        else if (area[0][0] == area[2][2] && area[2][2] == placeHolder && area[1][1] == null)
            return result = 1 + " " + 1;

        return result;
    }

    public static String getEmptyPlace(String[][] area, int sides) {
        for (int i = 0; i < sides; i++) {
            for (int j = 0; j < sides; j++) {
                if (area[i][j] == null)
                    return i + " " + j;
            }
        }
        return null;
    }

}
