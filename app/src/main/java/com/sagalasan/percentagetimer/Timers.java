package com.sagalasan.percentagetimer;

/**
 * Created by Christiaan on 3/28/2015.
 */
public class Timers
{
    private int _id;
    private String _timername;
    private long _startdate;
    private long _enddate;

    public Timers()
    {
        this._timername = "";
        this._startdate = 0;
        this._enddate = 0;
    }

    public Timers(String name)
    {
        this._timername = name;
        this._startdate = 0;
        this._enddate = 0;
    }

    public Timers(String name, long sDate, long fDate)
    {
        this._timername = name;
        this._startdate = sDate;
        this._enddate = fDate;
    }

    public long get_enddate() {
        return _enddate;
    }

    public void set_enddate(long _enddate) {
        this._enddate = _enddate;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_timername() {
        return _timername;
    }

    public void set_timername(String _timername) {
        this._timername = _timername;
    }

    public long get_startdate() {
        return _startdate;
    }

    public void set_startdate(long _startdate) {
        this._startdate = _startdate;
    }
}
