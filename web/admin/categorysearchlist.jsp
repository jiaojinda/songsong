<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>类别信息</title>
	<meta name="renderer" content="webkit">	
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">	
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">	
	<meta name="apple-mobile-web-app-status-bar-style" content="black">	
	<meta name="apple-mobile-web-app-capable" content="yes">	
	<meta name="format-detection" content="telephone=no">	
	<link rel="stylesheet" type="text/css" href="common/layui/css/layui.css" media="all">
	<link rel="stylesheet" type="text/css" href="common/bootstrap/css/bootstrap.css" media="all">
	<link rel="stylesheet" type="text/css" href="common/global.css" media="all">
	<link rel="stylesheet" type="text/css" href="css/personal.css" media="all">
	<script type="text/javascript" src="/songsong/layer/jquery-2.0.3.min.js"></script>
</head>
<body>
<section class="layui-larry-box">
	<div class="larry-personal">
	    <div class="layui-tab">
            <blockquote class="layui-elem-quote news_search">
		
		<div class="layui-inline">
			<div class="layui-input-inline">
		      <form action="<%=path %>/admin/searchCategory.do" method="post" id="searchform" name="searchform">
		    	<input value="${key}" placeholder="请输入类别名称" name="key" class="layui-input search_input" type="text">
		    <!-- <input type="submit" class="layui-btn" value="查询"> -->	
		      </form>
		    </div>
		    <a class="layui-btn"  href="javascript:void(0)" onclick="searchnew()" >查询</a>
		</div>
	   <div class="layui-inline">
		<a class="layui-btn layui-btn-normal" href="categoryadd.jsp">添加类别</a>
		</div>
		<div class="layui-inline">
			<a class="layui-btn layui-btn-danger" href="javascript:void(0)" onclick="deleteAll()">批量删除</a>
		</div>
	</blockquote>
            
		         <!-- 操作日志 -->
                <div class="layui-form news_list" >
                    <table class="layui-table" style="font-size:12px;">
					    <colgroup>
					    <col width="35%">
					    <col width="35%">
						<col width="">
					    </colgroup>
					<thead>
						<tr>
						    <th>
						    <input name="" lay-skin="primary" lay-filter="allChoose" id="allChoose" type="checkbox">
						    <div class="layui-unselect layui-form-checkbox" lay-skin="primary"><i class="layui-icon"></i></div>
						    </th>
						    <th>类别名称</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody class="news_content" id="userTbody">
					   <c:forEach items="${list}" var="category" varStatus="status">
						<tr >
						    <td>
						    <input name="checked" lay-skin="primary" lay-filter="choose" type="checkbox" value="${category.id}">
						    <div class="layui-unselect layui-form-checkbox" lay-skin="primary"><i class="layui-icon"></i></div>
						    </td>
							<td>${category.name}</td>
							<td>
							    <a class="layui-btn layui-btn-mini" href="showCategory.do?id=${category.id}"><i class="iconfont icon-edit"></i> 编辑</a>
							</td>
						</tr>
						</c:forEach>
						<tr>
                    <td align="center" style="font-weight: bold;font-family:楷体;font-weight: bold; color:blue" colspan="10">
                                                                 共${total}条记录&nbsp;&nbsp;&nbsp;&nbsp;
                        <a href="searchCategory.do?index=1&key=${key}" style="font-family:楷体;">首页</a>&nbsp;&nbsp;
                        
                        <c:choose>
                        <c:when test="${index >1}">
                        <a href="searchCategory.do?index=${index-1}&key=${key}" style="font-family:楷体;">上一页</a>
                        </c:when>
                        <c:otherwise>
                        <a href="javascrip:void(0)" style="font-family:楷体;">上一页</a>
                        </c:otherwise>
                        </c:choose>
                        &nbsp;&nbsp;
                        <c:choose>
                        <c:when test="${pages>index}">
                        <a href="searchCategory.do?index=${index+1}&key=${key}" style="font-family:楷体;">下一页</a>
                        </c:when>
                        <c:otherwise>
                        <a href="javascrip:void(0)" style="font-family:楷体;">下一页</a>
                        </c:otherwise>
                        </c:choose>
                        &nbsp;&nbsp;
                        <a href="searchCategory.do?index=${pages}&key=${key}" style="font-family:楷体;">末页</a>
                    </td>
                  </tr>
					</tbody>
					</table>
                     <div class="larry-table-page clearfix">
			         </div>
			    </div>
			     <!-- 登录日志 -->
		    </div>
		</div>
	
</section>
<script type="text/javascript" src="common/layui/layui.js"></script>
<script type="text/javascript" src="js/newslist.js"></script>
<script type="text/javascript">
	layui.use(['jquery','layer','element','laypage'],function(){
	      window.jQuery = window.$ = layui.jquery;
	      window.layer = layui.layer;
          var element = layui.element(),
              laypage = layui.laypage;

            
          laypage({
					cont: 'page',
					pages: 10 //总页数
						,
					groups: 5 //连续显示分页数
						,
					jump: function(obj, first) {
						//得到了当前页，用于向服务端请求对应数据
						var curr = obj.curr;
						if(!first) {
							//layer.msg('第 '+ obj.curr +' 页');
						}
					}
				});

          laypage({
					cont: 'page2',
					pages: 10 //总页数
						,
					groups: 5 //连续显示分页数
						,
					jump: function(obj, first) {
						//得到了当前页，用于向服务端请求对应数据
						var curr = obj.curr;
						if(!first) {
							//layer.msg('第 '+ obj.curr +' 页');
						}
					}
				});
    });

    function searchnew(){
      //${"#searchform"}.submit();
      searchform.submit()
      
    }

    function deleteAll(){

    	layer.confirm('是否确认删除？', {
      	  btn: ['是','否'] //按钮
      	}, function(){

      	  var len = $("input[name='checked']:checked").length;	
      	  if(len!=0){
      	  var checkedbox = $("input[name='checked']:checked");
      	  var arr = new Array();
            $(checkedbox).each(function(i){
                arr[i] = $(this).val();
            });
            var vals = arr.join(",");
            $.ajax({
      		type:"post",
      		url:"categoryDelAll.do?vals="+vals,
      		date:"",
      		success:function(msg){
      			location.replace("categoryList.do");
      		  }
            })
      	  }else{
				layer.msg("请选择要删除的项");
          	  }
            
            
      	}, function(){
      	});
	  //var $checkbox = $("input[name='checked']");
	  //var len = $("input[name='checked']:checked").length;
      
    }


</script>
</body>
</html>