package com.lpl;


import javax.swing.JFrame;

public class SwingDemo {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });

    }

    private static void createGUI() {
        //创建一个窗口，创建一个窗口
        MyFrame frame = null;
        try {
            frame = new MyFrame("html转化器");
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //设置窗口大小
        frame.setSize(1920, 1080);

        //显示窗口
        frame.setVisible(true);
    }

}
