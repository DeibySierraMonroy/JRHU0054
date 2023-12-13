/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.activos.jrhu0054.Repositories;

import co.com.activos.jrhu0054.Entities.CoordinadorDto;
import co.com.activos.jrhu0054.Entities.DatabaseResultDto;
import co.com.activos.jrhu0054.Entities.InformacionGeneralCandidatoDto;
import co.com.activos.jrhu0054.Entities.InformacionGeneralDelAnalista;
import co.com.activos.jrhu0054.Entities.SeguimientoCandidato;
import co.com.activos.jrhu0054.Entities.SeguimientoContratosAnalistaDto;
import co.com.activos.jrhu0054.Enum.DatabaseResultStatus;
import co.com.activos.jrhu0054.Utilities.OracleConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

import oracle.jdbc.OracleType;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author desierra
 */
@Stateless
public class SeguimientoRepoImpl implements SeguimientoRepo {

    @Override
    public DatabaseResultDto<CoordinadorDto> listarCoordinadores() {
        try ( Connection connection = OracleConnection.getConnection()) {
            String consulta = "call RHU.QB_SEGUIMIENTO_CONTRATO.pl_consultar_coordinadores(?,?,?)";
            try ( CallableStatement callableStatement = connection.prepareCall(consulta)) {
                callableStatement.registerOutParameter("vcconsulta", OracleTypes.CURSOR);
                callableStatement.registerOutParameter("vcestado_proceso", OracleTypes.VARCHAR);
                callableStatement.registerOutParameter("vcmensaje_proceso", OracleTypes.VARCHAR);
                callableStatement.execute();
                ResultSet resultSet = (ResultSet) callableStatement.getObject("vcconsulta");
                List<CoordinadorDto> coordinadorDtos = new ArrayList<>();
                while (resultSet.next()) {
                    CoordinadorDto coordinadorDto = new CoordinadorDto();
                    coordinadorDto.setNombreUsuario(resultSet.getString("nombre_usuario"));
                    coordinadorDto.setUsuUsuario(resultSet.getString("usuario"));
                    coordinadorDto.setUbicacion(resultSet.getString("ubicacion"));
                    coordinadorDtos.add(coordinadorDto);
                }
                return new DatabaseResultDto<>(DatabaseResultStatus.SUCCESS, "Consulta Exitosa", coordinadorDtos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseResultDto<>(DatabaseResultStatus.ERROR, "Error al traer la informacion en SeguimientoRepoImpl:listarCoordinadores  causado por : " + e.getMessage());
        }
    }

    @Override
    public DatabaseResultDto<SeguimientoContratosAnalistaDto> listarContratosPorAnalista(String usuario) {
        try ( Connection connection = OracleConnection.getConnection()) {
            String consulta = "call RHU.QB_SEGUIMIENTO_CONTRATO.pl_consultar_informacion_ana(?,?,?,?)";
            try ( CallableStatement callableStatement = connection.prepareCall(consulta)) {
                callableStatement.setString("vcusuario", usuario);
                callableStatement.registerOutParameter("vcconsulta", OracleTypes.CURSOR);
                callableStatement.registerOutParameter("vcestado_proceso", OracleTypes.VARCHAR);
                callableStatement.registerOutParameter("vcmensaje_proceso", OracleTypes.VARCHAR);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject("vcconsulta");
                List<SeguimientoContratosAnalistaDto> analistaDtos = new ArrayList<>();
                while (resultSet.next()) {
                    SeguimientoContratosAnalistaDto analistaDto = new SeguimientoContratosAnalistaDto();
                    analistaDto.setEstadoActualDelLibro(resultSet.getString("estado_lib"));
                    analistaDto.setFechaDeRegistro(resultSet.getString("fecha_registro"));
                    analistaDto.setFechaDelUltimoEstado(resultSet.getString("fecha_estado"));
                    analistaDto.setNombreCompletoCandidato(resultSet.getString("nombre_empleado"));
                    analistaDto.setNombreEmpresaFilial(resultSet.getString("empresa"));
                    analistaDto.setTelefonoCandidato(resultSet.getString("telefono"));
                    analistaDto.setTiempoTranscurrido(resultSet.getString("timepo_trancurrido"));
                    analistaDto.setTipoDocumentoCandidato(resultSet.getString("tipo_documento"));
                    analistaDto.setTipoDocumentoEmpresaFilial(resultSet.getString("tipo_documento_filial"));
                    analistaDto.setNumeroDelLibro(resultSet.getLong("lib_consecutivo"));
                    analistaDto.setNumeroDocumentoCandidato(resultSet.getLong("numero_documento"));
                    analistaDto.setNumeroDocumentoFilial(resultSet.getLong("numero_documento_filial"));
                    analistaDto.setResponsable(resultSet.getString("responsable"));
                    analistaDto.setUrlEnviada(resultSet.getString("url_enviada"));
                    analistaDto.setRespuestaWS(resultSet.getString("respuestaws"));
                    analistaDtos.add(analistaDto);
                }
                return new DatabaseResultDto<>(DatabaseResultStatus.SUCCESS, "Consulta Exitosa", analistaDtos);

            }
        } catch (Exception e) {
            return new DatabaseResultDto<>(DatabaseResultStatus.ERROR, "Error al traer la informacion en SeguimientoRepoImpl:listarContratosPorAnalista  causado por : " + e.getMessage());
        }
    }

    @Override
    public DatabaseResultDto<SeguimientoCandidato> seguimientoCandidato(Long libConsecutivo) {
        try ( Connection connection = OracleConnection.getConnection()) {
            String consulta = "call RHU.QB_SEGUIMIENTO_CONTRATO.pl_consultar_seguimiento_can(?,?,?,?)";
            try ( CallableStatement callbleStatement = connection.prepareCall(consulta)) {
                callbleStatement.setLong("nmlibconsecutivo", libConsecutivo);
                callbleStatement.registerOutParameter("vcestado_proceso", OracleTypes.VARCHAR);
                callbleStatement.registerOutParameter("vcmensaje_proceso", OracleTypes.VARCHAR);
                callbleStatement.registerOutParameter("vcconsulta", OracleTypes.CURSOR);
                callbleStatement.execute();

                ResultSet resultSet = (ResultSet) callbleStatement.getObject("vcconsulta");
                List<SeguimientoCandidato> eventos = new ArrayList<>();
                while (resultSet.next()) {
                    SeguimientoCandidato candidato = new SeguimientoCandidato();
                    candidato.setEvento(resultSet.getString("TIPO_EVENTO"));
                    candidato.setFecha(resultSet.getString("AUD_FECHA"));
                    eventos.add(candidato);
                }
                return new DatabaseResultDto<>(DatabaseResultStatus.SUCCESS, "Consulta Exitosa", eventos);
            }
        } catch (Exception e) {
            return new DatabaseResultDto<>(DatabaseResultStatus.ERROR, "Error al traer la informacion en SeguimientoRepoImpl:seguimientoCandidato  causado por" + e.getMessage());
        }
    }

    @Override
    public DatabaseResultDto<SeguimientoContratosAnalistaDto> listarContratosPorCoordinadores(String nombreAnalista, String nombreCoordinador) {
        try ( Connection connection = OracleConnection.getConnection()) {
            String consulta = "call RHU.QB_SEGUIMIENTO_CONTRATO.pl_consultar_contratos_coor(?,?,?,?,?)";
            try ( CallableStatement callableStatement = connection.prepareCall(consulta)) {
                callableStatement.setString("vcusuario", nombreAnalista);
                callableStatement.setString("vccoordinador", nombreCoordinador);
                callableStatement.registerOutParameter("vcconsulta", OracleTypes.CURSOR);
                callableStatement.registerOutParameter("vcestado_proceso", OracleTypes.VARCHAR);
                callableStatement.registerOutParameter("vcmensaje_proceso", OracleTypes.VARCHAR);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject("vcconsulta");
                List<SeguimientoContratosAnalistaDto> analistaDtos = new ArrayList<>();
                while (resultSet.next()) {
                    SeguimientoContratosAnalistaDto analistaDto = new SeguimientoContratosAnalistaDto();
                    analistaDto.setEstadoActualDelLibro(resultSet.getString("estado_lib"));
                    analistaDto.setFechaDeRegistro(resultSet.getString("fecha_registro"));
                    analistaDto.setFechaDelUltimoEstado(resultSet.getString("fecha_estado"));
                    analistaDto.setNombreCompletoCandidato(resultSet.getString("nombre_empleado"));
                    analistaDto.setNombreEmpresaFilial(resultSet.getString("empresa"));
                    analistaDto.setTelefonoCandidato(resultSet.getString("telefono"));
                    analistaDto.setTiempoTranscurrido(resultSet.getString("timepo_trancurrido"));
                    analistaDto.setTipoDocumentoCandidato(resultSet.getString("tipo_documento"));
                    analistaDto.setTipoDocumentoEmpresaFilial(resultSet.getString("tipo_documento_filial"));
                    analistaDto.setNumeroDelLibro(resultSet.getLong("lib_consecutivo"));
                    analistaDto.setNumeroDocumentoCandidato(resultSet.getLong("numero_documento"));
                    analistaDto.setNumeroDocumentoFilial(resultSet.getLong("numero_documento_filial"));
                    analistaDto.setResponsable(resultSet.getString("responsable"));
                    analistaDto.setUrlEnviada(resultSet.getString("url_enviada"));
                    analistaDto.setRespuestaWS(resultSet.getString("respuestaws"));
                    analistaDtos.add(analistaDto);
                }
                return new DatabaseResultDto<>(DatabaseResultStatus.SUCCESS, "Consulta Exitosa", analistaDtos);

            }
        } catch (Exception e) {
            return new DatabaseResultDto<>(DatabaseResultStatus.ERROR, "Error al traer la informacion en SeguimientoRepoImpl:listarContratosPorAnalista  causado por : " + e.getMessage());
        }
    }

    @Override
    public DatabaseResultDto<?> crearAuditoria(String log, DatabaseResultStatus tipo, String aplicativo, String transaccion, String estadoTransaccion) {
        String resultado = "";
        try ( Connection connection = OracleConnection.getConnection()) {
            String consulta = "call adm.qb_JADM0067_mesa_contratos.pl_insertar_auditoria(?,?,?,?,?,?,?)";
            try ( CallableStatement call = connection.prepareCall(consulta)) {

                //Parametros  de entrada
                call.setString("VCLOG", log);
                call.setString("VCTYPE_ERROR", String.valueOf(tipo));
                call.setString("VCAPLICATIVO", aplicativo);
                call.setString("VCTRANSACCION", transaccion);
                call.setString("VCESTADOTRANSACCION", estadoTransaccion);

                //Parametros de Salida
                call.registerOutParameter("vcestadoproceso", OracleType.VARCHAR2);
                call.registerOutParameter("vcmensajeproceso", OracleType.VARCHAR2);
                call.execute();

                resultado = call.getString("vcmensajeproceso");
            }
            return new DatabaseResultDto<>(DatabaseResultStatus.SUCCESS, "Consulta realizada correctamente", resultado);

        } catch (Exception e) {
            return new DatabaseResultDto<>(DatabaseResultStatus.WARNING, "Error no controlado en CrearDirectorioDao.obtenerTipoDocumentosDet, causado por: " + e.getMessage());
        }
    }

    @Override
    public DatabaseResultDto<SeguimientoContratosAnalistaDto> listarContratosPorAnalistaExpirados(String usuario) {
        try ( Connection connection = OracleConnection.getConnection()) {
            String consulta = "call RHU.QB_SEGUIMIENTO_CONTRATO.pl_consultar_informacion_exp(?,?,?,?)";
            try ( CallableStatement callableStatement = connection.prepareCall(consulta)) {
                callableStatement.setString("vcusuario", usuario);
                callableStatement.registerOutParameter("vcconsulta", OracleTypes.CURSOR);
                callableStatement.registerOutParameter("vcestado_proceso", OracleTypes.VARCHAR);
                callableStatement.registerOutParameter("vcmensaje_proceso", OracleTypes.VARCHAR);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject("vcconsulta");
                List<SeguimientoContratosAnalistaDto> analistaDtos = new ArrayList<>();
                while (resultSet.next()) {
                    SeguimientoContratosAnalistaDto analistaDto = new SeguimientoContratosAnalistaDto();
                    analistaDto.setEstadoActualDelLibro(resultSet.getString("estado_lib"));
                    analistaDto.setFechaDeRegistro(resultSet.getString("fecha_registro"));
                    analistaDto.setFechaDelUltimoEstado(resultSet.getString("fecha_estado"));
                    analistaDto.setNombreCompletoCandidato(resultSet.getString("nombre_empleado"));
                    analistaDto.setNombreEmpresaFilial(resultSet.getString("empresa"));
                    analistaDto.setTelefonoCandidato(resultSet.getString("telefono"));
                    analistaDto.setTiempoTranscurrido(resultSet.getString("timepo_trancurrido"));
                    analistaDto.setTipoDocumentoCandidato(resultSet.getString("tipo_documento"));
                    analistaDto.setTipoDocumentoEmpresaFilial(resultSet.getString("tipo_documento_filial"));
                    analistaDto.setNumeroLibroContratoExpirado(resultSet.getLong("lib_consecutivo"));
                    analistaDto.setNumeroDocumentoCandidato(resultSet.getLong("numero_documento"));
                    analistaDto.setNumeroDocumentoFilial(resultSet.getLong("numero_documento_filial"));
                    analistaDto.setResponsable(resultSet.getString("responsable"));
                    analistaDto.setEstadoTemporalLibro(resultSet.getString("estadoTemporal"));
                    analistaDto.setObservacionTemporal(resultSet.getString("observacionTemporal"));
                    analistaDtos.add(analistaDto);
                }
                return new DatabaseResultDto<>(DatabaseResultStatus.SUCCESS, "Consulta Exitosa", analistaDtos);

            }
        } catch (Exception e) {
            return new DatabaseResultDto<>(DatabaseResultStatus.ERROR, "Error al traer la informacion en SeguimientoRepoImpl:listarContratosPorAnalista  causado por : " + e.getMessage());
        }
    }

    @Override
    public DatabaseResultDto<InformacionGeneralCandidatoDto> consultarInformacionGeneralDelCandidato(Long libConsecutivo) {

        try ( Connection connection = OracleConnection.getConnection()) {
            String consulta = "call RHU.QB_SEGUIMIENTO_CONTRATO.pl_consultar_informacion_can(?,?,?,?)";
            try ( CallableStatement callableStatement = connection.prepareCall(consulta)) {

                callableStatement.setLong("nmlibconsecutivo", libConsecutivo);

                callableStatement.registerOutParameter("vcconsulta", OracleTypes.CURSOR);
                callableStatement.registerOutParameter("vcestado_proceso", OracleTypes.VARCHAR);
                callableStatement.registerOutParameter("vcmensaje_proceso", OracleTypes.VARCHAR);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject("vcconsulta");

                InformacionGeneralCandidatoDto candidatoDto = new InformacionGeneralCandidatoDto();
                while (resultSet.next()) {
                    candidatoDto.setCargo_empleado(resultSet.getString("cargo_empleado"));
                    candidatoDto.setCiudad(resultSet.getString("ciudad"));
                    candidatoDto.setCorreo_empleado(resultSet.getString("correo_empleado"));
                    candidatoDto.setDepartamento(resultSet.getString("departamento"));
                    candidatoDto.setEmp_nd_fil(resultSet.getString("emp_nd_fil"));
                    candidatoDto.setEmp_nom_filial(resultSet.getString("emp_nom_filial"));
                    candidatoDto.setEmp_nom_principal(resultSet.getString("emp_nom_principal"));
                    candidatoDto.setEstado_libro(resultSet.getString("estado_libro"));
                    candidatoDto.setNombre_empleado(resultSet.getString("nombre_empleado"));
                    candidatoDto.setSucursal_administrativa(resultSet.getString("sucursal_administrativa"));
                    candidatoDto.setSucursal_filial(resultSet.getString("sucursal_filial"));
                    candidatoDto.setDepartamento(resultSet.getString("departamento"));
                    candidatoDto.setCiudad(resultSet.getString("ciudad"));
                    candidatoDto.setEpl_nd(resultSet.getString("epl_nd"));
                    candidatoDto.setTdc_td_epl(resultSet.getString("tdc_td_epl"));
                    candidatoDto.setTdc_td_fil(resultSet.getString("tdc_td_fil"));
                    candidatoDto.setTelefono(resultSet.getString("telefono"));
                    candidatoDto.setRespuesWs(resultSet.getString("respuestaws"));
                    candidatoDto.setNumeroIntentos(resultSet.getInt("numeroIntentos"));
                }

                return new DatabaseResultDto<>(DatabaseResultStatus.SUCCESS, "Consulta Exitosa", candidatoDto);
            }

        } catch (Exception e) {
            return new DatabaseResultDto<>(DatabaseResultStatus.ERROR, "Error al traer la informacion en SeguimientoRepoImpl:consultarInformacionGeneralDelCandidato  causado por : " + e.getMessage());
        }

    }

    @Override
    public DatabaseResultDto<?> crearAuditoria(String urlEnviada, String respuesta, Long libConsecutivo, Integer numIntento, String tipoRespuesta, String nombreDelAplicativo, String respuestaDelProceso) {
        String resultado = "";
        try ( Connection connection = OracleConnection.getConnection()) {
            String consulta = "call adm.qb_JADM0067_mesa_contratos.pl_insertar_envio_concatenado(?,?,?,?,?,?,?,?)";
            try ( CallableStatement call = connection.prepareCall(consulta)) {

                //Parametros  de entrada
                call.setString("VCURL_ENVIADA", urlEnviada);
                call.setString("VCRESPUESTA", respuesta);
                call.setString("VCAPLICATIVO", nombreDelAplicativo);
                call.setLong("NMLIB_CONSECUTIVO", libConsecutivo);
                call.setInt("NMINTENTO", numIntento);
                call.setString("VCESTADOTRANSACCION", respuestaDelProceso);

                //Parametros de Salida
                call.registerOutParameter("vcestadoproceso", OracleType.VARCHAR2);
                call.registerOutParameter("vcmensajeproceso", OracleType.VARCHAR2);
                call.execute();

                resultado = call.getString("vcmensajeproceso");

                call.close();

            }
            return new DatabaseResultDto<>(DatabaseResultStatus.SUCCESS, "Consulta realizada correctamente", resultado);

        } catch (Exception e) {
            return new DatabaseResultDto<>(DatabaseResultStatus.WARNING, "Error no controlado en CrearDirectorioDao.obtenerTipoDocumentosDet, causado por: " + e.getMessage());
        }
    }

    @Override
    public DatabaseResultDto<InformacionGeneralDelAnalista> consultarInformacionAnalista(String usuario) {
        try (Connection connection = OracleConnection.getConnection()) {
            String consulta = "call RHU.QB_SEGUIMIENTO_CONTRATO.pl_consultar_info_analista(?,?,?,?)";
            try (CallableStatement callableStatement = connection.prepareCall(consulta)) {

               callableStatement.setString("vcusuario", usuario);
               
                callableStatement.registerOutParameter("vcconsulta", OracleTypes.CURSOR);
                callableStatement.registerOutParameter("vcestado_proceso", OracleTypes.VARCHAR);
                callableStatement.registerOutParameter("vcmensaje_proceso", OracleTypes.VARCHAR);
                callableStatement.execute();
                
                ResultSet resultSet = (ResultSet) callableStatement.getObject("vcconsulta");
                InformacionGeneralDelAnalista  analista = new InformacionGeneralDelAnalista();
                while(resultSet.next()){
                    analista.setTipoDeIdentificacion(resultSet.getString("tipo_documento"));
                    analista.setNumeroDeDocumento(resultSet.getString("documento"));
                    analista.setNombreCompleto(resultSet.getString("nombre_completo"));
                }
                callableStatement.close();
                return new DatabaseResultDto<>(DatabaseResultStatus.SUCCESS, "Consulta realizada correctamente",analista);
            }
        } catch (Exception e) {
            return new DatabaseResultDto<>(DatabaseResultStatus.WARNING, "Error no controlado en consultarInformacionAnalista, causado por: " + e.getMessage());
        }
        

    }
}
