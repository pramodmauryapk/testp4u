package my.app.p4ulibrary.classes;

public class User {
    private String userId;
    private String userName;
    private String userEmail;
    private String userRole;
    private String userMobile;
    private String userAddress;
    private String userIdentity;
    private String userStatus;

    public User() {

    }


    public User(String userId,
                String userName,
                String userEmail,
                String userRole,
                String userMobile,
                String userAddress,
                String userIdentity,
                String userStatus) {
        this.userId=userId;
        this.userName=userName;
        this.userEmail=userEmail;
        this.userRole=userRole;
        this.userMobile=userMobile;
        this.userAddress=userAddress;
        this.userIdentity=userIdentity;
        this.userStatus=userStatus;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserAddress() { return userAddress;   }

    public void setUserAddress(String userAddress) { this.userAddress = userAddress;    }

    public String getUserIdentity() {        return userIdentity;    }

    public void setUserIdentity(String userIdentity) {        this.userIdentity = userIdentity;    }

    public String getUserStatus() {        return userStatus;    }

    public void setUserStatus(String userStatus) {        this.userStatus = userStatus;    }

}
