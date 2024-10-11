<%@ page import="java.util.LinkedList" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<% request.setAttribute("title", "樂透選號"); %>

<jsp:include page="/WEB-INF/layout/layout.jsp"/>

<body>
<div class="container">
    <h1>樂透選號</h1>
    <%-- 此處為錯誤報告 --%>

    <c:if test="${not empty errorMessages}">
        <c:forEach var="error" items="${errorMessages}">
            <div class="alert alert-danger" role="alert">
                    ${error}
            </div>
        </c:forEach>
    </c:if>


    <form action="lotteryController.do" method="POST">
        <div class="mb-3">
            <label id="grouplabel" name="lable" class="form-label">組數</label>
            <input type="number" id="group" name="group" class="form-control" value="${ param.group }"/>
            <div id="groupHelp" class="form-text">輸入您要的組數</div>
        </div>
        <div class="mb-3">
            <lable id="excludelabel" name="lable" class="form-label">排除的數字</lable>
            <input type="text" id="exclude" name="exclude" class="form-control" value="${ param.exclude}"/>
            <div id="exculdeNumberHelp" class="form-text">輸入您要排除的數字，並使用空格分隔</div>
        </div>
        <input type="reset" class="btn btn-secondary" value="重置"/>
        <input type="submit" class="btn btn-primary" value="生成樂透號碼"/>
    </form>


</div>

</body>
