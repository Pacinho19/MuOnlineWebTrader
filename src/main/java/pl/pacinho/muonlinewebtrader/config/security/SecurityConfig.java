package pl.pacinho.muonlinewebtrader.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import pl.pacinho.muonlinewebtrader.frontend.config.UIConfig;
import pl.pacinho.muonlinewebtrader.service.AccountService;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AccountService accountService;
    private final CryptoConfig cryptoConfig;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountService);
        authProvider.setPasswordEncoder(cryptoConfig.encoder());
        return authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().disable()
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers(UIConfig.HOME_URL).permitAll()
                .antMatchers(UIConfig.HOME_URL + "/").permitAll()
                .antMatchers(UIConfig.DECODE_ITEM_URL).permitAll()
                .antMatchers(UIConfig.ITEM_LIST_URL).permitAll()
                .antMatchers(UIConfig.SHOP_URL + "/**").permitAll()
                .antMatchers(UIConfig.BUY_ITEM_URL + "/**").authenticated()
                .antMatchers(UIConfig.HOME_URL + "/**").authenticated()
                .and()
                .formLogin()
                .loginPage("/login");
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/resources/**", "/static/**");
    }
}