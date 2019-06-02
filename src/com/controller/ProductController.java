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
import com.util.CommDAO;

import java.util.*;

@Controller
public class ProductController extends BaseController {
	@Resource
	ProductDAO productDAO;
	@Resource
	CategoryDAO categoryDAO;
	@Resource
	CartDAO cartDAO;
	@Resource
	MemberDAO memberDAO;
	@Resource
	CommentDAO commentDAO;
	@Resource
	OrdermsgdetailsDAO ordermsgdetailsDAO;
	
	//后台查询菜品列表
	@RequestMapping("/admin/productList")
	public String productList(HttpServletRequest request) {
		List<Category> ctlist = (List<Category>)categoryDAO.selectAll();
		String index = request.getParameter("index");
	   	int pageindex = 1;
	   	if(index!=null){
	   		 pageindex = Integer.parseInt(index);
	   	}
   	    Page<Object> page = PageHelper.startPage(pageindex,6);
		List<Product> list = productDAO.selectAll();
		for(int i=0;i<list.size();i++){
			Category category = categoryDAO.findById(Integer.parseInt(list.get(i).getCategoryid()));
			list.get(i).setCategory(category);
		}
		request.setAttribute("list", list);
		request.setAttribute("ctlist", ctlist);
		request.setAttribute("index", page.getPageNum());
		request.setAttribute("pages", page.getPages());
		request.setAttribute("total", page.getTotal());
		return "admin/productlist";
	}
	
	//后台搜索菜品列表
	@RequestMapping("admin/searchProduct")
	public String searchUser(HttpServletRequest request){
		List<Category> ctlist = (List<Category>)categoryDAO.selectAll();
		String index = request.getParameter("index");
		String key = request.getParameter("key");
		String key1 = request.getParameter("key1");
	   	int pageindex = 1;
	   	if(index!=null){
	   		 pageindex = Integer.parseInt(index);
	   	}
   	    Page<Object> page = PageHelper.startPage(pageindex,6);
		List<Product> list = productDAO.search(key,key1);
		for(int i=0;i<list.size();i++){
			Category category = categoryDAO.findById(Integer.parseInt(list.get(i).getCategoryid()));
			list.get(i).setCategory(category);
		}
		request.setAttribute("list", list);
		request.setAttribute("ctlist", ctlist);
		request.setAttribute("key", key);
		request.setAttribute("key1", key1);
		request.setAttribute("index", page.getPageNum());
		request.setAttribute("pages", page.getPages());
		request.setAttribute("total", page.getTotal());
		return "admin/productsearchlist";
	}
	
	//查询菜品类别
	@RequestMapping("/admin/categorySelect")
	public String categorySelect(HttpServletRequest request){
		List<Category> list = categoryDAO.selectAll();
		request.setAttribute("list", list);
		return "admin/productadd";
	}
	
	//菜品添加
	@RequestMapping("/admin/productAdd")
	public String productAdd(Product product,HttpServletRequest request){
		productDAO.add(product);
		return "redirect:productList.do";
	}
	
	
	//后台菜品查询
	@RequestMapping("/admin/showProduct")
	public String showproduct(int id,HttpServletRequest request){
		Product product =  productDAO.findById(id);
		List<Category> list = categoryDAO.selectAll();
		request.setAttribute("list", list);
		request.setAttribute("product", product);
		return "admin/productedit";
	}
	
	//前台菜品查询
	@RequestMapping("productDetails")
	public String productDetails(int id,HttpServletRequest request){
		String msg = request.getParameter("msg");
		//购物车
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
			List<Cart> cartlist = cartDAO.selectMyProductList(member.getId());
			String totalstr = "";
			double total = 0.0;
			for(int i=0;i<cartlist.size();i++){
				Member m = memberDAO.findById(cartlist.get(i).getMemberid());
				Product product = productDAO.findById(cartlist.get(i).getProductid());
				cartlist.get(i).setMember(m);
				cartlist.get(i).setProduct(product);
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPrice();
			}
			totalstr = String.format("%.2f", total);
			request.setAttribute("cartlist", cartlist);
			request.setAttribute("totalstr", totalstr);
		}
		
		Product product =  productDAO.findById(id);
		List<Product> list = productDAO.selectCorrelation(id,product.getCategoryid());
		Category category = categoryDAO.findById(Integer.parseInt(product.getCategoryid()));
		product.setCategory(category);
		
		if(msg!=null){
			request.setAttribute("msg", msg);
		}
		
		//评论
		List<Comment> commentlist = commentDAO.selectProduct(id);
		Double score = 0.0;
		for(int i=0;i<commentlist.size();i++){
			Member m = memberDAO.findById(commentlist.get(i).getMemberid());
			commentlist.get(i).setMember(m);
			score+= Double.parseDouble(String.valueOf(commentlist.get(i).getQuality()));
		}
		//好感度
		double averageStr=0.0;
		if(commentlist.size()!=0){
		double average = score/Double.parseDouble(String.valueOf(commentlist.size()));
		averageStr = Double.valueOf(String.format("%.1f", average));
		}
		
		//菜品类别
		List<Category> ctlist = categoryDAO.selectAll();
		request.setAttribute("ctlist", ctlist);
		request.setAttribute("product", product);
		request.setAttribute("averageStr", averageStr);
		request.setAttribute("commentlist", commentlist);
		request.setAttribute("list", list);
		return "productdetails";
	}
	
	//菜品编辑
	@RequestMapping("/admin/productEdit")
	public String productEdit(Product product,HttpServletRequest request){
		productDAO.update(product);
		request.setAttribute("product", product);
		return "redirect:productList.do";
	}
	//菜品删除
	@RequestMapping("admin/productDelAll")
	public String productDelAll(HttpServletRequest request,HttpServletResponse response){
		String vals = request.getParameter("vals");
		String[] val = vals.split(",");
		for(int i=0;i<val.length;i++){
			productDAO.delete(Integer.parseInt(val[i]));
		}
		return "redirect:productList.do";
	}
	
	//添加评论
	@RequestMapping("commentAdd")
	public String commentAdd(Comment comment, HttpServletRequest request){
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
		List<Ordermsgdetails> list = ordermsgdetailsDAO.selectOne(comment.getProductid(),member.getId());
		if(list.size()!=0){
		comment.setMemberid(member.getId());
		commentDAO.add(comment);
		return "redirect:productDetails.do?id="+comment.getProductid()+"&msg=suc";
		}else{
			return "redirect:productDetails.do?id="+comment.getProductid()+"&msg=msg";
		}	
		}else{
			return "login";
		}
	}
	
	
	//显示菜品列表页
	@RequestMapping("product_List")
	public String listProduct(int categoryid, HttpServletRequest request){
		//菜品类别
		List<Category> ctlist = categoryDAO.selectAll();
		request.setAttribute("ctlist", ctlist);
		//菜品销量排行
		//CommDAO dao  = new CommDAO();
		//List<HashMap> phlist = dao.select("select sum(o.num) as num,o.productid  from ordermsgdetails o,ordermsg g where g.shstatus='已收货' and g.orderno=o.orderno GROUP BY productid order by num desc");
		
		
		//购物车
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
			List<Cart> cartlist = cartDAO.selectMyProductList(member.getId());
			String totalstr = "";
			double total = 0.0;
			for(int i=0;i<cartlist.size();i++){
				Member m = memberDAO.findById(cartlist.get(i).getMemberid());
				Product product = productDAO.findById(cartlist.get(i).getProductid());
				cartlist.get(i).setMember(m);
				cartlist.get(i).setProduct(product);
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPrice();
			}
			totalstr = String.format("%.2f", total);
			request.setAttribute("cartlist", cartlist);
			request.setAttribute("totalstr", totalstr);
		}
		//显示菜品列表
		String index = request.getParameter("index");
	   	int pageindex = 1;
	   	if(index!=null){
	   		 pageindex = Integer.parseInt(index);
	   	}
   	    Page<Object> page = PageHelper.startPage(pageindex,6);
		List<Product> list = productDAO.selectCategory(categoryid);
		for(int i=0;i<list.size();i++){
			List<Comment> commentlist = commentDAO.selectProduct(list.get(i).getId());
			Double score = 0.0;
			for(int a=0;a<commentlist.size();a++){
				score+= Double.parseDouble(String.valueOf(commentlist.get(a).getQuality()));
			}
			double averageStr=0.0;
			if(commentlist.size()!=0){
			double average = score/Double.parseDouble(String.valueOf(commentlist.size()));
			averageStr = Double.valueOf(String.format("%.1f", average));
			}
			list.get(i).setLikescore(averageStr);
		}
		request.setAttribute("list", list);
		request.setAttribute("categoryid", categoryid);
		request.setAttribute("index", page.getPageNum());
		request.setAttribute("pages", page.getPages());
		request.setAttribute("total", page.getTotal());
		return "productlist";
	}
	
	
	//显示宫格菜品列表页
	@RequestMapping("list_Product")
	public String productL(int categoryid, HttpServletRequest request){
		
		//菜品类别
		List<Category> ctlist = categoryDAO.selectAll();
		request.setAttribute("ctlist", ctlist);
		
		
		//购物车
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
			List<Cart> cartlist = cartDAO.selectMyProductList(member.getId());
			String totalstr = "";
			double total = 0.0;
			for(int i=0;i<cartlist.size();i++){
				Member m = memberDAO.findById(cartlist.get(i).getMemberid());
				Product product = productDAO.findById(cartlist.get(i).getProductid());
				cartlist.get(i).setMember(m);
				cartlist.get(i).setProduct(product);
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPrice();
			}
			totalstr = String.format("%.2f", total);
			request.setAttribute("cartlist", cartlist);
			request.setAttribute("totalstr", totalstr);
		}
		//显示菜品列表
		String index = request.getParameter("index");
	   	int pageindex = 1;
	   	if(index!=null){
	   		 pageindex = Integer.parseInt(index);
	   	}
   	    Page<Object> page = PageHelper.startPage(pageindex,6);
		List<Product> list = productDAO.selectCategory(categoryid);
		for(int i=0;i<list.size();i++){
			List<Comment> commentlist = commentDAO.selectProduct(list.get(i).getId());
			Double score = 0.0;
			for(int a=0;a<commentlist.size();a++){
				score+= Double.parseDouble(String.valueOf(commentlist.get(a).getQuality()));
			}
			double averageStr=0.0;
			if(commentlist.size()!=0){
			double average = score/Double.parseDouble(String.valueOf(commentlist.size()));
			averageStr = Double.valueOf(String.format("%.1f", average));
			}
			list.get(i).setLikescore(averageStr);
		}
		request.setAttribute("list", list);
		request.setAttribute("categoryid", categoryid);
		request.setAttribute("index", page.getPageNum());
		request.setAttribute("pages", page.getPages());
		request.setAttribute("total", page.getTotal());
		return "product_list";
	}
	
	//菜品投票
	@RequestMapping("voteProdcut")
	public String voteProdcut(int id, HttpServletRequest request){
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
		Product product = productDAO.findById(id);
		productDAO.updateVote(product);
		return "redirect:productDetails.do?id="+id;
		}else{
			return "login";
		}
	}
	
	//更新上下架状态
	@RequestMapping("admin/updateStatus")
	public String updateStatus(int id, HttpServletRequest request){
		Product product = productDAO.findById(id);
		if(product.getIssj().equals("yes")){
			product.setIssj("no");
		}else{
			product.setIssj("yes");
		}
		product.setId(id);
		productDAO.updateStatus(product);
		return "redirect:productList.do";
	}
	
	//人气排行
	@RequestMapping("popularityProduct")
	public String popularityProduct(HttpServletRequest request){
		//菜品类别
		List<Category> ctlist = categoryDAO.selectAll();
		request.setAttribute("ctlist", ctlist);
		
		//购物车
		Member member = (Member)request.getSession().getAttribute("sessionmember");
		if(member!=null){
			List<Cart> cartlist = cartDAO.selectMyProductList(member.getId());
			String totalstr = "";
			double total = 0.0;
			for(int i=0;i<cartlist.size();i++){
				Member m = memberDAO.findById(cartlist.get(i).getMemberid());
				Product product = productDAO.findById(cartlist.get(i).getProductid());
				cartlist.get(i).setMember(m);
				cartlist.get(i).setProduct(product);
				total+=Double.parseDouble(String.valueOf(cartlist.get(i).getNum()))*product.getPrice();
			}
			totalstr = String.format("%.2f", total);
			request.setAttribute("cartlist", cartlist);
			request.setAttribute("totalstr", totalstr);
		}
		
		
		String index = request.getParameter("index");
	   	int pageindex = 1;
	   	if(index!=null){
	   		 pageindex = Integer.parseInt(index);
	   	}
   	    Page<Object> page = PageHelper.startPage(pageindex,6);
		List<Product> list = productDAO.selectVote();
		request.setAttribute("list", list);
		request.setAttribute("ctlist", ctlist);
		request.setAttribute("index", page.getPageNum());
		request.setAttribute("pages", page.getPages());
		request.setAttribute("total", page.getTotal());
		return "votelist";
	}
	
	
	

}
