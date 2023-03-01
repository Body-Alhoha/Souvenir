package dev.bodyalhoha.souvenir.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import dev.bodyalhoha.souvenir.Obfuscator;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ObfUI extends JFrame {

        public ObfUI(){
            super("Body Obf!!");
            try {
                FlatLightLaf.install();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setSize(300, 150);
            setResizable(false);
            init();
            setVisible(true);
        }

        public static void main(String[] args){
            new ObfUI();
        }
        public void init(){
            JPanel panel = new JPanel();
            add(panel);
            panel.setLayout(new BorderLayout());

            JPanel top = new JPanel();
            panel.add(top, BorderLayout.NORTH);
            top.setLayout(new FlowLayout());

            JLabel label = new JLabel("Body Alhoha Obfuscatar!!");
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

            button2.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
//                set the default directory to the current directory
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.showOpenDialog(null);
                File f = chooser.getSelectedFile();
                path.setText(f.getAbsolutePath());
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
                    Obfuscator.getInstance().obfuscate(path.getText(), path.getText());
                }catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Obfuscation failed! Check the console for more info.");
                }
                JOptionPane.showMessageDialog(null, "Obfuscation done!");
            });

        }
}
