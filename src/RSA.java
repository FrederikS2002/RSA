import java.math.BigInteger;
import java.util.ArrayList;

public class RSA {
    private final int p;
    private final int q;
    private long n;
    private long e;
    private long a;
    private long d;

    public RSA(int p, int q) {
        this.p = p;
        this.q = q;
        this.setup();
    }

    public RSA(int p, int q, long e) {
        this.p = p;
        this.q = q;
        this.e = e;
        this.setup();
    }

    private static boolean isPrime(long n) {
        if (n <= 1) return false;

        for (long i = 2; i < n; i++)
            if (n % i == 0) return false;

        return true;
    }

    private static long ggt(long num1, long num2) {
        while (num2 != 0) {
            if (num1 > num2) {
                num1 = num1 - num2;
            } else {
                num2 = num2 - num1;
            }
        }
        return num1;
    }

    private static long nextPrime(long num) {
        num++;
        for (long i = 2; i < num; i++) {
            if (num % i == 0) {
                num++;
                i = 2;
            }
        }
        return num;
    }

    private static int encryptChar(int m, long e, long n) {
        BigInteger bm = new BigInteger(Integer.toString(m));
        BigInteger be = new BigInteger(Long.toString(e));
        BigInteger bn = new BigInteger(Long.toString(n));
        return bm.modPow(be, bn).intValue();
    }

    private static int decryptChar(int m, long d, long n) {
        BigInteger bg = new BigInteger(Integer.toString(m));
        BigInteger bd = new BigInteger(Long.toString(d));
        BigInteger bn = new BigInteger(Long.toString(n));
        return bg.modPow(bd, bn).intValue();
    }

    public static String decrypt(String secret, long d, long n) {
        if (secret.equals("")) {
            RSA.sendMessage('e', "No Input");
            return "";
        }
        if (!(d > 1 || d < -1)) {
            RSA.sendMessage('e', "D to small");
            return "";
        }
        StringBuilder result = new StringBuilder();
        String[] array = secret.split(" ");
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (String elem : array) {
            int intelem = Integer.parseInt(elem);
            arrayList.add(intelem);
        }
        for (int elem : arrayList) {
            result.append((char) RSA.decryptChar(elem, d, n));
        }
        return result.toString();
    }

    public static String encrypt(String text, long e, long n) {
        if (text.equals("")) {
            RSA.sendMessage('e', "No Input");
            return "";
        }
        if (e < 2) {
            RSA.sendMessage('e', "E to small");
            return "";
        }
        StringBuilder result = new StringBuilder();
        int encrypted;
        for (int i = 0; i < text.length(); i++) {
            encrypted = RSA.encryptChar(text.charAt(i), e, n);
            if (encrypted > n) {
                RSA.sendMessage('e', "N is to small");
                return "";
            }
            result.append(encrypted).append(" ");
        }
        return result.toString();
    }

    private static void sendMessage(char type, String message) {
        switch (type) {
            case 'e' -> System.out.println("\u001B[31m" + message + "\u001B[0m");
            case 'i' -> System.out.println("\u001B[32m" + message + "\u001B[0m");
            case 'w' -> System.out.println("\u001B[33m" + message + "\u001B[0m");
            default -> System.out.println(message);
        }
    }

    private void setup() {
        if (!RSA.isPrime(p)) {
            RSA.sendMessage('e', "P(" + this.p + ")is not a prime");
            return;
        }
        if (!RSA.isPrime(q)) {
            RSA.sendMessage('e', "Q(" + this.q + ")is not a prime");
            return;
        }
        this.n = this.calculateN();
        if (this.n <= 255) {
            RSA.sendMessage('w', "You can use ascii characters up to  " + this.n);
        }
        this.a = this.calculateA();
        if (this.e < 1 && this.e > -1) {
            this.e = this.checkE();
        }
        Stopwatch stopwatch = new Stopwatch();
        this.d = this.calculateDAlgorythm();
        stopwatch.stop();
        RSA.sendMessage('i', stopwatch.getTimeInMs() + "ms");
    }

    public void sendEncryptInfo() {
        if (this.n < 1) return;
        RSA.sendMessage('i', "E(" + this.e + ") N(" + this.n + ")");
    }

    public void sendDecryptInfo() {
        if (this.n < 1) return;
        RSA.sendMessage('i', "D(" + this.d + ") N(" + this.n + ")");
    }

    public String decrypt(String secret) {
        return RSA.decrypt(secret, this.d, this.n);
    }

    public String encrypt(String text) {
        if (this.e < 2) {
            return "";
        }
        return RSA.encrypt(text, this.e, this.n);
    }

    private long calculateA() {
        return ((this.p - 1) * (this.q - 1));
    }

    private long calculateN() {
        return (this.q * this.p);
    }

    private long checkE() {
        long biggerNumber = Math.max(this.p, this.q);
        long rand = RSA.nextPrime(biggerNumber);
        while (true) {
            if (RSA.ggt(rand, this.a) == 1) {
                break;
            } else {
                rand = RSA.nextPrime(rand);
            }
        }
        return rand;
    }
    private long calculateD() {
        long d = 0;
        while (this.e * d % this.a != 1) {
            d++;
        }
        return d;
    }

    private long calculateDAlgorythm() {
        ArrayList<long[]> test = new ArrayList<>();
        long rest;
        test.add(new long[]{this.a, 0});
        test.add(new long[]{this.e, 1});
        while (test.get(test.size() - 1)[0] != 1) {
            rest = (long) Math.ceil(test.get(test.size() - 2)[0] / test.get(test.size() - 1)[0]);
            test.add(new long[]{test.get(test.size() - 2)[0] - rest * test.get(test.size() - 1)[0], test.get(test.size() - 2)[1] - rest * test.get(test.size() - 1)[1],});
        }
        return test.get(test.size() - 1)[1];
    }
}