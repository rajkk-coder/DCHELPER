package com.example.dchelper.scholar;

public class PanelMember {
    private String FacultyName;
    private String Mode;
    private String ScholarName;

    public PanelMember() {
    }

    public PanelMember(String facultyName, String mode, String scholarName) {
        FacultyName = facultyName;
        Mode = mode;
        ScholarName = scholarName;
    }

    public String getFacultyName() {
        return FacultyName;
    }

    public String getMode() {
        return Mode;
    }

    public void setFacultyName(String facultyName) {
        FacultyName = facultyName;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public void setScholarName(String scholarName) {
        ScholarName = scholarName;
    }

    public String getScholarName() {
        return ScholarName;
    }
}
