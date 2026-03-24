package com.suanfa.rec;

import java.util.List;

/**
 * @Description
 * @Author: xiewu
 * @Date: 2022/4/24 15:01
 */
public class DomainDataModelTreeDto {

    private String value;
    private List<DomainDataModelTreeDto> children;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<DomainDataModelTreeDto> getChildren() {
        return children;
    }

    public void setChildren(List<DomainDataModelTreeDto> children) {
        this.children = children;
    }
}
