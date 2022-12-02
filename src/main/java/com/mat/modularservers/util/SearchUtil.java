//package com.mat.modularservers.util;
//
//import java.util.Arrays;
//import java.util.LinkedList;
//
//public class SearchUtil {
//    public static Object[] query(BooleanCallable callable, Iterable iterable, Object... objects) throws Exception {
//        LinkedList<Object> sockets = new LinkedList<>();
//        for (Object obj2 : iterable) {
//            for (Object obj : objects) {
//                if (callable.run(obj, obj2) && !sockets.contains(obj2)) sockets.add(obj2);
//            }
//        }
//        return sockets.toArray();
//    }
//}
