package com.app.base.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.system.session.service.MySessionService;

import java.util.ArrayList;
import java.util.List;

public abstract class WebController extends BaseController {

	protected String autoView(String name) {
        final Resource resource = getApplicationContext().getResource("classpath:/templates/" + name + ".html");
        if (resource != null && resource.exists()) {
            return name;
        }
        return "_" + name;
    }

    //过滤掉数组里面的空字符串
    public List removeNullStringArray(String[] arrayString) {
        List<String> list1 = new ArrayList<String>();
        for (int i=0 ;i<arrayString.length; i++) {
            if(arrayString[i]!=null && arrayString[i].length()!=0){ //过滤掉数组arrayString里面的空字符串
                list1.add(arrayString[i]);
            }
        }
        return list1;
    }
}
