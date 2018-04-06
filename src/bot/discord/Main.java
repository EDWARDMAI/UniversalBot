package bot.discord;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;


public class Main extends ListenerAdapter{
    private String prefix = ".";
    private int prefixLen = 1;
    public static JDA jda;
    public static void main(String[] args){ 
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken("NDAwNDc3OTk3OTcyMzI0MzYy.DTkz7w.lB8cZBhmCzi05E9NxmeyEgUOqtM");
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.ONLINE);
        try{
            jda = builder.buildBlocking();
        }
        catch(LoginException e){
            e.printStackTrace();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        jda.addEventListener(new Main());
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        Message objMsg = e.getMessage();

        MessageChannel objChannel = e.getChannel();
        User objUser = e.getAuthor();
        if(objMsg.getContentDisplay().equals("hello")){
            objChannel.sendMessage("Hello, " + objUser.getAsMention() + "!").queue();
        }
    }
}
