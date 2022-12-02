package com.mat.modularservers.module;


import com.mat.modularservers.util.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

//TODO: Ban, Kick, IPBan notifications.
public class ClientList implements Iterable<SocketWrapper> {
    private String
            welcomeMessage = "Welcome to the server!",
            usernameAlreadyExistsMessage = "Username already exists.",
            bannedMessage = "You are banned from this server.",
            credentialsFailureMessage = "login failed, please check username or password.",
            alreadyLoggedInMessage = "Account is already logged in.";
    private boolean isForceLogin = false;
    private final FileSet blacklist = new FileSet("blacklist.txt");
    private final FileMap passwords = new FileMap("login.txt");
    private final Set<SocketWrapper> pendingList = Collections.synchronizedSet(new HashSet<>());
    private final Set<SocketWrapper> clients = Collections.synchronizedSet(new HashSet<>());
    private final HashMap<String, SocketWrapper> queryMap = new HashMap<>();
    private final ArrayBlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(1024);

    public boolean join(SocketWrapper client) {
        if (isBlacklisted(client)) {
            client.sendMessage(bannedMessage, MessageFlag.BAN_REPLY);
            client.stop();
            return false;
        } else if (isForceLogin && !credentialsCheck(client)) {
            client.sendMessage(credentialsFailureMessage, MessageFlag.LOGIN_FAILED);
            pendingList.add(client);
            return false;
        } else if (add(client)) {
            client.sendMessage(welcomeMessage, MessageFlag.LOGIN_SUCCEEDED);
            client.setStatus(Status.CONNECTED);
            return true;
        } else {
            client.sendMessage(alreadyLoggedInMessage, MessageFlag.LOGIN_FAILED);
            return false;
        }
    }

    public boolean remove(SocketWrapper client) {
        if (clients.contains(client)) {
            clients.remove(client);
            queryMap.remove(client.getName());
            queryMap.remove(client.getSocketAddress());
            queryMap.remove(client.getCredentials().username());
            client.stop();
            return true;
        }
        return false;
    }

    private boolean credentialsCheck(SocketWrapper client) {
        return passwords.containsKey(client.getCredentials().username()) && passwords.get(client.getCredentials().username()).equals(client.getCredentials().password());
    }

    private boolean isBlacklisted(SocketWrapper client) {
        return blacklist.contains(client.getSocketAddress()) || blacklist.contains(client.getCredentials().username());
    }

    public boolean ban(String user, boolean ip) {
        SocketWrapper client = queryMap.get(user);
        blacklist.add(ip ? client.getSocketAddress() : client.getCredentials().username());
        return remove(client);
    }

    public boolean kick(String user) {
        return remove(queryMap.get(user));
    }

    public boolean isForceLogin() {
        return isForceLogin;
    }

    public void setForceLogin(boolean forceLogin) {
        isForceLogin = forceLogin;
    }

    public void register(SocketWrapper socket) {
        if (!passwords.containsKey(socket.getCredentials().username())) {
            passwords.put(socket.getCredentials().username(), socket.getCredentials().password());
            socket.sendMessage(usernameAlreadyExistsMessage, MessageFlag.REGISTER_SUCCEEDED);
        } else {
            socket.sendMessage(usernameAlreadyExistsMessage, MessageFlag.REGISTER_FAILED);
        }
    }

    public Set<SocketWrapper> getPending() {
        return pendingList;
    }

    public SocketWrapper get(String query) {
        return queryMap.get(query);
    }

    public void close() {
        clients.forEach(this::remove);
    }

    private boolean add(SocketWrapper client) {
        if (queryMap.containsKey(client.getCredentials().username())) {
            return false;
        } else {
            queryMap.put(client.getCredentials().username(), client);
            queryMap.put(client.getSocketAddress(), client);
            queryMap.put(client.getName(), client);
            clients.add(client);
            return true;
        }
    }

    public Message getMessage() throws InterruptedException {
        return messageQueue.take();
    }

    public ArrayBlockingQueue<Message> getMessageQueue() {
        return messageQueue;
    }

    @NotNull
    @Override
    public Iterator<SocketWrapper> iterator() {
        return clients.iterator();
    }

    @Override
    public String toString() {
        return ArrayUtil.recursiveToString(queryMap.entrySet());
    }
}
