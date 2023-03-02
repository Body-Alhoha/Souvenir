package dev.bodyalhoha.souvenir.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import dev.bodyalhoha.souvenir.Obfuscator;
import dev.bodyalhoha.souvenir.transformers.Transformer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class ObfUI extends JFrame {

        public ObfUI(){
            super("souvenir obf!!");
//            set up package file
            File packageFile = new File("package.txt");
            if(!packageFile.exists()){
                try{
                    packageFile.createNewFile();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


            try {
                FlatLightLaf.install();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setSize(300, 400);
            setResizable(false);
            init(packageFile);
            setVisible(true);
        }

        public static void main(String[] args){
            new ObfUI();
        }
        public void init(File packageFile){
            JPanel panel = new JPanel();
            add(panel);
            panel.setLayout(new BorderLayout());

            JPanel top = new JPanel();
            panel.add(top, BorderLayout.NORTH);
            top.setLayout(new FlowLayout());

            JLabel label = new JLabel("Souvenir Obfuscatar!!");
            top.add(label);

            JPanel bottom = new JPanel();
            panel.add(bottom, BorderLayout.SOUTH);
            bottom.setLayout(new FlowLayout());

            JButton button = new JButton("Obfuscate");
            bottom.add(button);

            JPanel center = new JPanel();
            panel.add(center, BorderLayout.CENTER);
            center.setLayout(new FlowLayout());

            JLabel label2 = new JLabel("Input file:");
            center.add(label2);

            JTextField path = new JTextField(10);
            center.add(path);

            JButton button2 = new JButton("Select");
            center.add(button2);


            JLabel label3 = new JLabel("Output file:");
            center.add(label3);

            JTextField pack = new JTextField(10);
            center.add(pack);

            JButton button3 = new JButton("Select");
            center.add(button3);

            JLabel label5 = new JLabel("Package:");
            center.add(label5);

            JTextField pack2 = new JTextField(10);
            pack2.setText(packageFile.getAbsolutePath());
            center.add(pack2);

            JButton button4 = new JButton("Open");
            center.add(button4);


//            add transformers list
            JLabel label4 = new JLabel("Transformers:");
            center.add(label4);

            JCheckBox[] boxes = new JCheckBox[Obfuscator.getInstance().getTransformers().size()];
            for(int i = 0; i < boxes.length; i++){
                boxes[i] = new JCheckBox(Obfuscator.getInstance().getTransformers().get(i).getClass().getSimpleName());
                center.add(boxes[i]);
            }


            button2.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
//                set the default directory to the current directory
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.showOpenDialog(null);
                File f = chooser.getSelectedFile();
                pack.setText(f.getAbsolutePath().replace(".jar", "_obf.jar"));
            });

            button3.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
//                set the default directory to the current directory
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.showOpenDialog(null);
                File f = chooser.getSelectedFile();
                pack.setText(f.getAbsolutePath());
            });

            button4.addActionListener(e -> {
                try{
                    Desktop.getDesktop().open(packageFile);
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            });


            button.addActionListener(e -> {
                String p = path.getText();
                String pa = pack.getText();
                if(p.isEmpty() || pa.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Input file or output is empty!");
                    return;
                }
                File f = new File(p);
                if(!f.exists()){
                    JOptionPane.showMessageDialog(null, "Input file does not exist!");
                    return;
                }
                try{
//                  transform handlers
                    java.util.List<Transformer> selected = new ArrayList<>();
                    for(int i = 0; i < boxes.length; i++){
                        if(boxes[i].isSelected()){
                            selected.add(Obfuscator.getInstance().getTransformers().get(i));
                        }
                    }
                    if(selected.isEmpty()){
                        JOptionPane.showMessageDialog(null, "No transformers selected!");
                        return;
                    }

                    selected.forEach(tf -> System.out.println("Selected transformer: " + tf.getClass().getSimpleName()));
//                    get inside package.txt file
                    String packageFormat="";
                    File packageFilef = new File(pack2.getText());
//                    read txt file
                    java.util.Scanner scanner = new java.util.Scanner(packageFilef);
                    String pscanner = scanner.nextLine();
                    for (int i = 0; i < pscanner.length(); i++) {
                        if (pscanner.charAt(i) == '.') {
                            packageFormat += "/";
                        } else {
                            packageFormat += pscanner.charAt(i);
                        }
                    }

                    scanner.close();

                    System.out.println("Package format: " + packageFormat);


                    Obfuscator.getInstance().obfuscate(path.getText(), pack.getText(), packageFormat, selected);
                }catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Obfuscation failed! Check the console for more info.");
                }
                JOptionPane.showMessageDialog(null, "Obfuscation done!");
            });

        }
}
