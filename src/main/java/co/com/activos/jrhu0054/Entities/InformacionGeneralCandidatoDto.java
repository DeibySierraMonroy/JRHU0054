/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.com.activos.jrhu0054.Entities;

/**
 *
 * @author desierra
 */
public class InformacionGeneralCandidatoDto {

    private String epl_nd;
    private String tdc_td_epl;
    private String tdc_td_fil;
    private String emp_nd_fil;
    private String emp_nom_filial;
    private String emp_nom_principal;
    private String nombre_empleado;
    private String cargo_empleado;
    private String correo_empleado;
    private String observacion_causal;
    private String estado_libro;
    private String telefono;
    private String sucursal_administrativa;
    private String sucursal_filial;
    private String departamento;
    private String ciudad;
    private String respuesWs;
    private Integer numeroIntentos;

    public String getRespuesWs() {
        return respuesWs;
    }

    public void setRespuesWs(String respuesWs) {
        this.respuesWs = respuesWs;
    }

    public Integer getNumeroIntentos() {
        return numeroIntentos;
    }

    public void setNumeroIntentos(Integer numeroIntentos) {
        this.numeroIntentos = numeroIntentos;
    }
    

    public String getTdc_td_epl() {
        return tdc_td_epl;
    }

    public void setTdc_td_epl(String tdc_td_epl) {
        this.tdc_td_epl = tdc_td_epl;
    }

    public String getTdc_td_fil() {
        return tdc_td_fil;
    }

    public void setTdc_td_fil(String tdc_td_fil) {
        this.tdc_td_fil = tdc_td_fil;
    }

    public String getEmp_nd_fil() {
        return emp_nd_fil;
    }

    public void setEmp_nd_fil(String emp_nd_fil) {
        this.emp_nd_fil = emp_nd_fil;
    }

    public String getEmp_nom_filial() {
        return emp_nom_filial;
    }

    public void setEmp_nom_filial(String emp_nom_filial) {
        this.emp_nom_filial = emp_nom_filial;
    }

    public String getEmp_nom_principal() {
        return emp_nom_principal;
    }

    public void setEmp_nom_principal(String emp_nom_principal) {
        this.emp_nom_principal = emp_nom_principal;
    }

    public String getNombre_empleado() {
        return nombre_empleado;
    }

    public void setNombre_empleado(String nombre_empleado) {
        this.nombre_empleado = nombre_empleado;
    }

    public String getCargo_empleado() {
        return cargo_empleado;
    }

    public void setCargo_empleado(String cargo_empleado) {
        this.cargo_empleado = cargo_empleado;
    }

    public String getCorreo_empleado() {
        return correo_empleado;
    }

    public void setCorreo_empleado(String correo_empleado) {
        this.correo_empleado = correo_empleado;
    }

    public String getObservacion_causal() {
        return observacion_causal;
    }

    public void setObservacion_causal(String observacion_causal) {
        this.observacion_causal = observacion_causal;
    }

    public String getEstado_libro() {
        return estado_libro;
    }

    public void setEstado_libro(String estado_libro) {
        this.estado_libro = estado_libro;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSucursal_administrativa() {
        return sucursal_administrativa;
    }

    public void setSucursal_administrativa(String sucursal_administrativa) {
        this.sucursal_administrativa = sucursal_administrativa;
    }

    public String getSucursal_filial() {
        return sucursal_filial;
    }

    public void setSucursal_filial(String sucursal_filial) {
        this.sucursal_filial = sucursal_filial;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEpl_nd() {
        return epl_nd;
    }

    public void setEpl_nd(String epl_nd) {
        this.epl_nd = epl_nd;
    }

    @Override
    public String toString() {
        return "InformacionGeneralCandidatoDto{" + "epl_nd=" + epl_nd + ", tdc_td_epl=" + tdc_td_epl + ", tdc_td_fil=" + tdc_td_fil + ", emp_nd_fil=" + emp_nd_fil + ", emp_nom_filial=" + emp_nom_filial + ", emp_nom_principal=" + emp_nom_principal + ", nombre_empleado=" + nombre_empleado + ", cargo_empleado=" + cargo_empleado + ", correo_empleado=" + correo_empleado + ", observacion_causal=" + observacion_causal + ", estado_libro=" + estado_libro + ", telefono=" + telefono + ", sucursal_administrativa=" + sucursal_administrativa + ", sucursal_filial=" + sucursal_filial + ", departamento=" + departamento + ", ciudad=" + ciudad + '}';
    }
    

}
