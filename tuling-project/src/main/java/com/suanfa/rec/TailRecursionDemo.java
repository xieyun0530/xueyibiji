package com.suanfa.rec;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 尾递归demo
 * @Author: xiewu
 * @Date: 2022/4/24 14:40
 */
public class TailRecursionDemo {

    public static void main(String[] args) {
        DomainModelTree domainModelTree1 = new DomainModelTree();
        domainModelTree1.setValue("1");
        DomainModelTree domainModelTree2 = new DomainModelTree();
        domainModelTree2.setValue("2");
        List<DomainModelTree> children1 = new ArrayList<>();
        children1.add(domainModelTree2);
        domainModelTree1.setChildren(children1);

        DomainModelTree domainModelTree3 = new DomainModelTree();
        domainModelTree3.setValue("3");
        List<DomainModelTree> children2 = new ArrayList<>();
        children2.add(domainModelTree3);
        domainModelTree2.setChildren(children2);

        DomainModelTree domainModelTree4 = new DomainModelTree();
        domainModelTree4.setValue("4");
        List<DomainModelTree> children3 = new ArrayList<>();
        children3.add(domainModelTree4);
        domainModelTree3.setChildren(children3);

        DomainModelTree domainModelTree5 = new DomainModelTree();
        domainModelTree5.setValue("5");
        List<DomainModelTree> children4 = new ArrayList<>();
        children4.add(domainModelTree5);
        domainModelTree4.setChildren(children4);

        DomainDataModelTreeDto domainDataModelTreeDto = new DomainDataModelTreeDto();
        beanCopyDomainDataModelTreeDto(domainModelTree1,domainDataModelTreeDto);
        beanCopyDomainDataModelTreeDto(domainModelTree1);
    }

    public static void beanCopyDomainDataModelTreeDto(DomainModelTree domainModelTree, DomainDataModelTreeDto domainDataModelTreeDto) {
        System.out.println("尾递归："+domainModelTree.getValue());
        BeanUtils.copyProperties(domainModelTree, domainDataModelTreeDto);
        if (null != domainModelTree.getChildren()) {
            List<DomainDataModelTreeDto> children = new ArrayList<>();
            domainDataModelTreeDto.setChildren(children);
            for (DomainModelTree child : domainModelTree.getChildren()) {
                DomainDataModelTreeDto domainDataModelTreeDtoChildren = new DomainDataModelTreeDto();
                children.add(domainDataModelTreeDtoChildren);
                beanCopyDomainDataModelTreeDto(child, domainDataModelTreeDtoChildren);
            }
        }
    }

    public static DomainDataModelTreeDto beanCopyDomainDataModelTreeDto(DomainModelTree domainModelTree) {
        System.out.println("递归："+domainModelTree.getValue());
        DomainDataModelTreeDto domainDataModelTreeDto = new DomainDataModelTreeDto();
        BeanUtils.copyProperties(domainModelTree, domainDataModelTreeDto);
        if (null != domainModelTree.getChildren()) {
            List<DomainDataModelTreeDto> children = new ArrayList<>();
            for (DomainModelTree child : domainModelTree.getChildren()) {
                DomainDataModelTreeDto domainDataModelTreeDtoChild = beanCopyDomainDataModelTreeDto(child);
                children.add(domainDataModelTreeDtoChild);
            }
            domainDataModelTreeDto.setChildren(children);
        }
        System.out.println("递归domainDataModelTreeDto："+domainDataModelTreeDto.getValue());
        return domainDataModelTreeDto;
    }

}
