package ec.edu.espe.arquitectura.service;

import ec.edu.espe.arquitectura.dto.AbrirTurnoRequest;
import ec.edu.espe.arquitectura.dto.ProcesarTransaccionRequest;
import ec.edu.espe.arquitectura.dto.CerrarTurnoRequest;
import ec.edu.espe.arquitectura.dto.TurnoResponse;
import ec.edu.espe.arquitectura.entity.TurnoCaja;
import ec.edu.espe.arquitectura.entity.TransaccionTurno;
import ec.edu.espe.arquitectura.exception.TurnoYaAbiertoException;
import ec.edu.espe.arquitectura.exception.TurnoNoAbiertoException;
import ec.edu.espe.arquitectura.exception.DiferenciaEnCierreException;
import ec.edu.espe.arquitectura.exception.ResourceNotFoundException;
import ec.edu.espe.arquitectura.mapper.GestionCajaMapper;
import ec.edu.espe.arquitectura.repository.TurnoCajaRepository;
import ec.edu.espe.arquitectura.repository.TransaccionTurnoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class GestionCajaService {

    private final TurnoCajaRepository turnoCajaRepository;
    private final TransaccionTurnoRepository transaccionTurnoRepository;
    private final GestionCajaMapper gestionCajaMapper;

    public TurnoResponse abrirTurno(AbrirTurnoRequest request) {
        if (turnoCajaRepository.existsByCodigoCajaAndEstado(request.getCodigoCaja(), "ABIERTO")) {
            log.error("Ya existe un turno abierto para la caja: {}", request.getCodigoCaja());
            throw new TurnoYaAbiertoException("Ya existe un turno abierto para la caja: " + request.getCodigoCaja());
        }

        if (turnoCajaRepository.existsByCodigoCajeroAndEstado(request.getCodigoCajero(), "ABIERTO")) {
            log.error("El cajero {} ya tiene un turno abierto", request.getCodigoCajero());
            throw new TurnoYaAbiertoException("El cajero ya tiene un turno abierto: " + request.getCodigoCajero());
        }

        BigDecimal montoCalculado = calcularMontoDenominaciones(request.getDenominaciones());
        if (request.getMontoInicial().compareTo(montoCalculado) != 0) {
            log.error("El monto inicial {} no coincide con las denominaciones: {}", 
                    request.getMontoInicial(), montoCalculado);
            throw new IllegalArgumentException("El monto inicial no coincide con las denominaciones");
        }

        TurnoCaja turnoCaja = gestionCajaMapper.toTurnoCaja(request);
        turnoCaja = turnoCajaRepository.save(turnoCaja);

        TransaccionTurno transaccionInicio = new TransaccionTurno();
        transaccionInicio.setCodigoTurno(turnoCaja.getCodigoTurno());
        transaccionInicio.setTipoTransaccion("INICIO");
        transaccionInicio.setMontoTotal(request.getMontoInicial());
        transaccionInicio.setDenominaciones(gestionCajaMapper.toDenominacionListFromAbrir(request.getDenominaciones()));
        transaccionTurnoRepository.save(transaccionInicio);

        TurnoResponse response = gestionCajaMapper.toTurnoResponse(turnoCaja);
        response.setMensaje("Turno abierto exitosamente");
        log.info("Turno abierto: {} - Cajero: {} - Monto: {}", 
                turnoCaja.getCodigoTurno(), request.getCodigoCajero(), request.getMontoInicial());
        
        return response;
    }

    public TurnoResponse procesarTransaccion(ProcesarTransaccionRequest request) {
        TurnoCaja turnoCaja = buscarTurnoAbierto(request.getCodigoCaja(), request.getCodigoCajero());

        BigDecimal montoCalculado = calcularMontoDenominaciones(request.getDenominaciones());
        if (request.getMontoTotal().compareTo(montoCalculado) != 0) {
            log.error("El monto total {} no coincide con las denominaciones: {}", 
                    request.getMontoTotal(), montoCalculado);
            throw new IllegalArgumentException("El monto total no coincide con las denominaciones");
        }

        TransaccionTurno transaccion = gestionCajaMapper.toTransaccionTurno(request);
        transaccionTurnoRepository.save(transaccion);

        BigDecimal nuevoMontoCalculado = calcularNuevoMontoCalculado(turnoCaja.getCodigoTurno());
        turnoCaja.setMontoCalculado(nuevoMontoCalculado);
        turnoCajaRepository.save(turnoCaja);

        TurnoResponse response = gestionCajaMapper.toTurnoResponse(turnoCaja);
        response.setMensaje("Transacción procesada exitosamente");
        log.info("Transacción {} procesada: {} - Monto: {} - Nuevo saldo: {}", 
                request.getTipoTransaccion(), turnoCaja.getCodigoTurno(), request.getMontoTotal(), nuevoMontoCalculado);
        
        return response;
    }

    public TurnoResponse cerrarTurno(CerrarTurnoRequest request) {
        TurnoCaja turnoCaja = buscarTurnoAbierto(request.getCodigoCaja(), request.getCodigoCajero());

        BigDecimal montoCalculado = calcularMontoDenominaciones(request.getDenominaciones());
        if (request.getMontoFinal().compareTo(montoCalculado) != 0) {
            log.error("El monto final {} no coincide con las denominaciones: {}", 
                    request.getMontoFinal(), montoCalculado);
            throw new IllegalArgumentException("El monto final no coincide con las denominaciones");
        }

        TransaccionTurno transaccionCierre = gestionCajaMapper.toTransaccionCierre(request);
        transaccionTurnoRepository.save(transaccionCierre);

        BigDecimal diferencia = request.getMontoFinal().subtract(turnoCaja.getMontoCalculado());
        
        turnoCaja.setFinTurno(LocalDateTime.now());
        turnoCaja.setMontoFinal(request.getMontoFinal());
        turnoCaja.setDiferencia(diferencia);
        turnoCaja.setEstado("CERRADO");
        turnoCajaRepository.save(turnoCaja);

        if (diferencia.compareTo(BigDecimal.ZERO) != 0) {
            log.warn("ALERTA: Diferencia en cierre - Turno: {} - Diferencia: ${}", 
                    turnoCaja.getCodigoTurno(), diferencia);
            throw new DiferenciaEnCierreException(
                String.format("Diferencia detectada en el cierre: $%.2f. Monto final: $%.2f, Monto calculado: $%.2f", 
                    diferencia, request.getMontoFinal(), turnoCaja.getMontoCalculado())
            );
        }

        TurnoResponse response = gestionCajaMapper.toTurnoResponse(turnoCaja);
        response.setMensaje("Turno cerrado exitosamente sin diferencias");
        log.info("Turno cerrado: {} - Monto final: {} - Sin diferencias", 
                turnoCaja.getCodigoTurno(), request.getMontoFinal());
        
        return response;
    }

    private TurnoCaja buscarTurnoAbierto(String codigoCaja, String codigoCajero) {
        List<TurnoCaja> turnosAbiertos = turnoCajaRepository.findByCodigoCajaAndEstado(codigoCaja, "ABIERTO");
        
        for (TurnoCaja turno : turnosAbiertos) {
            if (turno.getCodigoCajero().equals(codigoCajero)) {
                return turno;
            }
        }

        log.error("No se encontró un turno abierto para caja: {} y cajero: {}", codigoCaja, codigoCajero);
        throw new TurnoNoAbiertoException("No se encontró un turno abierto para la caja y cajero especificados");
    }

    private BigDecimal calcularMontoDenominaciones(List<?> denominaciones) {
        BigDecimal total = BigDecimal.ZERO;
        
        for (Object denom : denominaciones) {
            if (denom instanceof AbrirTurnoRequest.DenominacionDto) {
                total = total.add(((AbrirTurnoRequest.DenominacionDto) denom).getMonto());
            } else if (denom instanceof ProcesarTransaccionRequest.DenominacionDto) {
                total = total.add(((ProcesarTransaccionRequest.DenominacionDto) denom).getMonto());
            } else if (denom instanceof CerrarTurnoRequest.DenominacionDto) {
                total = total.add(((CerrarTurnoRequest.DenominacionDto) denom).getMonto());
            }
        }
        
        return total;
    }

    private BigDecimal calcularNuevoMontoCalculado(String codigoTurno) {
        List<TransaccionTurno> transacciones = transaccionTurnoRepository.findByCodigoTurno(codigoTurno);
        
        BigDecimal montoCalculado = BigDecimal.ZERO;
        for (TransaccionTurno transaccion : transacciones) {
            switch (transaccion.getTipoTransaccion()) {
                case "INICIO":
                case "DEPOSITO":
                    montoCalculado = montoCalculado.add(transaccion.getMontoTotal());
                    break;
                case "RETIRO":
                    montoCalculado = montoCalculado.subtract(transaccion.getMontoTotal());
                    break;
            }
        }
        
        return montoCalculado;
    }

    public TurnoResponse obtenerEstadoTurno(String codigoCaja, String codigoCajero) {
        try {
            TurnoCaja turnoCaja = buscarTurnoAbierto(codigoCaja, codigoCajero);
            TurnoResponse response = gestionCajaMapper.toTurnoResponse(turnoCaja);
            response.setMensaje("Turno encontrado");
            return response;
        } catch (TurnoNoAbiertoException e) {
            TurnoResponse response = new TurnoResponse();
            response.setCodigoCaja(codigoCaja);
            response.setCodigoCajero(codigoCajero);
            response.setEstado("SIN_TURNO");
            response.setMensaje("No hay turno abierto");
            return response;
        }
    }

    public List<TurnoResponse> obtenerHistorialTurnos(String codigoCaja) {
        List<TurnoCaja> turnos = turnoCajaRepository.findByCodigoCaja(codigoCaja);
        List<TurnoResponse> response = gestionCajaMapper.toTurnoResponseList(turnos);
        return response;
    }

    public boolean consultarTurnosAbiertos(String codigoCaja) {
        return turnoCajaRepository.existsByCodigoCajaAndEstado(codigoCaja, "ABIERTO");
    }
} 