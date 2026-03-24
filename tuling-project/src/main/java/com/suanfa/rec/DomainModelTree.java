package com.suanfa.rec;

import java.util.List;

/**
 * @Description
 * @Author: xiewu
 * @Date: 2022/4/24 15:01
 */
public class DomainModelTree {

    private String value;
    private List<DomainModelTree> children;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<DomainModelTree> getChildren() {
        return children;
    }

    public void setChildren(List<DomainModelTree> children) {
        this.children = children;
    }
}
