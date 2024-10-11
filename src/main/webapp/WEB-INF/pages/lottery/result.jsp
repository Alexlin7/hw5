<%@ page import="java.util.ArrayList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<% request.setAttribute("title", "樂透選號結果"); %>
<jsp:include page="/WEB-INF/layout/layout.jsp"/>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<body>
<div class="container">
	<h1>生成結果</h1>
	<p class="lead">排除數字 ${ excludeNumber }</p>
	<table class="table table-hover">
		<thead>
		<th>組數</th>
		<th>號碼</th>
		</thead>
		<tbody>
		<c:forEach var="lottery" items="${lotterys}" varStatus="status">
			<tr>
				<th scope="row">第${status.index + 1}組</th>
				<td>
					${lottery}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<a href="<%=request.getContextPath()%>/lottery/main">
		<button class="btn btn btn-secondary">再生成一次</button>
	</a>

	<a href="<%=request.getContextPath()%>">
		<button class="btn btn-primary">回首頁</button>
	</a>

</div>

</body>
