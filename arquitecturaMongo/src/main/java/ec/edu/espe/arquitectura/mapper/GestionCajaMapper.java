package ec.edu.espe.arquitectura.mapper;

import ec.edu.espe.arquitectura.dto.AbrirTurnoRequest;
import ec.edu.espe.arquitectura.dto.ProcesarTransaccionRequest;
import ec.edu.espe.arquitectura.dto.CerrarTurnoRequest;
import ec.edu.espe.arquitectura.dto.TurnoResponse;
import ec.edu.espe.arquitectura.entity.TurnoCaja;
import ec.edu.espe.arquitectura.entity.TransaccionTurno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GestionCajaMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigoTurno", expression = "java(generarCodigoTurno(request.getCodigoCaja(), request.getCodigoCajero()))")
    @Mapping(target = "inicioTurno", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "finTurno", ignore = true)
    @Mapping(target = "montoFinal", ignore = true)
    @Mapping(target = "montoCalculado", source = "montoInicial")
    @Mapping(target = "diferencia", constant = "0.00")
    @Mapping(target = "estado", constant = "ABIERTO")
    TurnoCaja toTurnoCaja(AbrirTurnoRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigoTurno", expression = "java(generarCodigoTurno(request.getCodigoCaja(), request.getCodigoCajero()))")
    TransaccionTurno toTransaccionTurno(ProcesarTransaccionRequest request);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigoTurno", expression = "java(generarCodigoTurno(request.getCodigoCaja(), request.getCodigoCajero()))")
    @Mapping(target = "tipoTransaccion", constant = "CIERRE")
    TransaccionTurno toTransaccionCierre(CerrarTurnoRequest request);
    
    TurnoResponse toTurnoResponse(TurnoCaja turnoCaja);
    
    List<TurnoResponse> toTurnoResponseList(List<TurnoCaja> turnosCaja);
    
    List<TransaccionTurno.Denominacion> toDenominacionListFromAbrir(List<AbrirTurnoRequest.DenominacionDto> denominaciones);
    
    List<TransaccionTurno.Denominacion> toDenominacionListFromProcesar(List<ProcesarTransaccionRequest.DenominacionDto> denominaciones);
    
    List<TransaccionTurno.Denominacion> toDenominacionListFromCerrar(List<CerrarTurnoRequest.DenominacionDto> denominaciones);
    
    TransaccionTurno.Denominacion toDenominacionFromAbrir(AbrirTurnoRequest.DenominacionDto denominacionDto);
    
    TransaccionTurno.Denominacion toDenominacionFromProcesar(ProcesarTransaccionRequest.DenominacionDto denominacionDto);
    
    TransaccionTurno.Denominacion toDenominacionFromCerrar(CerrarTurnoRequest.DenominacionDto denominacionDto);
    
    default String generarCodigoTurno(String codigoCaja, String codigoCajero) {
        return codigoCaja + "-" + codigoCajero + "-" + 
               java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
} 