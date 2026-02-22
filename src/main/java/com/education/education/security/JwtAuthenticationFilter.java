package com.education.education.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider){
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        String token = resolveToken(request);

        if (StringUtils.hasText(token)){
            try {
                Claims claims = jwtProvider.parseClaims(token);
                Long userId = claims.get("uid", Long.class);
                String email = claims.getSubject();

                UserPrincipal principal = new UserPrincipal(userId,email);

                var auth = new UsernamePasswordAuthenticationToken(
                        principal,
                        null,
                        principal.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e){
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request,response);
    }

    private String resolveToken(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        if (!StringUtils.hasText(bearer)) return null;
        if (!bearer.startsWith("Bearer ")) return null;
        return bearer.substring(7);
    }
}
