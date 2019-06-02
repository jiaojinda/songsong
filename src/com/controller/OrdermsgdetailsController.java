package com.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.dao.OrdermsgdetailsDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
public class OrdermsgdetailsController extends BaseController {
    @Resource
    OrdermsgdetailsDAO ordermsgdetailsDAO;

    //查看数据统计
    @RequestMapping("/admin/countData")
    public String orderDetails(HttpServletRequest request){
        List<Map<String,Object>> list = ordermsgdetailsDAO.countData();
        String nameArr = "";
        String dataArr = "";
        for(Map<String,Object> map : list){
            nameArr += map.get("NAME");
            nameArr += ",";
            dataArr += map.get("num");
            dataArr += ",";
        }
        if(nameArr.length()>0){
            nameArr = nameArr.substring(0,nameArr.length()-1);
        }else{
            nameArr = "暂无数据";
        }
        if(dataArr.length()>0){
            dataArr = dataArr.substring(0,dataArr.length()-1);
        }else{
            dataArr = "0";
        }

        request.setAttribute("nameArr", nameArr);
        request.setAttribute("dataArr", dataArr);
        return "admin/countData";
    }

}
