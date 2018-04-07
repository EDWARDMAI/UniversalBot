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
        //Data from message.
        User objUser = e.getAuthor();
        Message objMsg = e.getMessage();
        MessageChannel objChannel = e.getChannel();

        //Retrieved a command message
        String message = objMsg.getContentRaw();
        if(message.startsWith(prefix)){
            commandHelp(message.substring(1), objUser, objChannel);
        }
    }

    //Checks first word of the command string, does whatever the command is.
    private void commandHelp(String command, User objUser, MessageChannel objChannel){
        command = command.trim();
        String[] commands = command.split(" ");
        if(commands[0].equals("ping")){
            objChannel.sendMessage(objUser.getAsMention() + " pong.").queue();
        }
        else{
            objChannel.sendMessage(objUser.getAsMention() + " command not recognized.").queue();
        }
    }
}
