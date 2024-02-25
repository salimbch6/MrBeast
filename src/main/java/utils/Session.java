package utils;
public class Session {
    public static int account_id;

    public static int getAccount_id() {
        return account_id;
    }

    public static void setAccount_id(int id_user) {
        Session.account_id = id_user;
    }}