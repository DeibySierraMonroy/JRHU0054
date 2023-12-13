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

import javax.ejb.Local;

/**
 *
 * @author desierra
 */
@Local
public interface SeguimientoRepo {
    
    DatabaseResultDto<CoordinadorDto> listarCoordinadores ();
    
    DatabaseResultDto<SeguimientoContratosAnalistaDto> listarContratosPorAnalista(String usuario);
    
    DatabaseResultDto<SeguimientoContratosAnalistaDto> listarContratosPorAnalistaExpirados(String usuario);
    
    DatabaseResultDto<SeguimientoCandidato> seguimientoCandidato(Long libConsecutivo);
    
    DatabaseResultDto<InformacionGeneralCandidatoDto> consultarInformacionGeneralDelCandidato(Long libConsecutivo);
    
    DatabaseResultDto<?> crearAuditoria(String urlEnviada,String respuesta,Long libConsecutivo,Integer numIntento, String tipoRespuesta,String nombreDelAplicativo,String respuestaDelProceso);
    
    DatabaseResultDto<SeguimientoContratosAnalistaDto> listarContratosPorCoordinadores(String nombreAnalista,String nombreCoordinador);

    DatabaseResultDto<?> crearAuditoria(String log, DatabaseResultStatus tipo , String aplicativo, String transaccion, String estadoTransaccion);
    
     DatabaseResultDto<InformacionGeneralDelAnalista> consultarInformacionAnalista(String usuario);

}
