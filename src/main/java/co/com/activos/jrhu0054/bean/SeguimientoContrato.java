package co.com.activos.jrhu0054.bean;

import co.com.activos.jrhu0054.Entities.CoordinadorDto;
import co.com.activos.jrhu0054.Entities.DatabaseResultDto;
import co.com.activos.jrhu0054.Entities.InformacionGeneralCandidatoDto;
import co.com.activos.jrhu0054.Entities.InformacionGeneralDelAnalista;
import co.com.activos.jrhu0054.Entities.SeguimientoCandidato;
import co.com.activos.jrhu0054.Entities.SeguimientoContratosAnalistaDto;
import static co.com.activos.jrhu0054.Enum.DatabaseResultStatus.SUCCESS;
import co.com.activos.jrhu0054.Repositories.SeguimientoRepo;
import static co.com.activos.jrhu0054.Utilities.SessionUtils.getParameterValue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author desierra
 */
@Named(value = "seguimientoContrato")
@ViewScoped
public class SeguimientoContrato implements Serializable {

    private List<CoordinadorDto> coordinadores;
    private List<SeguimientoContratosAnalistaDto> analistaDtos;
    private List<SeguimientoContratosAnalistaDto> contratosVencidos;
    private List<SeguimientoCandidato> estados;
    private List<String> fecha;
    private List<Date> rangoDeFechas;

    private CoordinadorDto coordinadorRecuperado;
    private SeguimientoContratosAnalistaDto contratosAnalistaDto;
    private InformacionGeneralCandidatoDto candidatoDto;
    private InformacionGeneralDelAnalista analista;

    private Long totalCandidatosFinalizados;
    private Long totalCandidatosPendienteFirma;
    private Long totalCandidatosPendienteRepresentante;
    private String totalEstados;
    private String activarSecciones = "tabSeguimiento";
    private String fechaInicial;
    private String fechaFinal;
    private String sesionActiva;

    private boolean accesoIndex;
    private boolean accesoSeguimiento = false;
    private static final String CONSTANTE_ERROR = "ERROR";

    private String valor;

    private HashMap<String, String> listaHomologacionEventos = new HashMap<>();
    private HashMap<String, String> estadosHomologacion = new HashMap<>();
    private HashMap<String, Boolean> accesoAFucnionalidades = new HashMap<>();

    @Inject
    private SeguimientoRepo seguimientoRepo;

    @PostConstruct
    public void init() {
        sesionActiva = (String) getParameterValue("usu_usuario");
        listarCoordinadores();
        listarCandidatosPorAnalista();
        ConsultarContratosVencidos();
        informacionDelAnalista();
        this.accesoIndex = true;
        this.accesoSeguimiento = false;
        accesoAFucnionalidades.put("Inicio", true);
        accesoAFucnionalidades.put("accesoModuloSeguimiento", false);
        cargarEstadosHomologados();
    }

    public void cargarEstadosHomologados() {
        listaHomologacionEventos.put("AGREEMENT_CREATED", "ACUERDO CREADO");
        listaHomologacionEventos.put("AGREEMENT_EMAIL_VIEWED", "ACUERDO VISTO");
        listaHomologacionEventos.put("AGREEMENT_SIGNER_NAME_CHANGED_BY_SIGNER", "ACUERDO FIRMADO");
        listaHomologacionEventos.put("AGREEMENT_EXPIRED", "ACUERDO EXPIRADO");
        listaHomologacionEventos.put("AGREEMENT_ACTION_REQUESTED", "ACUERDO SOLICITADO");
        listaHomologacionEventos.put("AGREEMENT_WORKFLOW_COMPLETED", "ACUERDO COMPLETADO");
        listaHomologacionEventos.put("AGREEMENT_ACTION_COMPLETED", "ACUERDO ACCION COMPLETADA");
        estadosHomologacion.put("PFC", "PENDIENTE FIRMA CANDIDATO");
        estadosHomologacion.put("CTO", "CONTRATADO");
        estadosHomologacion.put("PFR", "PENDIENTE FIRMA REPRESENTANTE");
    }

    public void listarCoordinadores() {
        try {
            DatabaseResultDto<CoordinadorDto> listaCoordinadores = seguimientoRepo.listarCoordinadores();
            if (SUCCESS.equals(listaCoordinadores.getStatus())) {
                this.coordinadores = listaCoordinadores.getListResult();
                limpiarCampos();
            }
        } catch (Exception e) {
            System.out.println("Error al traer la informacion listarCoordinadores" + e.getStackTrace());
        }

    }

    public void listarCandidatosPorAnalista() {
        try {
            DatabaseResultDto<SeguimientoContratosAnalistaDto> databaseResultDto = seguimientoRepo.listarContratosPorAnalista(this.sesionActiva);
            if (SUCCESS.equals(databaseResultDto.getStatus())) {
                analistaDtos = databaseResultDto.getListResult();
                totalCandidatosFinalizados = analistaDtos
                        .stream()
                        .filter(candidato -> candidato.getEstadoActualDelLibro()
                        .equals("CTO"))
                        .count();
                totalCandidatosPendienteFirma = analistaDtos
                        .stream()
                        .filter(candidato -> candidato.getEstadoActualDelLibro()
                        .equals("PFC"))
                        .count();
                totalCandidatosPendienteRepresentante = analistaDtos
                        .stream()
                        .filter(candidato -> candidato.getEstadoActualDelLibro()
                        .equals("PFR"))
                        .count();
                this.analistaDtos
                        .stream()
                        .map(seguimientoContratosAnalistaDto -> seguimientoContratosAnalistaDto.getEstadoActualDelLibro())
                        .collect(Collectors.toList());
                listarCoordinadores();

            }
        } catch (Exception e) {
            System.out.println("Error al traer la informacion listarCandidatosPorAnalista" + e.getStackTrace());
        }

    }

    public void buscarContratosPorCoordinador() {
        try {
            DatabaseResultDto<SeguimientoContratosAnalistaDto> databaseResultDto
                    = seguimientoRepo.listarContratosPorCoordinadores("WFORERO", coordinadorRecuperado.getNombreUsuario());
            if (SUCCESS.equals(databaseResultDto.getStatus())) {
                this.analistaDtos = databaseResultDto.getListResult();
                this.totalCandidatosFinalizados = analistaDtos.stream().filter(candidato -> candidato.getEstadoActualDelLibro().equals("CTO")).count();
                this.totalCandidatosPendienteFirma = analistaDtos.stream().filter(candidato -> candidato.getEstadoActualDelLibro().equals("PFC")).count();
                this.totalCandidatosPendienteRepresentante = analistaDtos.stream().filter(candidato -> candidato.getEstadoActualDelLibro().equals("PFR")).count();
                limpiarCampos();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void buscarInformacionSegunEstado(String estado) {
        try {
            if (Objects.isNull(this.coordinadorRecuperado)) {
                listarCandidatosPorAnalista();
                this.analistaDtos = this.analistaDtos.stream()
                        .filter(seguimientoContratosAnalistaDto -> estado.equals(seguimientoContratosAnalistaDto.getEstadoActualDelLibro()))
                        .collect(Collectors.toList());
            } else {
                this.buscarContratosPorCoordinador();
                this.analistaDtos = this.analistaDtos.stream()
                        .filter(seguimientoContratosAnalistaDto -> estado.equals(seguimientoContratosAnalistaDto.getEstadoActualDelLibro()))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void seguimientoFirmaContrato() {
        try {
            DatabaseResultDto<SeguimientoCandidato> databaseResultDto = seguimientoRepo.seguimientoCandidato(contratosAnalistaDto.getNumeroDelLibro());
            if (SUCCESS.equals(databaseResultDto.getStatus())) {
                this.estados = databaseResultDto.getListResult().stream()
                        .map(seguimientoCandidato
                                -> homologarEvento(seguimientoCandidato))
                        .collect(Collectors.toList());
                if (!estados.isEmpty()) {
                    totalEstados = estados.get(0).getFecha();
                }
                obtenerInformacionCandidato();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void obtenerInformacionCandidato() {
        try {
            DatabaseResultDto<InformacionGeneralCandidatoDto> databaseResultDto
                    = seguimientoRepo.consultarInformacionGeneralDelCandidato(contratosAnalistaDto.getNumeroDelLibro());
            if (SUCCESS.equals(databaseResultDto.getStatus())) {
                candidatoDto = databaseResultDto.getSingleResult();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void reenvioFirmaArchivosConcatenados() {
        try {
            Process proc = Runtime.getRuntime().exec(contratosAnalistaDto.getUrlEnviada());
            BufferedReader respuestaExistosa = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String respuesta = null;
            String concatenar = "";
            while (Objects.nonNull(respuesta = respuestaExistosa.readLine())) {
                System.out.println("Repuesta : " + respuesta);
                concatenar = respuesta;
            }

            if (concatenar.contains(CONSTANTE_ERROR)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error !", "El Proceso no se pudo Finalizar con Exito !."));
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exitoso !", "Se ha reenviado el contrato de manera Exitosa !."));

            seguimientoRepo.crearAuditoria(contratosAnalistaDto.getUrlEnviada(), concatenar, contratosAnalistaDto.getNumeroDelLibro(), 1, "seguimientoContratos", "JADM0067:mesaContratosBean:firmarArchivosConcatenados", "mesaContratos");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String validarEltiempoTrancurrido(String tiempo) {
        String background = "#059669";
        Integer tiempoTotal = 0;
        Integer validarHoras = 0;
        tiempoTotal = Integer.parseInt(tiempo.substring(5, 7));
        validarHoras = Integer.parseInt(tiempo.substring(1, 3));
        if (validarHoras >= 1) {
            background = "#DC2626";
        }
        if (tiempoTotal > 3 && tiempoTotal < 5 && validarHoras < 1) {
            background = "#f57c00";
        }
        if (tiempoTotal >= 5) {
            background = "#DC2626";
        }

        return background;
    }

    public String convertidorFecha(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return simpleDateFormat.format(date);

    }

    public void guardarFecha() {
        List<String> listaFechaFormateada = rangoDeFechas.stream().map(date -> convertidorFecha(date)).collect(Collectors.toList());
        fechaInicial = listaFechaFormateada.get(0);
        fechaFinal = listaFechaFormateada.get(1);
        this.accesoIndex = false;
        this.accesoSeguimiento = true;

    }

    public void limpiarCampos() {
        estados = new ArrayList<>();
        candidatoDto = new InformacionGeneralCandidatoDto();

    }

    public SeguimientoCandidato homologarEvento(SeguimientoCandidato valor) {
        SeguimientoCandidato seguimientoCandidato = new SeguimientoCandidato();
        seguimientoCandidato.setEvento(this.listaHomologacionEventos.get(valor.getEvento()));
        seguimientoCandidato.setFecha(valor.getFecha());
        return seguimientoCandidato;
    }

    public void ConsultarContratosVencidos() {
        try {
            DatabaseResultDto<SeguimientoContratosAnalistaDto> databaseResultDto = seguimientoRepo.listarContratosPorAnalistaExpirados(this.sesionActiva);
            if (SUCCESS.equals(databaseResultDto.getStatus())) {
                contratosVencidos = databaseResultDto.getListResult();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String obtenerObservacionSegunElEstado(String estadoAHomologar) {
        return this.estadosHomologacion.get(estadoAHomologar);
    }

    public void informacionDelAnalista() {
        try {
            DatabaseResultDto<InformacionGeneralDelAnalista> databaseResultDto = seguimientoRepo.consultarInformacionAnalista(sesionActiva);
            if (SUCCESS.equals(databaseResultDto.getStatus())) {
                analista = databaseResultDto.getSingleResult();
            }
        } catch (Exception e) {
            System.out.println("Error al Consultar la Informacion del Analista" + e.getMessage());
        }

    }

    public List<CoordinadorDto> getCoordinadores() {
        return coordinadores;
    }

    public void setCoordinadores(List<CoordinadorDto> coordinadores) {
        this.coordinadores = coordinadores;
    }

    public CoordinadorDto getCoordinadorRecuperado() {
        return coordinadorRecuperado;
    }

    public void setCoordinadorRecuperado(CoordinadorDto coordinadorRecuperado) {
        this.coordinadorRecuperado = coordinadorRecuperado;
    }

    public List<SeguimientoContratosAnalistaDto> getAnalistaDtos() {
        return analistaDtos;
    }

    public void setAnalistaDtos(List<SeguimientoContratosAnalistaDto> analistaDtos) {
        this.analistaDtos = analistaDtos;
    }

    public SeguimientoContratosAnalistaDto getContratosAnalistaDto() {
        return contratosAnalistaDto;
    }

    public void setContratosAnalistaDto(SeguimientoContratosAnalistaDto contratosAnalistaDto) {
        this.contratosAnalistaDto = contratosAnalistaDto;
    }

    public Long getTotalCandidatosFinalizados() {
        return totalCandidatosFinalizados;
    }

    public void setTotalCandidatosFinalizados(Long totalCandidatosFinalizados) {
        this.totalCandidatosFinalizados = totalCandidatosFinalizados;
    }

    public Long getTotalCandidatosPendienteFirma() {
        return totalCandidatosPendienteFirma;
    }

    public void setTotalCandidatosPendienteFirma(Long totalCandidatosPendienteFirma) {
        this.totalCandidatosPendienteFirma = totalCandidatosPendienteFirma;
    }

    public List<String> getFecha() {
        return fecha;
    }

    public void setFecha(List<String> fecha) {
        this.fecha = fecha;
    }

    public Long getTotalCandidatosPendienteRepresentante() {
        return totalCandidatosPendienteRepresentante;
    }

    public void setTotalCandidatosPendienteRepresentante(Long totalCandidatosPendienteRepresentante) {
        this.totalCandidatosPendienteRepresentante = totalCandidatosPendienteRepresentante;
    }

    public List<SeguimientoCandidato> getEstados() {
        return estados;
    }

    public void setEstados(List<SeguimientoCandidato> estados) {
        this.estados = estados;
    }

    public String getTotalEstados() {
        return totalEstados;
    }

    public void setTotalEstados(String totalEstados) {
        this.totalEstados = totalEstados;
    }

    public List<SeguimientoContratosAnalistaDto> getContratosVencidos() {
        return contratosVencidos;
    }

    public void setContratosVencidos(List<SeguimientoContratosAnalistaDto> contratosVencidos) {
        this.contratosVencidos = contratosVencidos;
    }

    public String getActivarSecciones() {
        return activarSecciones;
    }

    public void setActivarSecciones(String activarSecciones) {
        this.activarSecciones = activarSecciones;
    }

    public InformacionGeneralCandidatoDto getCandidatoDto() {
        return candidatoDto;
    }

    public void setCandidatoDto(InformacionGeneralCandidatoDto candidatoDto) {
        this.candidatoDto = candidatoDto;
    }

    public InformacionGeneralDelAnalista getAnalista() {
        return analista;
    }

    public void setAnalista(InformacionGeneralDelAnalista analista) {
        this.analista = analista;
    }

    public String getSesionActiva() {
        return sesionActiva;
    }

    public void setSesionActiva(String sesionActiva) {
        this.sesionActiva = sesionActiva;
    }

    public List<Date> getRangoDeFechas() {
        return rangoDeFechas;
    }

    public void setRangoDeFechas(List<Date> rangoDeFechas) {
        this.rangoDeFechas = rangoDeFechas;
    }

    public boolean isAccesoIndex() {
        return accesoIndex;
    }

    public void setAccesoIndex(boolean accesoIndex) {
        this.accesoIndex = accesoIndex;
    }

    public boolean isAccesoSeguimiento() {
        return accesoSeguimiento;
    }

    public void setAccesoSeguimiento(boolean accesoSeguimiento) {
        this.accesoSeguimiento = accesoSeguimiento;
    }

    public HashMap<String, Boolean> getAccesoAFucnionalidades() {
        return accesoAFucnionalidades;
    }

    public void setAccesoAFucnionalidades(HashMap<String, Boolean> accesoAFucnionalidades) {
        this.accesoAFucnionalidades = accesoAFucnionalidades;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
