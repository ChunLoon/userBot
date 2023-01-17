package my.uum;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * This class is for telegram bot function
 */
public class bot extends TelegramLongPollingBot {

    /**
     * This method is  return bot username
     * @return bot username
     */
    @Override
    public String getBotUsername() {
        // TODO
        return "s278884_A221_bot";
    }

    /**
     * This method is for return bot token
     * @return bot token
     */
    @Override
    public String getBotToken() {
        // TODO
        return "5717300937:AAGM0VpChvz7680oSc17BiyUacLhYP0dgBk";
    }


    int stepBook = 0;
    int stepCancel = 0;

    int stepEdit = 0;
    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String reply = String.valueOf(message.getText());
        String text = message.getText();
        String chatId = message.getChatId().toString();

        if (text.equals("/start") || text.equals("/booking") || text.equals("/cancel") || text.equals("/list") || text.equals("/edit") || text.equals("0")) { //command
            SendMessage sendMessage = new SendMessage();
            switch (text) {
                case "/start": { //choose command
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Hello, Welcome to STIW3054_Fivesome_bot\n\n" +
                            "Click /booking if you want book a meeting room\n" +
                            "Click /cancel if you want to cancel a meeting room\n" +
                            "Click /edit if you want to edit your profile info and booking info\n" +
                            "Click /list if you want to display the list of users");
                    break;
                }
                case "/cancel": {
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("May I know your email?\n\nReply 0: Back to Main Menu");
                    stepCancel = 1;
                    break;
                }
                case "/list": {
                    sendMessage.setChatId(chatId);
                    sendMessage.setText(SQLite.list());
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Click /booking if you want book a meeting room\n" +
                            "Click /cancel if you want to cancel a meeting room\n" +
                            "Click /edit if you want to edit your profile info and booking info\n" +
                            "Click /list if you want to display the list of users");
                    break;
                }
                case "/booking": {
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Please provide a preferred booking date for meeting room \n(eg: 25.12.2022)(DD-MM-YYYY)\n\n" +
                            "Reply 0: Back to Main Menu");
                     stepBook = 1;
                    break;
                }
                case "/edit": {
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("May I know your email?\n\nReply 0: Back to Main Menu");
                    stepEdit = 1;
                    break;
                }
                case "0": {
                    stepBook = 0;
                    stepCancel = 0;
                    stepEdit = 0;
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Hello, Welcome to STIW3054_Fivesome_bot\n\n" +
                            "Click /booking if you want book a meeting room\n" +
                            "Click /cancel if you want to cancel a meeting room\n" +
                            "Click /edit if you want to edit your profile info and booking info\n" +
                            "Click /list if you want to display the list of users");
                    break;
                }

            }
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }  //end choose command

        else if (message.hasText()) {
            SendMessage sendMessage = new SendMessage();
            switch (stepBook) {


                case 1: {
                    sendMessage.setChatId(chatId);


                   if(reply.length()==10 & reply.matches(".*[.]+.*" )){
                    Booking_info.setBooking_Date( reply);

                    stepBook = 2;
                    sendMessage.setText("Please enter start  time  of booking meeting room.\n(In 24hour format eg:11:20, 09:20, 14:00)\n\nReply 0: Back to Main Menu");}
                  else{
                    sendMessage.setText("Invalid date format. Please insert again your Booking Date.\n\nReply 0: Back to Main Menu");}

                    break;
                }
                case 2: {

                    if(reply.length()==5 & reply.matches(".*[:]+.*" )) {
                        Booking_info.setBooking_StartTime(reply);

                        covertStartTime(reply);

                        stepBook = 3;
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Please enter end  time  of booking meeting room.\n(In 24hour format eg:11:20, 09:20, 14:00)\n\nReply 0: Back to Main Menu");
                    }else{

                        stepBook = 2;
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Invalid time format. Please enter start  time  of booking meeting room again.\n\nReply 0: Back to Main Menu");
                    }
                    break;
                }
                case 3: {


                    if(reply.length()==5 & reply.matches(".*[:]+.*" )) {
                        covertEndTime(reply);

                     if(Time.getStartTime()>Time.getEndTime())   {
                         sendMessage.setChatId(chatId);
                         sendMessage.setText("End time must be greater than end time.Please enter end time  of booking meeting room again.\n\nReply 0: Back to Main Menu");

                     }else {
                         Booking_info.setBooking_EndTime(reply);

                         stepBook = 4;
                         sendMessage.setChatId(chatId);

                         sendMessage.setText("May I know the purpose of you booking?\n\n" +
                                 "Reply 1: Group Discussion\n" +
                                 "Reply 2: Hold Meeting\n" +
                                 "Reply 3: Conduct lecture\n" +
                                 "Reply 4: Take a quiz\n" +
                                 "Reply 5: Use equipment\n\n" +
                                 "Reply 0: Back to Main Menu");
                     }
                    }else{

                        stepBook = 3;
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Invalid time format. Please enter end time  of booking meeting room again.\n");
                    }


                    break;
                }
                case 4: {


                    sendMessage.setChatId(chatId);

                    if (reply.equals("1") || reply.equals("2") || reply.equals("3") || reply.equals("4") || reply.equals("5")) {
                        switch (reply) {
                            case "1":
                                //   purpose = "Group Discussion";
                                Booking_info.setPurpose( "Group Discussion");
                                break;
                            case "2":
                                // purpose = "Hold Meeting";
                                Booking_info.setPurpose( "Hold Meeting");
                                break;
                            case "3":
                                // purpose = "Conduct lecture";
                                Booking_info.setPurpose( "Conduct lecture");
                                break;
                            case "4":
                                //  purpose = "Take a quiz";
                                Booking_info.setPurpose(  "Take a quiz");
                                break;
                            case "5":
                                // purpose = "Use equipment";
                                Booking_info.setPurpose( "Use equipment");
                                break;
                        }

                        stepBook = 24;

                        String rr=  SQLite.displaySchool();
                        sendMessage.setText("Before booking a room, you need choose the building location.\nThis is a list for school:\n"+rr+"\nPlease Insert School Id you want (eg:S11)\n\n\nReply 0: Back to Main Menu");


                    } else {
                        stepBook = 4;
                        sendMessage.setText("Please enter 1-5 only\n\nReply 0: Back to Main Menu");
                    }
                    break;



                }
                case 5: {
                    Booking_info.setRoomId(reply);

                    String rr=SQLite.selectRoom(reply);
                    if(rr.equals("Invalid room ID")){
                        sendMessage.setChatId(chatId);

                        sendMessage.setText("Invalid room ID, please enter again. \n\nReply 0: Back to Main Menu");

                    }else {
                        stepBook = 6;

                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Kindly provide your email address?\n\nReply 0: Back to Main Menu");
                    }

                    break;
                }
                case 6: {
                    sendMessage.setChatId(chatId);

                    if(reply.matches(".*[@]+.*" )){
                        User_list.setEmail(reply);
                        stepBook = 7;
                        sendMessage.setText("May I have your full name please?\n\nReply 0: Back to Main Menu");

                        sendMessage.setChatId(chatId);
                    }
else{
                        stepBook = 6;
                        sendMessage.setText("Email need to contains @. Please enter again your email.\n\nReply 0: Back to Main Menu");

                    }



                    break;
                }
                case 7: {
                    sendMessage.setChatId(chatId);
                    User_list.setName(reply);



                    stepBook = 8;
                    sendMessage.setText("First at all, may I know your ic number?\n(eg:111111-08-1234)\n\nReply 0: Back to Main Menu");

                    break;
                }
                case 8: {

                 sendMessage.setChatId(chatId);
                    if (reply.matches(".*[a-zA-Z]+.*")) { //if contain a-z *
                        sendMessage.setText("Ic number should not contain alphabet, please enter again\n\nReply 0: Back to Main Menu");
                    } else {
                        if (reply.length() == 14) {
                           // ic = reply;
                            User_list.setICNO(reply);
                            stepBook = 9;
                           sendMessage.setText("How about your telephone number?\n\nReply 0: Back to Main Menu");
                        } else {
                            sendMessage.setText("Failed, please enter 14 digit number including '-'\n\nReply 0: Back to Main Menu");
                        }
                    }





                    break;
                }
                case 9: {

                    sendMessage.setChatId(chatId);

                    if (reply.length() < 10 || reply.length() > 11) {
                        sendMessage.setText("Please enter 10 or 11 digit phone number\n\nReply 0: Back to Main Menu");
                    }  else {
                        User_list.setMobile_TelNo(reply);
                        stepBook = 10;
                        sendMessage.setText("Kindly provide your password, this will be needed when cancel or edit booking. (Need to more than 4 value and contain alphabet and number)\n(eg:1111A)\n\nReply 0: Back to Main Menu");

                    }


                    break;
                }
                case 10: {
                    sendMessage.setChatId(chatId);

                    if(reply.length()>4 && reply.matches(".*[a-zA-Z]+.*")&&  reply.matches(".*[1-9]+.*")){
                    User_list.setPassword(reply);
                           stepBook=14;
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Pls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");}
                    else{
                        stepBook=10;
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Pls enter again your password (Need to more than 4 value and contain alphabet and number)\n\nReply 0: Back to Main Menu");
                    }

break;

                }
                case 11:{
                    sendMessage.setChatId(chatId);

                    if (reply.equals("1")) {
                        SQLite.insertBooking(Booking_info.getPurpose(),Booking_info.getBooking_Date(),Booking_info.getBooking_StartTime(),Booking_info.getBooking_EndTime(),Booking_info.getRoomId());
                       SQLite.insert_User_Info(User_list.getEmail(),User_list.getICNO(),User_list.getName(),User_list.getMobile_TelNo(),User_list.getPassword());
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Booking successfully");
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                           stepBook = 0;

                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Click /booking if you want book a meeting room\n" +
                                "Click /cancel if you want to cancel a meeting room\n" +
                                "Click /list if you want to display the list of users");

                    } else if (reply.equals("2")) {

                         stepBook = 12;
                        String roomInfo=SQLite.roomInfo(Booking_info.getRoomId());

                        sendMessage.setChatId(chatId);
                           sendMessage.setText("What number you want edit? Please enter the number \n" +
                                   "\nReply 1: Booking Info&Room Info " +   "\nRoom Id: " + Booking_info.getRoomId()+roomInfo+  "\nPurpose: " + Booking_info.getPurpose() + "\nDate: " + Booking_info.getBooking_Date()
                                   + "\nStart Time: " + Booking_info.getBooking_StartTime() +"\nEnd Time: "+ Booking_info.getBooking_EndTime() +
                                   "\n\nUser Info: " + "\nReply 2: IC no: " + User_list.getICNO()+ "\nReply 3: Name: " + User_list.getName() +
                                   "\nReply 4: Tel no: " + User_list.getMobile_TelNo() + "\nReply 5: Email: " + User_list.getEmail() + "\nReply 6: Password: " + User_list.getPassword()  +
                                   "\n\nReply 0: Back to Main Menu");

                    }
                    break;
                }
                case 12:{
                    sendMessage.setChatId(chatId);
                    if (reply.equals("1")) {

                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Please provide a preferred booking date for meeting room? \n(eg: 25.12.2022)\n\n" +
                                "Reply 0: Back to Main Menu");
                        stepBook = 19;
                        break;

                    }else if ( reply.equals("2") || reply.equals("3") || reply.equals("4") || reply.equals("5")|| reply.equals("6")) {
                        switch (reply) {

                            case "2":
                                stepBook = 13;
                                sendMessage.setText("Kindly provide your ICNo you want update.\n\nReply 0: Back to Main Menu");
                                break;
                            case "3":
                                stepBook = 15;
                                sendMessage.setText("Kindly provide your name you want update.\n\nReply 0: Back to Main Menu");
                                break;
                            case "4":
                                stepBook = 16;
                                sendMessage.setText("Kindly provide your TelNo you want update.\n\nReply 0: Back to Main Menu");
                                break;
                            case "5":
                                stepBook = 17;
                                sendMessage.setText("Kindly provide your Email you want update.\n\nReply 0: Back to Main Menu");
                                break;
                            case "6":
                                stepBook = 18;
                                sendMessage.setText("Kindly provide your Password you want update.\n\nReply 0: Back to Main Menu");
                                break;
                        }


                    }
                   break;

                }
                case 13:{
                    sendMessage.setChatId(chatId);

                    if (reply.matches(".*[a-zA-Z]+.*")) { //if contain a-z *
                        sendMessage.setText("Ic number should not contain alphabet, please enter again\n\nReply 0: Back to Main Menu");
                    } else {
                        if (reply.length() == 14) {
                            User_list.setICNO(reply);
                        sendMessage.setText("Update Successfully" +"\nReply 0: Back to Main Menu");
                         stepBook = 14;
                        sendMessage.setText("Pls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");


                        } else {
                            sendMessage.setText("Failed, please enter 14 digit number including '-'\n\nReply 0: Back to Main Menu");
                        }
                    }

 break;

                }

                case 14:{
                    sendMessage.setChatId(chatId);
                    if (reply.equals("1")) {

                  String roomInfo=SQLite.roomInfo(Booking_info.getRoomId());

                        sendMessage.setText("This is your booking detail\n" +
                                "\nRoom Info: " + "\nRoom Id: " + Booking_info.getRoomId() +roomInfo+  "\n\nBooking Info: "+"\nPurpose: " + Booking_info.getPurpose() + "\nDate: " + Booking_info.getBooking_Date()
                                + "\nStart Time: " + Booking_info.getBooking_StartTime() + "\nEnd Time: " + Booking_info.getBooking_EndTime() +
                                "\n\nUser Info: " + "\nIC no: " + User_list.getICNO() + "\nName: " + User_list.getName() +
                                "\nTel no: " + User_list.getMobile_TelNo() + "\nEmail: " + User_list.getEmail() + "\nPassword: " + User_list.getPassword() +
                                "\n\nAre these correct?\n" +
                                "Reply 1: Yes\nReply 2: No, I would like to make a correction \n\nReply 0: Back to Main Menu");
                        stepBook = 11;
                        break;
                    }
                    else{


                    }

                }

                case 15:{

                    sendMessage.setChatId(chatId);
                    User_list.setName(reply);
                    sendMessage.setText("Update Successfully" +"\nReply 0: Back to Main Menu");
                    stepBook = 14;
                    sendMessage.setText("Pls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");
                    break;
                }
                case  16:{

                    sendMessage.setChatId(chatId);

                    if (reply.length() < 10 || reply.length() > 11) {
                       // stepBook = 16;

                        sendMessage.setText("Please enter 10 or 11 digit phone number\n\nReply 0: Back to Main Menu");
                    }  else {
                        User_list.setMobile_TelNo(reply);
                        stepBook = 14;
                        sendMessage.setText("Pls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");

                    }

                    break;
                }
                case 17:{
                    sendMessage.setChatId(chatId);

                    if(reply.matches(".*[@]+.*" )){
                        User_list.setEmail(reply);
                        stepBook = 14;
                        sendMessage.setText("Update Successfully" +"\nReply 0: Back to Main Menu");

                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Pls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");
                    }
                    else{
                      //  stepBook = 17;
                        sendMessage.setText("Email need to contains @. Please enter again your email.\n\nReply 0: Back to Main Menu");

                    }



                    break;
                }
                case 18:{
                    sendMessage.setChatId(chatId);

                    if(reply.length()>4 && reply.matches(".*[a-zA-Z]+.*")&&  reply.matches(".*[1-9]+.*")) {
                        User_list.setPassword(reply);
                        stepBook = 14;
                        sendMessage.setText("Update Successfully" + "\nReply 0: Back to Main Menu");
                        sendMessage.setText("Pls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");

                    }
                    else{
                      //  stepBook=18;
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Pls enter again your password (Need to more than 4 value and contain alphabet and number)\n\nReply 0: Back to Main Menu");
                    }



                    break;
                }
                case 19: {

                    if(reply.length()==10 & reply.matches(".*[.]+.*" )){
                        Booking_info.setBooking_Date(reply);

                        stepBook = 20;
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Please enter start  time  of booking meeting room.\n(In 24hour format eg:11:20, 09:20, 14:00)\n\nReply 0: Back to Main Menu");}
                    else{
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Invalid date format. Please insert again your Booking Date.\n\nReply 0: Back to Main Menu");}






                    break;
                }
                case 20: {

                    if(reply.length()==5 & reply.matches(".*[:]+.*" )) {
                        Booking_info.setBooking_StartTime(reply);
                        covertStartTime(reply);

                        stepBook = 21;
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Please enter end  time  of booking meeting room.(eg: 11:20)\n");
                    }else{

                        stepBook = 20;
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Invalid time format. Please enter start  time  of booking meeting room again.\n");
                    }


                    break;
                }
                case 21: {

                    if(reply.length()==5 & reply.matches(".*[:]+.*" )) {
                        covertEndTime(reply);

                        if(Time.getStartTime()>Time.getEndTime())   {
                            sendMessage.setChatId(chatId);
                            sendMessage.setText("End time must be greater than end time.Please enter end time  of booking meeting room again.\n\nReply 0: Back to Main Menu");

                        }else {

                        Booking_info.setBooking_EndTime(reply);

                        stepBook = 22;
                        sendMessage.setChatId(chatId);

                        sendMessage.setText("May I know the purpose of you booking?\n\n" +
                                "Reply 1: Group Discussion\n" +
                                "Reply 2: Hold Meeting\n" +
                                "Reply 3: Conduct lecture\n" +
                                "Reply 4: Take a quiz\n" +
                                "Reply 5: Use equipment\n\n" +
                                "Reply 0: Back to Main Menu");}
                    }else{

                        stepBook = 21;
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Invalid time format. Please enter end time  of booking meeting room again.\n");
                    }



                    break;
                }
                case 22: {


                    sendMessage.setChatId(chatId);
                    if (reply.equals("1") || reply.equals("2") || reply.equals("3") || reply.equals("4") || reply.equals("5")) {
                        switch (reply) {
                            case "1":
                                //   purpose = "Group Discussion";
                                Booking_info.setPurpose( "Group Discussion");
                                break;
                            case "2":
                                // purpose = "Hold Meeting";
                                Booking_info.setPurpose( "Hold Meeting");
                                break;
                            case "3":
                                // purpose = "Conduct lecture";
                                Booking_info.setPurpose( "Conduct lecture");
                                break;
                            case "4":
                                //  purpose = "Take a quiz";
                                Booking_info.setPurpose(  "Take a quiz");
                                break;
                            case "5":
                                // purpose = "Use equipment";
                                Booking_info.setPurpose( "Use equipment");
                                break;
                        }

                        stepBook = 23;
                        String rr=  SQLite.displaySchool();
                        sendMessage.setText("Before booking a room, you need choose the building location.\nThis is a list for school:\n"+rr+"\nPlease Insert School Id you want (eg:S11)\n\n\nReply 0: Back to Main Menu");
                    } else {
                        sendMessage.setText("Please enter 1-5 only\n\nReply 0: Back to Main Menu");
                    }
                    break;



                }
                case 23: {

                    sendMessage.setChatId(chatId);
                    String rr= SQLite.displayAvailableRoom(reply);

                    if(rr.equals("Invalid school ID")){

                        sendMessage.setText(rr+ ". Please enter again\n\nReply 0: Back to Main Menu");

                    }else {

                        stepBook = 25;
                        sendMessage.setText("This is a  list for available room:\n" + rr + "\nPlease Insert Room Id you want \n\n\nReply 0: Back to Main Menu");
                    }
                    break;





                }
                case 24:{
                    sendMessage.setChatId(chatId);
                    String rr= SQLite.displayAvailableRoom(reply);

                    if(rr.equals("Invalid school ID")){

                        sendMessage.setText(rr+ "Please enter school ID again\n\nReply 0: Back to Main Menu");

                    }else {

                        stepBook = 5;
                        sendMessage.setText("This is a  list for available room:\n" + rr + "\n\nPlease Insert Room Id you want \n\n\nReply 0: Back to Main Menu");
                    }
                    break;
                }
                case 25:{
                    Booking_info.setRoomId(reply);
                    String rr=SQLite.selectRoom(reply);
                    if(rr.equals("Invalid room ID")){
                        sendMessage.setChatId(chatId);

                        sendMessage.setText("Invalid room ID, please enter again. \n\nReply 0: Back to Main Menu");

                    }else {
                        sendMessage.setChatId(chatId);
                        stepBook = 14;

                         sendMessage.setText("Update Successfully.\nPls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");
                    }

                    break;
                }
                }




            switch (stepCancel) {
                case 1: {
                    User_list.setEmail(reply);
                    sendMessage.setChatId(chatId);
                    String rr=SQLite.select1(User_list.getEmail());

                    if (rr.equals("No")) {
                        sendMessage.setText("Sorry, I can't find the booked meeting room with this email, please enter again\n\nReply 0: Back to Main Menu");
                    } else {
                        stepCancel = 2;
                        sendMessage.setText("Please provide your password\n\nReply 0: Back to Main Menu");
                    }

                    break;
                }
                case 2:{
                    User_list.setPassword(reply);
                    sendMessage.setChatId(chatId);

                    String rr=SQLite.selectPass(User_list.getPassword());
                    if (rr.equals("No")) {
                        sendMessage.setText("Sorry, your password is wrong, please enter again\n\nReply 0: Back to Main Menu");
                    } else {
                    stepCancel = 3;
                        sendMessage.setText("I found your booking detail\n" + SQLite.selectPass(reply) +
                                "\nDo you want to cancel your booking?\nReply 1 : Yes \nReply 2: No \n\nReply 0: Back to Main Menu");
                    }
                    break;
                }
                case 3:{
                    sendMessage.setChatId(chatId);
                    if (reply.equals("1")) {
                        SQLite.delete();
                        sendMessage.setText("Booking record was deleted successfully!");
                    } else if (reply.equals("2")) {
                        stepCancel = 0;
                        sendMessage.setText("Stop canceling meeting room booking records");
                    }
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    sendMessage.setChatId(chatId);
                    sendMessage.setText("Click /booking if you want book a meeting room\n" +
                            "Click /cancel if you want to cancel a meeting room\n" +
                            "Click /list if you want to display the list of users");
                    break;


                }
            }

            switch (stepEdit) {
                case 1: {
                    sendMessage.setChatId(chatId);

                    User_list.setEmail(reply);
                    String rr=SQLite.select1(User_list.getEmail());

                    if (rr.equals("No")) {
                        sendMessage.setText("Sorry, I can't find the booked meeting room with this email,please enter again\n\nReply 0: Back to Main Menu");
                    } else {
                        stepEdit = 2;
                        sendMessage.setText("Please provide your password\n\nReply 0: Back to Main Menu");
                    }

                    break;
                }
                case 2:{
                    User_list.setPassword(reply);
                    sendMessage.setChatId(chatId);

                    String rr=SQLite.editDisplay(User_list.getPassword());
                    if (rr.equals("No")) {
                        sendMessage.setText("Sorry, your password is wrong, please enter again\n\nReply 0: Back to Main Menu");
                    } else {
                        stepEdit = 3;

                        sendMessage.setText("I found your booking detail\n" +rr +
                                "\n\n\nReply 0: Back to Main Menu");
                    }
                    break;
                }
                case 3:{
                    sendMessage.setChatId(chatId);

                  if (reply.equals("1")) {

                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Please provide a preferred booking date for meeting room? \n(eg: 25.12.2022)\n\n" +
                                "Reply 0: Back to Main Menu");
                        stepEdit = 10;
                        break;

                    }else if ( reply.equals("2") || reply.equals("3") || reply.equals("4") || reply.equals("5")|| reply.equals("6")) {
                        switch (reply) {

                            case "2":
                                stepEdit = 4;
                                sendMessage.setText("Kindly provide your ICNo you want update.\n\nReply 0: Back to Main Menu");
                                break;
                            case "3":
                                stepEdit = 5;
                                sendMessage.setText("Kindly provide your name you want update.\n\nReply 0: Back to Main Menu");
                                break;
                            case "4":
                                stepEdit = 6;
                                sendMessage.setText("Kindly provide your TelNo you want update.\n\nReply 0: Back to Main Menu");
                                break;
                            case "5":

                                stepEdit = 7;
                                sendMessage.setText("Kindly provide your Password you want update.\n\nReply 0: Back to Main Menu");
                                break;
                        }


                    }
                    break;
                }
                case 4:{
                    sendMessage.setChatId(chatId);

                    if (reply.matches(".*[a-zA-Z]+.*")) { //if contain a-z *
                        sendMessage.setText("Ic number should not contain alphabet, please enter again\n\nReply 0: Back to Main Menu");
                    } else {
                        if (reply.length() == 14) {
                            User_list.setICNO(reply);
                            SQLite.editIC(reply);

                           stepEdit= 9;
                            sendMessage.setText("Update Successfully \n\nPls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");


                        } else {
                            sendMessage.setText("Failed, please enter 14 digit number including '-'\n\nReply 0: Back to Main Menu");
                        }
                    }

                    break;
                }
                case 5:{
                    sendMessage.setChatId(chatId);
                    User_list.setName(reply);
                    SQLite.editName(reply);

                    stepEdit= 9;
                    sendMessage.setText("Update Successfully \n\nPls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");
                    break;
                }
                case 6:{
                    sendMessage.setChatId(chatId);

                    if (reply.length() < 10 || reply.length() > 11) {
                        sendMessage.setText("Please enter 10 or 11 digit phone number\n\nReply 0: Back to Main Menu");
                    }  else {
                        User_list.setMobile_TelNo(reply);
                        SQLite.editTelNo(reply);

                        stepEdit= 9;
                        sendMessage.setText("Update Successfully \n\nPls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");

                    }

                    break;
                }

                case 7:{
                    sendMessage.setChatId(chatId);

                    if(reply.length()>4 && reply.matches(".*[a-zA-Z]+.*")&&  reply.matches(".*[1-9]+.*")) {
                        User_list.setPassword(reply);
                        SQLite.editPass(reply);

                        stepEdit = 9;
                        sendMessage.setText("Update Successfully \n\nPls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");


                    }
                    else{
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Pls enter again your password (Need to more than 4 value and contain alphabet and number)\n\nReply 0: Back to Main Menu");
                    }


                    break;
                }
                case 9: {
                    sendMessage.setChatId(chatId);

                    if (reply.equals("1")) {

                        String rr=SQLite.editDisplay(User_list.getPassword());

                            stepEdit = 3;
                            sendMessage.setText("Booking detail\n" +rr +
                                    "\nPlease enter the number, if you want edit again \n\nReply 0: Back to Main Menu");

                    }

                    break;
                }
                case 10:{

                    if(reply.length()==10 & reply.matches(".*[.]+.*" )){
                        Booking_info.setBooking_Date(reply);

                        stepEdit  = 11;
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Please enter start  time  of booking meeting room.\n(In 24hour format eg:11:20, 09:20, 14:00)\n\nReply 0: Back to Main Menu");}
                    else{
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Invalid date format. Please insert again your Booking Date.\n\nReply 0: Back to Main Menu");}

                    break;
                }
                case 11:{

                    if(reply.length()==5 & reply.matches(".*[:]+.*" )) {
                        Booking_info.setBooking_StartTime(reply);
                        covertStartTime(reply);

                        stepEdit  = 13;
                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Please enter end  time  of booking meeting room.(eg: 11:20)\n");
                    }else{

                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Invalid time format. Please enter start  time  of booking meeting room again.\n");
                    }

                    break;
                }
                case 13:{
                    if(reply.length()==5 & reply.matches(".*[:]+.*" )) {

                        covertEndTime(reply);

                        if(Time.getStartTime()>Time.getEndTime())   {
                            sendMessage.setChatId(chatId);
                            sendMessage.setText("End time must be greater than end time.Please enter end time  of booking meeting room again.\n\nReply 0: Back to Main Menu");

                        }else {
                        Booking_info.setBooking_EndTime(reply);

                        stepEdit = 14;
                        sendMessage.setChatId(chatId);

                        sendMessage.setText("May I know the purpose of you booking?\n\n" +
                                "Reply 1: Group Discussion\n" +
                                "Reply 2: Hold Meeting\n" +
                                "Reply 3: Conduct lecture\n" +
                                "Reply 4: Take a quiz\n" +
                                "Reply 5: Use equipment\n\n" +
                                "Reply 0: Back to Main Menu");}
                    }else{

                        sendMessage.setChatId(chatId);
                        sendMessage.setText("Invalid time format. Please enter end time  of booking meeting room again.\n");
                    }



                    break;
                }
                case 14:{
                    sendMessage.setChatId(chatId);
                    if (reply.equals("1") || reply.equals("2") || reply.equals("3") || reply.equals("4") || reply.equals("5")) {
                        switch (reply) {
                            case "1":
                                //   purpose = "Group Discussion";
                                Booking_info.setPurpose( "Group Discussion");
                                break;
                            case "2":
                                // purpose = "Hold Meeting";
                                Booking_info.setPurpose( "Hold Meeting");
                                break;
                            case "3":
                                // purpose = "Conduct lecture";
                                Booking_info.setPurpose( "Conduct lecture");
                                break;
                            case "4":
                                //  purpose = "Take a quiz";
                                Booking_info.setPurpose(  "Take a quiz");
                                break;
                            case "5":
                                // purpose = "Use equipment";
                                Booking_info.setPurpose( "Use equipment");
                                break;
                        }

                        stepEdit = 15;
                        String rr=  SQLite.displaySchool();
                        sendMessage.setText("Before booking a room, you need choose the building location.\nThis is a list for school:\n"+rr+"\nPlease Insert School Id you want (eg:S11)\n\n\nReply 0: Back to Main Menu");
                    } else {
                        sendMessage.setText("Please enter 1-5 only\n\nReply 0: Back to Main Menu");
                    }
                    break;
                }
                case 15:{
                    sendMessage.setChatId(chatId);
                    String rr= SQLite.displayAvailableRoom(reply);

                    if(rr.equals("Invalid school ID")){

                        sendMessage.setText(rr+ "Please enter again\n\nReply 0: Back to Main Menu");

                    }else {

                        stepEdit = 16;
                        sendMessage.setText("This is a  list for available room.\n" + rr + "\n\nPlease Insert Room Id you want \n\n\nReply 0: Back to Main Menu");
                    }
                    break;

                }
                case 16:{
                    Booking_info.setRoomId(reply);
                    String rr=SQLite.selectRoom(reply);

                    if(rr.equals("Invalid room ID")){
                        sendMessage.setChatId(chatId);

                        sendMessage.setText("Invalid room ID, please enter again. \n\nReply 0: Back to Main Menu");

                    }else {
                        sendMessage.setChatId(chatId);
                        stepEdit = 9;
SQLite.editBooking(Booking_info.getBooking_Date(),Booking_info.getBooking_StartTime(),Booking_info.getBooking_EndTime(),Booking_info.getPurpose(),Booking_info.getRoomId());
                        sendMessage.setText("Update Successfully.\nPls check your info \n\nReply 1: Yes \n\nReply 0: Back to Main Menu");
                    }

                    break;
                }
            }


                try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void covertEndTime(String reply) {

        String[] arr = reply.split(":");

        // Converting hours into integer
        int hh = Integer.parseInt(arr[0]);
        String hour = String.format("%02d", hh);
        String minute = arr[1];
        System.out.print(hour + ":" + minute  );
        Time.setEndTime(Integer.parseInt(hour));
    }

    private void covertStartTime(String time) {

        String[] arr = time.split(":");

        // Converting hours into integer
        int hh = Integer.parseInt(arr[0]);
        String hour = String.format("%02d", hh);
        String minute = arr[1];
        System.out.print(hour + ":" + minute  );
        Time.setStartTime(Integer.parseInt(hour));
    }
}
