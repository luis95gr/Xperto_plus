package com.example.luis9.xperto_plus.estadisticas;

public class variablesBoDias {
    private String fecha,ritmo,respiracion,presion,fNormal,fCansado,fMuy,mCalmado,mExcite,mDepri;

    public variablesBoDias(String fecha, String ritmo, String respiracion, String presion, String fNormal, String fCansado, String fMuy, String mCalmado, String mExcite, String mDepri) {
        this.fecha = fecha;
        this.ritmo = ritmo;
        this.respiracion = respiracion;
        this.presion = presion;
        this.fNormal = fNormal;
        this.fCansado = fCansado;
        this.fMuy = fMuy;
        this.mCalmado = mCalmado;
        this.mExcite = mExcite;
        this.mDepri = mDepri;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getRitmo() {
        return ritmo;
    }

    public void setRitmo(String ritmo) {
        this.ritmo = ritmo;
    }

    public String getRespiracion() {
        return respiracion;
    }

    public void setRespiracion(String respiracion) {
        this.respiracion = respiracion;
    }

    public String getPresion() {
        return presion;
    }

    public void setPresion(String presion) {
        this.presion = presion;
    }

    public String getfNormal() {
        return fNormal;
    }

    public void setfNormal(String fNormal) {
        this.fNormal = fNormal;
    }

    public String getfCansado() {
        return fCansado;
    }

    public void setfCansado(String fCansado) {
        this.fCansado = fCansado;
    }

    public String getfMuy() {
        return fMuy;
    }

    public void setfMuy(String fMuy) {
        this.fMuy = fMuy;
    }

    public String getmCalmado() {
        return mCalmado;
    }

    public void setmCalmado(String mCalmado) {
        this.mCalmado = mCalmado;
    }

    public String getmExcite() {
        return mExcite;
    }

    public void setmExcite(String mExcite) {
        this.mExcite = mExcite;
    }

    public String getmDepri() {
        return mDepri;
    }

    public void setmDepri(String mDepri) {
        this.mDepri = mDepri;
    }
}
