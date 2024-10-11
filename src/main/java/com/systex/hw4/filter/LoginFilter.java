package com.systex.hw4.filter;

import com.systex.hw4.model.Member;
import com.systex.hw4.service.MemberService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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

        boolean isLoginRequest = uri.equals(contextPath + "/login");
        boolean isLogoutRequest = uri.equals(contextPath + "/logout") && request.getMethod().equalsIgnoreCase("GET");
        boolean isMemberRequest = uri.equals(contextPath + "/createMember");

        if (isLoginRequest) {
            switch (request.getMethod()) {
                case "GET":
                    filterChain.doFilter(request, response);
                    break;
                case "POST":
                    // 處理登入請求
                    handleLogin(request, response);
                    break;
            }
            return; // 處理完成後不需要繼續執行Filter鏈
        }

        if (isLogoutRequest) {
            HttpSession session = request.getSession(true);
            session.invalidate();
            response.sendRedirect(contextPath + "/login");
            return;
        }

        if (isMemberRequest) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            response.sendRedirect(contextPath + "/login");
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Member member = new Member();
        member.setUsername(username);
        member.setPassword(password);

        try {
            Optional<Member> obj = memberService.login(member);

            if (obj.isPresent()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("member", obj.get());

                // Redirect to main page
                response.sendRedirect(request.getContextPath() + "/lottery/main");
            } else {
                throw new IllegalArgumentException("帳號密碼錯誤");
            }
        } catch (Exception e) {
            LinkedList<String> errors = new LinkedList<>();
            HttpSession session = request.getSession();
            session.setAttribute("errorMessages", errors);

            // 重定向到 /login
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}
