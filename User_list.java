package my.uum;

/**
 * This class is for save  and edit the  user data
 */
public class User_list extends  bot {

  //  static String Staff_id= "";

    static String name="";
    static String Mobile_TelNo= "";
    static String email= "";

    static String ICNO= "";
    static String Password= "";

    /**
     * This method is for return value ICNO
     * @return ICNO
     */
    public static String getICNO() {
        return ICNO;
    }
    /**
     * This method is for displaying String value ICNO
     */
    public static void setICNO(String ICNO) {
        User_list.ICNO = ICNO;
    }

    /**
     * This method is for return value name
     * @return name
     */

    public static String getName() {
        return name;
    }

    /**
     * This method is for edit/save String value name
     */
    public static void setName(String name) {
        User_list.name = name;
    }

    /**
     * This method is for return value handphone number
     * @return handphone number
     */
    public static String getMobile_TelNo() {
        return Mobile_TelNo;
    }
    /**
     * This method is for edit/save  String value handphone number
     */
    public static void setMobile_TelNo(String mobile_TelNo) {
        Mobile_TelNo = mobile_TelNo;
    }

    /**
     * This method is for return value email
     * @return email
     */
    public static String getEmail() {
        return email;
    }
    /**
     * This method is for edit/save  String value email
     */
    public static void setEmail(String email) {
        User_list.email = email;
    }


    /**
     *
     * @return value password
     */
    public static String getPassword() {
        return Password;
    }

    /**
     * this method is to edit password
     * @param password
     */
    public static void setPassword(String password) {
        Password = password;
    }


}

