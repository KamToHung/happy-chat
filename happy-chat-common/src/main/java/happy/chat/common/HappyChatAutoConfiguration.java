package happy.chat.common;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({HappyChatProperties.class})
@ComponentScan(basePackages =  {"happy.chat.common"})
public class HappyChatAutoConfiguration {


}
