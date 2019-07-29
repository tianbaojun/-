package com.tabjin.twelveanimals.model;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CompainModel {
    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();
        for (int i=1;i<50;i++){
            list.add(i+"");
        }

        ArrayList<ArrayList<String>> lists = new ArrayList<>();
        ArrayList<Integer> nums = new ArrayList<>();
        ArrayList<String> list1 = new ArrayList<>();
        for (int i=1;i<10;i++){
            list1.add(i+"");
        }
        lists.add(list1);
        nums.add(2);

        ArrayList<String> list2= new ArrayList<>();
        for (int i=11;i<20;i++){
            list2.add(i+"");
        }
        lists.add(list2);
        nums.add(2);

        ArrayList<String> list3= new ArrayList<>();
        for (int i=20;i<23;i++){
            list3.add(i+"");
        }
        lists.add(list3);
        nums.add(3);

        ArrayList<String> sha= new ArrayList<>();
        for (int i=30;i<38;i++){
            sha.add(i+"");
        }
//        ArrayList<String> result = new ArrayList<>();
//        result = compainDan3Sha1(list,list1,2,list2,1,list3,0,sha,7);
//        System.out.println("---------------有"+result.size()+"个结果");
//        for(String str:result) {
//            System.out.println(str);
//        }
        screenByDansSha(7,lists,nums,list);
    }

    public static final String SEP = "  ";

    public static void screenByDansSha(int renji, List<ArrayList<String>> lists,List<Integer> nums,ArrayList<String> all){
        if(renji>all.size()){
            System.out.println("任几数据错误");
            return;
        }
        for(Integer num: nums){
            if(num>renji){
                System.out.println("出码数据错误");
                return;
            }
        }
        for(ArrayList<String> list: lists){
            if(list.size()>all.size()){
                System.out.println("出码数据错误");
                return;
            }
        }
        ArrayList<String> result = new ArrayList<>();
        fullPermutation(lists,nums,all,0,0,7,result,"");
//        return result;

    }

    public static void  fullPermutation(List<ArrayList<String>> lists,List<Integer> nums,ArrayList<String> list, int index, int startIndex,int num, ArrayList<String> result,String prefix) {
        if(num<1){
            return;
        }
        start:for (int i = startIndex;i<list.size()-num+index+1;i++) {
            String str = prefix+list.get(i);
            if (isStrSuit(lists, nums, str)) break start;
            if(index == num-1){
//                result.add(prefix+list.get(i));
                System.out.println(str);
            }else{
                fullPermutation(list,index+1,i+1,num,result,str+SEP);
            }
        }
    }

    private static boolean isStrSuit(List<ArrayList<String>> lists, List<Integer> nums, String prefix) {
        for(int j=0;j<lists.size();j++){
            int count = 0;
            for(String str:lists.get(j)){
                if(prefix.contains(str)){
                    count++;
                }
                if(count > nums.get(j)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取所有的排列组合结果
     * @param list 组合
     * @param index
     * @param startIndex
     * @param num
     * @param result 返回结果结合
     * @param prefix
     */
    public static void  fullPermutation(ArrayList<String> list, int index, int startIndex,int num, ArrayList<String> result,String prefix) {
        if(num<1){
            return;
        }
        for (int i = startIndex;i<list.size()-num+index+1;i++) {
            if(index == num-1){
                result.add(prefix+list.get(i));
                System.out.println(prefix+list.get(i));
            }else{
                fullPermutation(list,index+1,i+1,num,result,prefix+list.get(i)+SEP);
            }
        }
    }

    /**
     * 封装下获取排列组合的方法
     * @param list
     * @param num
     * @return
     */
    public static ArrayList<String> fullPermutation(ArrayList<String> list,int num){
        ArrayList<String> result = new ArrayList<String>();
        fullPermutation(list,0,0,num,result,"");
        return result;
    }

    /**
     * 两个集合，从一个当中选mustNum个，总共选择renji个
     * @param list
     * @param list2
     * @param renji
     * @param mustNum
     * @return 结果集合
     */
    public static ArrayList<String> compin(ArrayList<String> list,ArrayList<String> list2,int renji,int mustNum){
        if(renji<0||mustNum<0){
            return new ArrayList<>();
        }
       ArrayList<String> killResult = new ArrayList<>();
       fullPermutation(list,0,0,mustNum,killResult,"");
        if(renji == mustNum){
            return killResult;
        }
       ArrayList<String> unKillResult = new ArrayList<>();
       fullPermutation(list2,0,0,renji-mustNum,unKillResult,"");
       if(mustNum == 0){
           return unKillResult;
       }

       ArrayList<String> result = new ArrayList<>();
       for(int i=0;i<killResult.size();i++){
           for(int j=0;j<unKillResult.size();j++){
               result.add(killResult.get(i)+SEP+unKillResult.get(j));
           }
       }
       return result;
    }


    /**
     * 三个胆码 一个杀码
     * @param all
     * @param dan1
     * @param num1
     * @param dan2
     * @param num2
     * @param dan3
     * @param num3
     * @param sha
     * @param chuNum
     * @return
     */
    public static ArrayList<String> compainDan3Sha1(ArrayList<String> all,ArrayList<String> dan1,int num1,ArrayList<String> dan2,int num2,ArrayList<String> dan3,int num3,ArrayList<String> sha,int chuNum){
        ArrayList<String> result = new ArrayList<String>();
        removeKill(dan1,sha);
        removeKill(dan2,sha);
        removeKill(dan3,sha);
        removeKill(all,sha);
        removeKill(all,dan1);
        removeKill(all,dan2);
        removeKill(all,dan3);
        //如果数据错误
        if(dan1.size()<num1 || dan2.size()<num2 || dan3.size()<num3 || chuNum<num1||chuNum<num2||chuNum<num3){
            System.out.println("没有结果的简单情况,更改条件！");
            return result;
        }
        //排除没有结果的简单情况
        int duc12 = getDucNum(dan1,dan2);
        int duc23 = getDucNum(dan2,dan3);
        int duc13 = getDucNum(dan1,dan3);
        if(num1+num2+num3-duc12-duc13-duc23>chuNum){
            System.out.println("没有结果的简单情况！");
            return result;
        }
        ArrayList<String> s1 = fullPermutation(dan1,num1);
        ArrayList<String> s2 = fullPermutation(dan2,num2);
        ArrayList<String> s3 = fullPermutation(dan3,num3);
        ArrayList<String> danResult = compainLists(s1,s2,s3);
        Iterator<String> iterator = danResult.iterator();
        if(danResult.isEmpty()){
            result.addAll(fullPermutation(all,chuNum));
        }else{
            while (iterator.hasNext()){
                String danStr = iterator.next();
                ArrayList<String> danNums = new ArrayList<>(Arrays.asList(danStr.split(SEP)));
                if(getDucNum(danNums,dan1)!=num1||getDucNum(danNums,dan2)!=num2||getDucNum(danNums,dan3)!=num3){
                    iterator.remove();
                }else{
                    if(chuNum==danNums.size()){
                        result.add(danStr);
                    }else {
                        ArrayList<String> per = fullPermutation(all, chuNum - danNums.size());
                        for (String str : per) {
                            result.add(danStr + str);
                        }
                    }
                }
            }
        }
        return result;

    }

    /**
     * 得到重复的元素个数
     * @param list
     * @param list2
     * @return
     */
    public static int getDucNum(List<String> list,List<String> list2){
        int num = 0;
        for(String str:list){
            if(list2.contains(str)){
                num++;
            }
        }
        return num;
    }

    @SafeVarargs
    public static ArrayList<String> compainLists(ArrayList<String>... lists){
        ArrayList<String> result = new ArrayList<>();
        if(lists==null||lists.length==0){
            return  result;
        }
        compainLists(result,0,"",lists);
        return result;
    }

    /**
     * 多个集合之间组合 并去重 且排序
     * @param index
     * @param prefix
     * @param lists
     * @return
     */
    public static void compainLists(ArrayList<String> result,int index,String prefix,ArrayList<String>... lists){
        if(result == null||lists == null||lists.length==0){
            return;
        }
        if(lists[index].size() == 0){
            if(index == lists.length-1){
                if(!prefix.isEmpty()){
                    result.add(prefix);
                }
            }else{
                compainLists(result, index + 1, prefix, lists);
            }
        }else {
            for (String bean : lists[index]) {
                StringBuilder sb = getStrRemoveDup(prefix, bean);
                if (index == lists.length - 1) {
                    result.add(sb.toString());
                } else {
                    compainLists(result, index + 1, sb.toString(), lists);
                }
            }
        }
    }

    /**
     * 两个字符串去重 且排序
     * @param prefix
     * @param bean
     * @return
     */
    @NonNull
    private static StringBuilder getStrRemoveDup(String prefix, String bean) {
        if(prefix==null){
            prefix = "";
        }
        if(bean == null){
            bean = "";
        }
        StringBuilder sb = new StringBuilder();
        ArrayList<String> beanList = new ArrayList<>(Arrays.asList(bean.split(SEP)));
        ArrayList<String> preList = new ArrayList<>(Arrays.asList(prefix.split(SEP)));
        removeKill(beanList, preList);
        beanList.addAll(preList);
        //排序
        Collections.sort(beanList);
        for(String str:beanList){
            if(!str.isEmpty()) {
                sb.append(str).append(SEP);
            }
        }
        return sb;
    }

    /**
     * 去除杀码
     * @param data
     * @param kills
     */
    public static void removeKill(ArrayList<String> data, ArrayList<String> kills){
        if(data.isEmpty()||kills.isEmpty()){
            return;
        }
        Iterator<String> iterator = data.iterator();
        while(iterator.hasNext()){
            String str = iterator.next();
            for(String kill:kills){
                if(kill.equals(str)){
                    iterator.remove();
                }
            }
        }
    }

    /**
     * 数字替换生肖
     * @param list
     */
    public void repleaceAnimalWithNumber(ArrayList<String> list){
        if(list != null && list.size()>0)
        {
            ArrayList<String> number = new ArrayList<>();
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()){
                String next = iterator.next();
                if(Constant.Companion.getAnimalMap().containsKey(next)){
                    if(Constant.Companion.getAnimalMap().get(next)!=null) {
                        number.addAll(Constant.Companion.getAnimalMap().get(next));
                    }
                    iterator.remove();
                }
            }
            list.addAll(number);
        }
    }
}
