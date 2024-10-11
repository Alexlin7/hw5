<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="/WEB-INF/layout/layout.jsp"/>
<% request.setAttribute("title", "出現錯誤"); %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<body>
<div class="container">
  <h2>出現錯誤</h2>

  <%-- 此處為錯誤報告 --%>
  <div class="alert alert-danger" role="alert">
    <p>抱歉，系統出現錯誤。</p>
    <p>錯誤詳情：<span>${error}</span></p>
  </div>

  <!-- 可以添加返回首頁或其他導航連結 -->
  <a href="${pageContext.request.contextPath}" class="btn btn-primary">返回首頁</a>
</body>
