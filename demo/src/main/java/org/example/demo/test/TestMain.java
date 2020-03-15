package org.example.demo.test;

import static org.example.demo.test.TestLocal.testLocal;
import static org.example.demo.test.TestRemote.testRemote;

public class TestMain {

    public static void main(String[] args) {
        testLocal();
        testRemote();
    }
}
