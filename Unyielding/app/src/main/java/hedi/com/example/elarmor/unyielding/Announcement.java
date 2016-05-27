package hedi.com.example.elarmor.unyielding;

/**
 * Created by eLarmor on 11/5/2015.
 */
public class Announcement {

    private int _id;
    private String _title;
    private String _location;
    private String _content;

    public Announcement() {
    }

    public Announcement(String _title, String _location, String _content) {
        this._title = _title;
        this._location = _location;
        this._content = _content;
    }

    public int get_id() {
        return _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public void set_id(int _id) {
        this._id = _id;
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
