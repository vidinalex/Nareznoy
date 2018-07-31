package com.example.vidinalex.helpme.helpers;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterDataFilter {

    public static boolean checkRegDataCorrectness(AppCompatActivity activity, String email, String password, String passwordConf, String phone)
    {
        return checkEmail(activity, email) &&
                checkPassword(activity, password) &&
                checkPasswordEquality(activity, password, passwordConf) &&
                checkPhone(activity, phone);
    }


    public static boolean checkEmail(AppCompatActivity activity, String email)
    {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+\\-/=?^`{|}~]{3,}@[a-zA-Z0-9_]+\\.[a-z0-9_]+$");
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches())
        {
            Toast.makeText(activity, "Имэйл не менее 6 симв до @ и по формату", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean checkPassword(AppCompatActivity activity, String password)
    {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{6,}$");
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches())
        {
            Toast.makeText(activity, "Пароль >= 6 симв; a-zA-z0-9", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean checkPasswordEquality(AppCompatActivity activity, String password, String passwordConf)
    {
        if(!password.equals(passwordConf)) {
            Toast.makeText(activity, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean checkPhone(AppCompatActivity activity, String phone)
    {
        if(!phone.equals(""))
        {
            Pattern pattern = Pattern.compile("^((\\+7)|(8))[0-9]{10}$");
            Matcher matcher = pattern.matcher(phone);
            if(!matcher.matches())
            {
                Toast.makeText(activity, "Телефон не соответствует формату", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
