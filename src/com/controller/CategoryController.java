package com.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.dao.*;
import com.entity.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.*;

@Controller
public class CategoryController extends BaseController {
	@Resource
	CategoryDAO categoryDAO;
	
	@RequestMapping("/admin/categoryList")
	public String categoryList(HttpServletRequest request) {
		String index = request.getParameter("index");
	   	int pageindex = 1;
	   	if(index!=null){
	   		 pageindex = Integer.parseInt(index);
	   	}
   	    Page<Object> page = PageHelper.startPage(pageindex,6);
		List<Category> list = categoryDAO.selectAll();
		request.setAttribute("list", list);
		request.setAttribute("index", page.getPageNum());
		request.setAttribute("pages", page.getPages());
		request.setAttribute("total", page.getTotal());
		return "admin/categorylist";
	}
	
	@RequestMapping("admin/searchCategory")
	public String searchUser(HttpServletRequest request){
		String index = request.getParameter("index");
		String key = request.getParameter("key");
	   	int pageindex = 1;
	   	if(index!=null){
	   		 pageindex = Integer.parseInt(index);
	   	}
   	    Page<Object> page = PageHelper.startPage(pageindex,6);
		List<Category> list = categoryDAO.search(key);
		request.setAttribute("list", list);
		request.setAttribute("key", key);
		request.setAttribute("index", page.getPageNum());
		request.setAttribute("pages", page.getPages());
		request.setAttribute("total", page.getTotal());
		return "admin/categorysearchlist";
	}
	
	
	@RequestMapping("/admin/categoryAdd")
	public String dingdanAdd(Category ct,HttpServletRequest request){
		categoryDAO.add(ct);
		return "redirect:categoryList.do";
	}
	
	@RequestMapping("/admin/showCategory")
	public String showCategory(int id,HttpServletRequest request){
		Category category =  categoryDAO.findById(id);
		request.setAttribute("category", category);
		return "admin/categoryedit";
	}
	
	@RequestMapping("/admin/categoryEdit")
	public String categoryEdit(Category category,HttpServletRequest request){
		categoryDAO.update(category);
		request.setAttribute("category", category);
		return "redirect:categoryList.do";
	}
	
	@RequestMapping("admin/categoryDelAll")
	public String categoryDelAll(HttpServletRequest request,HttpServletResponse response){
		String vals = request.getParameter("vals");
		String[] val = vals.split(",");
		for(int i=0;i<val.length;i++){
			categoryDAO.delete(Integer.parseInt(val[i]));
		}
		return "redirect:newsList.do";
	}
	
	
	

}
