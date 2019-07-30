package com.zx.utils;

import java.util.ArrayList;
import java.util.List;

import com.zx.entity.Data;
import com.zx.entity.User;
import com.zx.hibernate.CRUD;

public class ChartUtils {
	  //创建初始化lsit对象;
		private static List<Data> list = new ArrayList<Data>();

			
		
		
		
		// 获得结果;
		public static List<Data> getDataList() {
			list.removeAll(list);
			list.add(new Data("0-20",0));
			list.add(new Data("20-40",0));
			list.add(new Data("40-60",0));
			list.add(new Data("60-80",0));
			list.add(new Data("80-100",0));
			List<User> userlist = CRUD.QueryAll();
			for(int i = 0; i < userlist.size();i++) {
				int age = userlist.get(i).getAge();
				System.out.println(age);
				if(age>0 && age<=20) {
					list.get(0).addNum();
				}else if(age>20&&age<=40) {
					list.get(1).addNum();
				}else if(age>40&&age<=60) {
					list.get(2).addNum();
				}else if(age>60&&age<=80) {
					list.get(3).addNum();
				}else if(age>80&&age<=100) {
					list.get(4).addNum();
				}
			}
			return list;
		}
}
