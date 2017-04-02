package com.company;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        //Get Input
        Scanner scanner = new Scanner(System.in);
        //System.out.println("Input string less than 50 characters: ");
        String msg = scanner.nextLine();
        while(msg.length()>51){
            System.out.println("Input string less than 50 characters: ");
            msg = scanner.nextLine();
        }

        BigInteger data = new BigInteger(msg.getBytes());

        //Generate a Key Pair
        KeyPair key = new KeyPair();
        Map<String, BigInteger> pub = key.getPublicKeyPair();
        Map<String, BigInteger> priv = key.getPrivateKeyPair();
        System.out.println("Private Key: " + priv.toString());
        System.out.println("Public Key: " + pub.toString());

        //cipher
        BigInteger m = data;
        BigInteger encryptedText = RSA.cipher(pub, m);
        System.out.println("Encrypted Text: " + new String(encryptedText.toByteArray()));

        //decipher
        BigInteger decryptedText = RSA.decipher(priv, encryptedText);
        System.out.println("Decrypted Text: " + new String(decryptedText.toByteArray()));

    }
}


class KeyPair {
    private Map<String, BigInteger> priv = new HashMap<>();
    private Map<String, BigInteger> pub = new HashMap<>();

    public KeyPair(){
        Random rnd = new Random();
        BigInteger p = new BigInteger(256, 500, rnd);
        BigInteger q = new BigInteger(256, 500, rnd);
        BigInteger On = p.subtract(new BigInteger("1")).multiply(q.subtract(new BigInteger("1")));
        BigInteger e;
        for ( int i = 2; ;i++ ){
            BigInteger temp_e = new BigInteger(String.valueOf(i));
            if(temp_e.gcd(On).equals(new BigInteger("1"))){
                e = temp_e;
                break;
            }
        }

        priv.put("d", e.modInverse(On));
        priv.put("n", p.multiply(q));
        pub.put("e", e);
        pub.put("n", p.multiply(q));

    }

    public Map<String, BigInteger> getPrivateKeyPair(){
        return priv;
    }

    public Map<String, BigInteger> getPublicKeyPair(){
        return pub;
    }



}


class RSA {
    public static BigInteger cipher( Map<String, BigInteger> pub, BigInteger m){
        //Get public key pair value
        BigInteger e = pub.get("e");
        BigInteger n = pub.get("n");

        //Encrypt
        BigInteger c = m.modPow(e, n);
        return c;
    }

    public static BigInteger decipher(Map<String, BigInteger> priv, BigInteger c){
        //Get private key pair value
        BigInteger d = priv.get("d");
        BigInteger n = priv.get("n");

        //Decrypt
        BigInteger m = c.modPow(d, n);
        return m;
    }
}


