package gr.aueb.cf.schoolapp.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration             //create a bean from method
@RequiredArgsConstructor      //DI
@EnableMethodSecurity     // TO ENABLE @PreAuthorize annotation
@EnableWebSecurity        //filter security
public class SecurityConfig {

    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

//    @Autowired
//    public SecurityConfig(AuthenticationSuccessHandler authenticationSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler){
//        this.authenticationSuccessHandler = authenticationSuccessHandler;
//        this.authenticationFailureHandler = authenticationFailureHandler;
//    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "index.html"). permitAll()  // dinoyme dikaioma authorization
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/user/register", "/user/success").permitAll()
                        .requestMatchers("/teachers/insert").hasAuthority("INSERT_TEACHER")
                        .requestMatchers(HttpMethod.GET,"/teachers/edit/{uuid}").hasAuthority("EDIT_TEACHER")
                        .requestMatchers(HttpMethod.POST, "/teachers/edit").hasAuthority("EDIT_TEACHER")
                        .requestMatchers(HttpMethod.GET, "/teachers/update-success").hasAuthority("EDIT_TEACHER")
                        .requestMatchers(HttpMethod.POST, "/teachers/delete/{uuid}").hasAuthority("DELETE_TEACHER")
                        .requestMatchers(HttpMethod.GET, "/teachers/delete-success").hasAuthority("DELETE_TEACHER")
                        .requestMatchers("/teachers/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin ->formLogin
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler)
                       .failureHandler(authenticationFailureHandler)
                )
                .logout((logout -> logout
                        .logoutSuccessUrl("/login?logout"))    //δουλευει με Post logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
                return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);  // 12^12 ειναι τα rounds που θα κανει ο αλγοριθμος
    }
}
