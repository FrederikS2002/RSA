public class Main {
    public static void main(String[] args) {
        RSA myrsa = new RSA(9973,  9923);
        System.out.println(myrsa.encrypt("abcd"));
        System.out.println(myrsa.decrypt("43134124 8269417 28424867 16856707 "));
    }
}