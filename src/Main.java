import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        RSA myrsa = new RSA(17, 19);
        System.out.println(myrsa.encrypt("abcd"));
        System.out.println(myrsa.decrypt("279 72 74 161 "));
    }
}