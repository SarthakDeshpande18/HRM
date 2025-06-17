package SRMS.FinalProject.JwtFilter;

import SRMS.FinalProject.DTO.ApiResponse;
import SRMS.FinalProject.JwtUtil.JwtUtil;
import SRMS.FinalProject.Service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//    private final UserDetailsService userDetailsService;
//
//    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
//        this.jwtUtil = jwtUtil;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//
//        try {
//            String userEmail = jwtUtil.extractEmail(token);
//
//            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null
//                    && jwtUtil.validateToken(token, userEmail)) {
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(, null, null);
//
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//
//        } catch (ExpiredJwtException e) {
//            System.out.println("Token expired: " + e.getMessage());
//            setErrorResponse(response, 401, "Token expired");
//            return;
//        } catch (Exception e) {
//            System.out.println("Invalid Token: " + e.getMessage());
//            setErrorResponse(response, 401, "Token invalid");
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private void setErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
//        response.setStatus(status);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        ApiResponse<Object> apiResponse = new ApiResponse<>(status, message, null);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(apiResponse);
//
//        response.getWriter().write(json);
//    }
//}


    @Component
    public class JwtFilter extends OncePerRequestFilter {

        private final JwtUtil jwtUtil;
        private final CustomUserDetailsService customUserDetailsService; // assuming your UserDetailsService loads by userId

        public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
            this.jwtUtil = jwtUtil;
            this.customUserDetailsService = customUserDetailsService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {

            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);

            try {
                Long userId = jwtUtil.extractUserId(token); //  extract userId from token

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null
                        && jwtUtil.validateToken(token, userId)) { //  optionally validate based on ID

                    UserDetails userDetails = customUserDetailsService.loadUserById(userId); //  load by ID

                    System.out.println("Authenticated User ID: " + userId);
                    System.out.println("Authorities: " + userDetails.getAuthorities());

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (ExpiredJwtException e) {
                System.out.println("Token expired: " + e.getMessage());
                setErrorResponse(response, 401, "Token expired");
                return;
            } catch (Exception e) {
                System.out.println("Invalid Token: " + e.getMessage());
                setErrorResponse(response, 401, "Invalid token");
                return;
            }

            filterChain.doFilter(request, response);
        }

        private void setErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ApiResponse<Object> apiResponse = new ApiResponse<>(status, message, null);

            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(apiResponse);

            response.getWriter().write(json);
        }
    }
