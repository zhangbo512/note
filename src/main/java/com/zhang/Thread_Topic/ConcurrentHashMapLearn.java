package com.zhang.Thread_Topic;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description
 *
 * @author zb 2019/07/06 18:01
 */
public class ConcurrentHashMapLearn {

    public static void main(String[] args) {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap(1000);




    }


    static void hashMap() {
        HashMap<String,Integer> hashMap = new HashMap();

        // putVal(hash(key), key, value, false, true);
        /**
         *  Node<K,V>[] tab; Node<K,V> p; int n, i;
         *
         *  Node:
         *         final int hash;
         *         final K key;
         *         V value;
         *         Node<K,V> next;
         *
         *    //table== null 或者 长度为0 ，进行扩容
         *   if ((tab = table) == null || (n = tab.length) == 0)
         *             n = (tab = resize()).length;
         *
         *  resize:
         *     两倍扩容
         *     if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&oldCap >= DEFAULT_INITIAL_CAPACITY)
         *      将旧的tableNode赋值给新的table
         *      for (int j = 0; j < oldCap; ++j)
         *           //判断节点是否有链表
         *           if (e.next == null)
         *             newTab[e.hash & (newCap - 1)] = e;
         *           //是否是红黑树（链表超过8会转为红黑树）
         *           else if (e instanceof TreeNode)
         *
         *
         */
        hashMap.put("word",3);
    }

    static void concurrentHashMap() {

    }
}
