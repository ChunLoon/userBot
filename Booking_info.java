package my.uum;

/**
 * This class is for save  and edit the  booking data
 */
public class Booking_info extends bot {
    static String Purpose = "";
    static String Booking_Date = "";
    static String Booking_StartTime = "";
    static String Booking_EndTime = "";

    static String RoomId = "";


    /**
     * This method is for return value purpose
     *
     * @return purpose
     */
    public static String getPurpose() {
        return Purpose;
    }

    /**
     * This method is for edit/save  String value purpose of meeting
     */
    public static void setPurpose(String purpose) {
        Purpose = purpose;
    }

    /**
     * This method is for return value Booking Date
     *
     * @return BookingDate
     */
    public static String getBooking_Date() {
        return Booking_Date;
    }


    /**
     * This method is for edit/save  String value booking date
     */
    public static void setBooking_Date(String booking_Date) {
        Booking_Date = booking_Date;
    }

    /**
     * This method is for return value Booking Start Time
     *
     * @return Booking Time
     */
    public static String getBooking_StartTime() {
        return Booking_StartTime;
    }

    /**
     * This method is for edit/save String value booking start time
     */
    public static void setBooking_StartTime(String booking_StartTime ) {
        Booking_StartTime = booking_StartTime;
    }

    /**
     * This method is for return value Booking End Time
     *
     * @return Booking Time
     */
    public static String getBooking_EndTime() {
        return Booking_EndTime;
    }
    /**
     * This method is for edit/save String value booking end time
     */
    public static void setBooking_EndTime(String booking_EndTime) {
        Booking_EndTime = booking_EndTime;
    }

    /**
     * This method is for return value room id
     *
     * @return room id
     */
    public static String getRoomId() {
        return RoomId;
    }

    /**
     * This method is for edit room Id
     * @param roomId
     */
    public static void setRoomId(String roomId) {
        RoomId = roomId;
    }
}
