<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="/WEB-INF/layout/layout.jsp"/>
<% request.setAttribute("title", "AJAX登入"); %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<body>
<div class="container">
    <h2>使用AJAX登入會員</h2>

    <%-- Error messages will be displayed here --%>
    <div id="error-container"></div>

    <form:form id="loginForm">
        <div class="mb-3">
            <form:label path="username" cssClass="form-label">使用者名稱</form:label>
            <form:input path="username" cssClass="form-control" required="required" />
            <div id="groupHelp" class="form-text">輸入您的帳號</div>
        </div>
        <div class="mb-3">
            <form:label path="password" cssClass="form-label">密碼</form:label>
            <form:password path="password" cssClass="form-control" required="required" />
            <div id="groupHelp" class="form-text">輸入您的密碼</div>
        </div>
        <input type="reset" class="btn btn-secondary" value="重置" />
        <input type="submit" value="登入" class="btn btn-primary" />
    </form:form>

    <a href="createMember">註冊帳號</a>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function () {
        $('#loginForm').on('submit', function (event) {
            event.preventDefault();

            const formData = {
                username: $('input[name="username"]').val(),
                password: $('input[name="password"]').val()
            };

            $.ajax({
                type: 'POST',
                url: '${pageContext.request.contextPath}/ajaxlogin',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function (response) {
                    if (response.success) {
                        window.location.href = '${pageContext.request.contextPath}/lottery/main';
                    } else {
                        displayErrors(response.errors);
                    }
                },
                error: function () {
                    displayErrors(['無法連線伺服器，請稍後再試。']);
                }
            });
        });

        function displayErrors(errors) {
            $('#error-container').empty();

            // Check if errors is an array and iterate only if valid
            if (Array.isArray(errors)) {
                errors.forEach(function (error) {
                    $('#error-container').append('<div class="alert alert-danger" role="alert">' + error + '</div>');
                });
            } else {
                $('#error-container').append('<div class="alert alert-danger" role="alert">未知的錯誤發生</div>');
            }
        }
    });
</script>
</body>
