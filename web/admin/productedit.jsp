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
	<title>编辑菜品</title>
	<meta name="renderer" content="webkit">	
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">	
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">	
	<meta name="apple-mobile-web-app-status-bar-style" content="black">	
	<meta name="apple-mobile-web-app-capable" content="yes">	
	<meta name="format-detection" content="telephone=no">	
	<link rel="stylesheet" type="text/css" href="<%=path %>/admin/common/layui/css/layui.css" media="all">
	<link rel="stylesheet" type="text/css" href="<%=path %>/admin/common/bootstrap/css/bootstrap.css" media="all">
	<link rel="stylesheet" type="text/css" href="<%=path %>/admin/common/global.css" media="all">
	<link rel="stylesheet" type="text/css" href="<%=path %>/admin/css/personal.css" media="all">
	<script type="text/javascript" src="<%=path %>/layer/jquery-2.0.3.min.js"></script>
	<script type="text/javascript" src="<%=path %>/layer/layer.js"></script>
	<script type="text/javascript" src="<%=path %>/admin/js/popup.js"></script>
</head>
<body>
<section class="layui-larry-box">
	<div class="larry-personal">
		<header class="larry-personal-tit">
			<span>编辑菜品</span>
		</header><!-- /header -->
		<div class="larry-personal-body clearfix changepwd">
			<form class="layui-form col-lg-4" id="supplierform" method="post" action="<%=path %>/admin/productEdit.do" onsubmit="return supplieradd()">
			<input type="hidden" name="id" value="${product.id}"/>
				<div class="layui-form-item">
					<label class="layui-form-label">菜品名称</label>
					<div class="layui-input-block">  
					  	<input type="text" name="productname" id="productname" value="${product.productname}" required oninvalid="setCustomValidity('菜品名称不能为空')" oninput="setCustomValidity('');"  style="width: 300px;"  autocomplete="off"  class="layui-input" placeholder="菜品名称" >
					</div>
				</div>
			    
			 	<div class="layui-form-item">
					<label class="layui-form-label">图片</label>
					<div class="layui-input-block">  
					  	<input name='filename' type='text' class="layui-input" value="${product.filename}"  style="width: 300px;" id='url'  size='50' value='' />&nbsp;<input type='button' value='上传'  class="layui-input" onClick="up('url')" style="width: 60px;height: 37px;"/>
					</div>
				</div>
				
				<div class="layui-form-item">
					<label class="layui-form-label">菜品类别</label>
					<div class="layui-input-block" style="width: 300px;">  
					  	<select name="categoryid" id="categoryid" style="width: 300px;"   autocomplete="off"  class="layui-input">
					  	<c:forEach items="${list}" var="category">
					  	  <option value="${category.id}" <c:if test="${category.id eq product.categoryid}">selected</c:if> >${category.name}</option>
					  	</c:forEach>
					  	</select>
					</div>
				</div>
				
				<div class="layui-form-item">
					<label class="layui-form-label">菜品单价</label>
					<div class="layui-input-block">  
					  	<input type="text" name="price" id="price" value="${product.price}"  required oninvalid="setCustomValidity('菜品单价不能为空或价格输入有误')" oninput="setCustomValidity('');"  pattern="(0|[1-9][0-9]{0,9})(\.[0-9]{1,2})?" style="width: 300px;"  autocomplete="off"  class="layui-input" placeholder="菜品单价" >
					</div>
				</div>
				
				<div class="layui-form-item">
					<label class="layui-form-label">产地</label>
					<div class="layui-input-block">  
					  	<input type="text" name="author" id="author" value="${product.author}" required oninvalid="setCustomValidity('产地不能为空')" oninput="setCustomValidity('');"  style="width: 300px;"  autocomplete="off"  class="layui-input" placeholder="产地" >
					</div>
				</div>
				
				<div class="layui-form-item">
					<label class="layui-form-label">品级</label>
					<div class="layui-input-block">  
					  	<input type="text" name="press" id="press" value="${product.press}" required oninvalid="setCustomValidity('品级不能为空')" oninput="setCustomValidity('');"  style="width: 300px;"  autocomplete="off"  class="layui-input" placeholder="品级" >
					</div>
				</div>
				
				<div class="layui-form-item">
					<label class="layui-form-label">菜品介绍</label>
					<div class="layui-input-block">  
					  	<textarea name="content" placeholder="请输入菜品介绍" required class="layui-textarea" oninvalid="setCustomValidity('菜品介绍不能为空')" oninput="setCustomValidity('');" >${product.content}</textarea>
					</div>
				</div>
				
				
				
				<div class="layui-form-item change-submit">
					<div class="layui-input-block">
					    
						<button class="layui-btn" lay-submit="" id="supplierbutton" lay-filter="demo1" >立即提交</button>
						<button type="reset" class="layui-btn layui-btn-primary">重置</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</section>
<script type="text/javascript" src="<%=path %>/admin/common/layui/layui.js"></script>
<script type="text/javascript">
	layui.use(['form','upload'],function(){
         var form = layui.form();
	});

	function up(tt)
	{
			    layer.open({
			      type: 2,
			      title: '上传文件',
			      shadeClose: true,
			      shade: false,
			      maxmin: true, //开启最大化最小化按钮
			      area: ['450px', '200px'],
			      content: '/songsong/upload.jsp?Result='+tt
			    });
	}	
</script>
</body>
</html>