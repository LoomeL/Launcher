package pro.gravit.launchserver.dao.impl;

import pro.gravit.launcher.ClientPermissions;
import pro.gravit.launchserver.dao.User;
import pro.gravit.utils.helper.LogHelper;
import pro.gravit.utils.helper.SecurityHelper;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

@Entity
@Table(name = "users")
public class UserHibernateImpl implements User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    public String username;
    public String email;
    @Column(unique = true)
    public UUID uuid;
    @Column(name = "password")
    private byte[] password;
    private String accessToken;
    public String serverID;
    private String password_salt;
    public long permissions;

    public void setPassword(String password) {
        password_salt = SecurityHelper.randomStringAESKey();
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            LogHelper.error(e);
            return;
        }
        this.password = digest.digest(password.concat(password_salt).getBytes(StandardCharsets.UTF_8));
    }

    public boolean verifyPassword(String password) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            LogHelper.error(e);
            return false;
        }
        byte[] enpassword = digest.digest(password.concat(password_salt).getBytes(StandardCharsets.UTF_8));
        return Arrays.equals(enpassword, this.password);
    }

    public ClientPermissions getPermissions() {
        return new ClientPermissions(permissions);
    }

    public void setPermissions(ClientPermissions permissions) {
        this.permissions = permissions.toLong();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getServerID() {
        return serverID;
    }

    @Override
    public void setServerID(String serverID) {
        this.serverID = serverID;
    }
}
