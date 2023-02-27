package dev.bodyalhoha.souvenir;

import java.io.File;

public class Main {

    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("Usage: java -jar obf.jar file.jar package");
            return;
        }
        File f = new File(args[0]);
        if(!f.exists()){
            System.out.println("Input file does not exists!");
            return;
        }

        try{
            Obfuscator.getInstance().obfuscate(args[0], args[1]);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("[core] Obfuscator done.");

    }

}
