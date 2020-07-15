package happy.chat.client.config;


import happy.chat.client.command.BaseCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class CommandInfo {

    @Autowired
    private ApplicationContext applicationContext;

    private static final Map<Integer, BaseCommand> COMMAND_MAP = new HashMap<>();

    @PostConstruct
    public void initCommand() {
        Map<String, BaseCommand> beans = applicationContext.getBeansOfType(BaseCommand.class);
        beans.values().forEach(baseCommand -> COMMAND_MAP.put(baseCommand.command(), baseCommand));
    }

    public BaseCommand getCommand(int key) {
        return COMMAND_MAP.get(key);
    }
}
