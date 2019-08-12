package com.example.lib;

import com.example.lib.fflag.Flags;

public class LibSample {

    static void test() {
        if (Flags.logs) {
            System.out.println("Hello world");
        }
    }
}
