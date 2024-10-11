package com.systex.hw4.filter;

import com.systex.hw4.model.Member;
import com.systex.hw4.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

@Component
@WebFilter(urlPatterns = "/*")
public class LoginFilter extends OncePerRequestFilter {

    private final MemberService memberService;

    public LoginFilter(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");


        boolean isLoginRequest = uri.equalsIgnoreCase(contextPath + "/login");
        boolean isAjaxLoginRequest = uri.equalsIgnoreCase(contextPath + "/ajaxlogin");
        boolean isLogoutRequest = uri.equalsIgnoreCase(contextPath + "/logout");
        boolean isMemberRequest = uri.equalsIgnoreCase(contextPath + "/createmember");
        boolean isStaticResource = uri.contains("/css/") || uri.contains("/js/") || uri.contains("/images/");
        boolean isH2Console = uri.startsWith(contextPath + "/h2-console");

        // 處理登入請求
        if (isLoginRequest && request.getMethod().equalsIgnoreCase("POST")) {
            handleLogin(request, response);
            return;
        }

        // 處理AJAX登入請求
        if (isAjaxLoginRequest && request.getMethod().equalsIgnoreCase("POST")) {
            handleAjaxLogin(request, response);
            return;
        }

        // 處理登出請求
        if (isLogoutRequest) {
            HttpSession session = request.getSession(true);
            session.invalidate();
            response.sendRedirect(contextPath + "/login");
            return;
        }

        // 直接進入特定路由
        if (isLoginRequest || isAjaxLoginRequest || isMemberRequest || isH2Console || isStaticResource) {
            filterChain.doFilter(request, response);
            return;
        }

        // 檢查使用者是否有登入
        HttpSession session = request.getSession(false); // 不创建新会话
        Member member = (session != null) ? (Member) session.getAttribute("member") : null;
        if (member == null) {
            response.sendRedirect(contextPath + "/login");
            return;
        }

        // 繼續執行過濾
        filterChain.doFilter(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        HttpSession session = request.getSession(true);

        try {
            Optional<Member> obj = getMember(username, password);

            if (obj.isEmpty()) {
                throw new IllegalArgumentException("帳號密碼錯誤");
            }
            session.setAttribute("member", obj.get());

            // Redirect to main page
            response.sendRedirect(request.getContextPath() + "/lottery/main");
        } catch (Exception e) {
            LinkedList<String> errors = new LinkedList<>();
            errors.add(e.getMessage());
            session.setAttribute("errorMessages", errors);
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    private void handleAjaxLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");

        HttpSession session = request.getSession(true);
        JSONObject jsonResponse = new JSONObject();

        try {
            String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
            JSONObject json = new JSONObject(body);

            String username = json.getString("username");
            String password = json.getString("password");

            Optional<Member> obj = getMember(username, password);
            session.setAttribute("member", obj.get());

            // Success response
            jsonResponse.put("success", true);
        } catch (Exception e) {
            // Ensure errors array is returned even when there's a single error
            LinkedList<String> errors = new LinkedList<>();
            errors.add(e.getMessage());
            jsonResponse.put("success", false);
            jsonResponse.put("errors", errors);  // Add the errors array to JSON
        }
        response.getWriter().write(jsonResponse.toString());
    }

    private Optional<Member> getMember(String username, String password) {
        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);

        Optional<Member> obj = memberService.login(member);

        if (obj.isEmpty()) {
            throw new IllegalArgumentException("帳號密碼錯誤");
        }
        return obj;
    }
}
