/*
* Programmer:   Amber Murphy
* TUID:         MURPH32
* Course:       CITP 290: Spring 2014
* Main Program: PopApp.java
* Assignment:   Phase 3
* Summary:      Create GUI for phase 2 content.
*
*/

package murph32.core;


import java.io.Serializable;

/**
 * <code>User</code> class.
 * This class was created during class.
 * This class creates an object to define a user.
 * Most methods in this class are auto-generated.
 * <br><code>toDataString</code> and <code>printProduct</code> methods are based on class examples.
 * <br>String constant <code>MANAGER_LEVEL</code> defines a user as a manager. (not currently being used).
 * <br>String constant <code>EMPLOYEE_LEVEL</code> defines a user as an employee. (not currently being used).
 * <br>String <code>username</code> stores the username.
 * <br>char[] <code>password</code> stores the password.
 * <br>String <code>accessLevel</code> stores the access level of the user(this isn't currently being used).
 *
 * @author hoffmanz
 * @author Amber Murphy
 */
public class User implements Serializable {
    private static final long serialVersionUID = 296615436555921185L;
    private String username;
    private char[] password;
    private String accessLevel;

    public User() {
    }

    public User(String username, char[] password, String accessLevel) {

        this.username = username;
        this.password = password;
        this.accessLevel = accessLevel;
    }

    public User(String userData) {
        String[] data = userData.split("\t", -1);
        username = data[0];
        password = data[1].toCharArray();
        accessLevel = data[2];

    }

    public User(User u) {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.padWithSpaces(username, 15));
        sb.append(StringUtils.padWithSpaces(String.valueOf(accessLevel), 11));
        return sb.toString();
    }
}
