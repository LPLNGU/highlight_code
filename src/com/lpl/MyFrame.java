package com.lpl;

//myFrame基于JFrame的个人工具类

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;


public class MyFrame extends JFrame {
    JLabel label = new JLabel("输入");

    //创建JTextField，16表示16列，用于JTextField的宽度显示而不是限制字符个数
    JTextArea jTextArea = new JTextArea(100, 60);
    JButton button = new JButton("确定");
    JTextArea jEditorPane = new JTextArea(100, 60);

    //构造函数
    public MyFrame(String title) throws Exception {
        //继承父类，
        super(title);

        //内容面板
        JPanel jPanel = new JPanel();
        //添加控件
        jPanel.add(jTextArea, BorderLayout.WEST);
        jPanel.add(button, BorderLayout.CENTER);
        jPanel.add(jEditorPane, BorderLayout.EAST);
        this.add(jPanel);
        //按钮点击处理 lambda表达式
        button.addActionListener((e) -> {
            onButtonOk();
            analyse();
        });
    }

    //事件处理
    private void onButtonOk() {
        //获取输入内容
        String str = jTextArea.getText();
        //判断是否输入了
        if (str.equals("")) {
            Object[] options = {"OK ", "CANCEL "};
            JOptionPane.showOptionDialog(null, "您还没有输入 ", "提示", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        } else {
            JOptionPane.showMessageDialog(this, "您输入了：" + str);
        }
    }

    private final Set<String> keysWords = fileRead("./keyword.txt");
    private static final Set<Character> end = new HashSet<>(Arrays.asList('\n', '\t', ' '));

    public void analyse() {
        String str = jTextArea.getText();
        //输出结果
        StringBuilder resString = new StringBuilder(),
                tempString = new StringBuilder();
        //方便指针操作
        char[] cs = str.toCharArray();
        //存放指针
        int point = 0;
        //状态
        int state = 1;

        resString.append("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>output</title>\n" +
                "</head>\n" +
                "<body>\n");
        for (; point < cs.length; point++) {
            /**
             * 判断是否为字母或者数字，如果是字母，状态为1转化为2,
             * 如果是空格，判断是否为关键字，
             * 其他符号直接加入结果。
             */
            switch (state) {
                case 2:
                    if (isLetter(cs[point])) {
                        state = 2;
                        tempString.append(cs[point]);
                    } else {
                        if (isKey(tempString)) {
                            tempString.insert(0, "<font color = \"red\">");
                            tempString.append("</font>");
                            resString.append(tempString.append(cs[point]));
                        } else {
                            if (cs[point] == '\n') {
                                tempString.append("<br>");
                            }
                            //把非关键字加入结果
                            resString.append(tempString.append(cs[point]));
                        }
                        //清理字符串
                        tempString.delete(0, tempString.length());
                        state = 1;
                    }
                    break;
                default:
                    if (isLetter(cs[point])) {
                        state = 2;
                        tempString.append(cs[point]);
                    } else if (cs[point] == '\n') {
                        resString.append("<br>");
                        resString.append(cs[point]);
                    } else {
                        resString.append(cs[point]);
                    }
                    break;
            }
        }
        resString.append("</body>\n" +
                "</html>");
        jEditorPane.setText(resString.toString());
        outputFile(resString.toString(), "./output.html");
    }

    /**
     * 判断字符是否为一个字母
     *
     * @param c
     * @return
     */
    public Boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    /**
     * 判断stringbuilder是否为关键字
     *
     * @param stringBuilder
     * @return
     */
    public Boolean isKey(StringBuilder stringBuilder) {
        return keysWords.contains(stringBuilder.toString());
    }

    /**
     * 输入内容到文件
     *
     * @param input
     * @param fileName
     */
    public void outputFile(String input, String fileName) {
        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file)) {
            // if file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            //直接覆盖掉文件内容，并不读取之前的脏数据
            fileWriter.write(input);
            fileWriter.flush();
            fileWriter.close();
            System.out.println("写入文件成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读入字符串到文件中
     *
     * @throws Exception
     */
    public Set<String> fileRead(String filePath) throws Exception {
        //定义一个file对象，用来初始化FileReader
        File file = new File(filePath);
        //定义一个fileReader对象，用来初始化BufferedReader
        FileReader reader = new FileReader(file);
        //new一个BufferedReader对象，将文件内容读取到缓存
        BufferedReader bReader = new BufferedReader(reader);
        //定义一个字符串缓存，将字符串存放缓存中
        StringBuilder sb = new StringBuilder();
        String s = "";
        Set<String> res = new HashSet<>();
        //逐行读取文件内容，不读取换行符和末尾的空格
        while ((s = bReader.readLine()) != null) {
            res.add(s);
        }
        bReader.close();
        System.out.println(res);
        return res;
    }
}