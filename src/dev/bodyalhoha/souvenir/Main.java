package dev.bodyalhoha.souvenir;

import dev.bodyalhoha.souvenir.transformers.Transformer;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args){

//        create a package txt file on the jar directory
        File packageFile = new File("package.txt");
        if(!packageFile.exists()){
            try{
                packageFile.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
//        check if run by double clicks
        if(args.length < 2){
            System.out.println("Usage: java -jar obf.jar file.jar outfile.jar package transform1 transform2 ...");
            return;
        }
        File f = new File(args[0]);
        if(!f.exists()){
            System.out.println("Input file does not exist!");
            return;
        }
        // TODO: add transformers list to args
        List<Transformer> selected = Obfuscator.getInstance().getTransformers();


        try{
            Obfuscator.getInstance().obfuscate(args[0], args[1], args[2], selected);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("[core] Obfuscator done.");

    }

}
