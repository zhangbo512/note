package com.zhang.Design_Patterns.delegation_mode;

/**
 * description
 *
 * @author zb 2019/05/26 22:14
 */
public class StaffC implements Staff {

    @Override
    public Object excute(String command) {
        System.out.println("员工C开始执行：" + command);
        return null;
    }
}
