package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below-mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashMap<String,User> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashMap<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }
    //My code below
    public String createUser(String name, String mobile) {
        //If the mobile number exists in database, throw "User already exists" exception
        //Otherwise, create the user and return "SUCCESS"
        if(userMobile.containsKey(mobile)){
            return "User already exists";
        }
        //creating user
        userMobile.put(mobile,new User(name,mobile));
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        // The list contains at least 2 users where the first user is the admin. A group has exactly one admin.
        // If there are only 2 users, the group is a personal chat and the group name should be kept as the name of the second user(other than admin)
        // If there are 2+ users, the name of group should be "Group count". For example, the name of first group would be "Group 1", second would be "Group 2" and so on.
        // Note that a personal chat is not considered a group and the count is not updated for personal chats.
        // If group is successfully created, return group.

        //For example: Consider userList1 = {Alex, Bob, Charlie}, userList2 = {Dan, Evan}, userList3 = {Felix, Graham, Hugh}.
        //If createGroup is called for these userLists in the same order, their group names would be "Group 1", "Evan", and "Group 2" respectively.

        if(users.size() == 2) {
            //Personal Chat
            Group personal = new Group(users.get(1).getName(),2);
            groupUserMap.put(personal,users);
            return personal;
        }
        //Group Chat
        customGroupCount++;//Group count incremented
        Group group = new Group("Group "+customGroupCount,users.size());//Group Created
        groupUserMap.put(group,users);//Group Added to group map
        adminMap.put(group,users.get(0));//Group and Admin added to admin map
        return group;
    }

    public int createMessage(String content) {
        // The 'i^th' created message has message id 'i'.
        // Return the message id.
        messageId++;
        Message message = new Message(messageId,content);//Message created with message id: messageId;
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) {
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.
        if(!groupUserMap.containsKey(group)) {
            return -1;//mentioned group does not exist
        }
        if(!groupUserMap.get(group).contains(sender))
            return -2;//sender is not a member of the group
        //sending message operations
        if(groupMessageMap.containsKey(group)){
            groupMessageMap.get(group).add(message);
        }else {
            List<Message> msg = new ArrayList<>();
            msg.add(message);
            groupMessageMap.put(group,msg);
        }
        senderMap.put(message,sender);
        return groupMessageMap.get(group).size();
    }

    public String changeAdmin(User approver, User user, Group group) {
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only
        // one admin and the admin rights are transferred from approver to user.

        if(!groupUserMap.containsKey(group))
        {//mentioned group does not exist
            return "NG";
        }
        if(!adminMap.get(group).equals(approver))
        {//approver is not the current admin of the group
            return "NA";
        }
        if(!groupUserMap.get(group).contains(user))
        {//user is not a part of the group
            return "NU";
        }
        adminMap.remove(group);//Removing older admin group pair
        adminMap.put(group,user);//Adding new admin to that group
        return "SUCCESS";
    }

    public int removeUser(User user) {
        return 1;//Dummy return
    }

    public String findMessage(Date start, Date end, int k) {
        return "Dummy Return";
    }
}
