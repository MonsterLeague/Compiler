import UI.CompilerFrame;

import javax.swing.*;


public class Compiler {
    public static void main(String[] args){
        CompilerFrame CF = new CompilerFrame();
        CF.setResizable(false);
        CF.setVisible(true);
        CF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
