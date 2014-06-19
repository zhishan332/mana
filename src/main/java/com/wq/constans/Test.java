package com.wq.constans;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: wangq
 * Date: 14-6-18
 * Time: 下午8:37
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main(String[] args) {
        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        for(UIManager.LookAndFeelInfo ll:info){
            System.out.println(ll.getClassName());
        }
    }
}
