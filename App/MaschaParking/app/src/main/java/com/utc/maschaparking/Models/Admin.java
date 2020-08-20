package com.utc.maschaparking.Models;

public class Admin {



    private Integer idAdmin;
    private String cedulaAdmin;
    private String nombresAdmin;
    private String apelldioAdmin;
    private String telefonoAdmin;
    private String correoAdmin;
    private String enableAdmin;
    private String direccionAdmin;

    public String getDireccionAdmin() {
        return direccionAdmin;
    }

    public void setDireccionAdmin(String direccionAdmin) {
        this.direccionAdmin = direccionAdmin;
    }



    public Integer getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(Integer idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getCedulaAdmin() {
        return cedulaAdmin;
    }

    public void setCedulaAdmin(String cedulaAdmin) {
        this.cedulaAdmin = cedulaAdmin;
    }

    public String getNombresAdmin() {
        return nombresAdmin;
    }

    public void setNombresAdmin(String nombresAdmin) {
        this.nombresAdmin = nombresAdmin;
    }

    public String getApelldioAdmin() {
        return apelldioAdmin;
    }

    public void setApelldioAdmin(String apelldioAdmin) {
        this.apelldioAdmin = apelldioAdmin;
    }

    public String getTelefonoAdmin() {
        return telefonoAdmin;
    }

    public void setTelefonoAdmin(String telefonoAdmin) {
        this.telefonoAdmin = telefonoAdmin;
    }

    public String getCorreoAdmin() {
        return correoAdmin;
    }

    public void setCorreoAdmin(String correoAdmin) {
        this.correoAdmin = correoAdmin;
    }

    public String getEnableAdmin() {
        return enableAdmin;
    }

    public void setEnableAdmin(String enableAdmin) {
        this.enableAdmin = enableAdmin;
    }
}
