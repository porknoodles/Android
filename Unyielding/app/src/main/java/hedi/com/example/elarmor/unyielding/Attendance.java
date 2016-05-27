package hedi.com.example.elarmor.unyielding;

public class Attendance {

    private int _id;
    private String _date;
    private String _location;
    private String _content;

    public Attendance() {
    }

    public Attendance(String _date, String _location, String _content) {
        this._date = _date;
        this._location = _location;
        this._content = _content;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_location() {
        return _location;
    }

    public void set_location(String _location) {
        this._location = _location;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }
}
