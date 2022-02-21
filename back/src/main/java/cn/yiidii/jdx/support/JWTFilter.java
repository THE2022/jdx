package cn.yiidii.jdx.support;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpStatus;
import cn.hutool.jwt.JWTValidator;
import cn.yiidii.jdx.model.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JWTFilter
 *
 * @author ed w
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnClass(Filter.class)
public class JWTFilter extends OncePerRequestFilter {

    private static final List<String> IGNORED_URL = Arrays.asList("/jd/**", "/info", "/oauth/**");
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        boolean ignore = false;
        for (String pattern : IGNORED_URL) {
            if (PATH_MATCHER.match(pattern, requestURI)) {
                ignore = true;
                break;
            }
        }
        if (!ignore) {
            String token = request.getHeader("token");
            try {
                JWTValidator.of(token).validateDate();
            } catch (Throwable e) {
                R<?> fail = R.failed(1, "Token失效");
                response.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setContentType(ContentType.JSON.getValue());
                response.getWriter().write(objectMapper.writeValueAsString(fail));
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
